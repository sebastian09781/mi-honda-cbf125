package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.components.AddExpenseDialog
import com.example.ui.components.AddFuelDialog
import com.example.ui.components.AddHistoryRecordDialog
import com.example.ui.components.CalendarReminderDialog
import com.example.ui.components.EditDocumentDialog
import com.example.ui.components.EditManualDialog
import com.example.ui.components.EditMotoProfileDialog
import com.example.ui.components.RealPdfViewerDialog
import com.example.ui.components.RegisterMaintenanceDoneDialog
import com.example.ui.components.UpdateMileageDialog
import com.example.ui.screens.DocumentsScreen
import com.example.ui.screens.ExpensesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MaintenanceScreen
import com.example.ui.screens.MyMotoScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.EditorialBackground
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MotoViewModel

// Added imports
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : ComponentActivity() {
    private val viewModel: MotoViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { false }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                var showSplash by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    delay(2500)
                    showSplash = false
                }

                Crossfade(
                    targetState = showSplash,
                    animationSpec = tween(durationMillis = 1000),
                    label = "splash_transition"
                ) { isSplash ->
                    if (isSplash) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.screensplash),
                                contentDescription = "Splash Screen",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    } else {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = EditorialBackground
                        ) {
                            MainAppContent(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: MotoViewModel) {
    val isOnboardingCompleted by viewModel.isOnboardingCompleted.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()
    val moto by viewModel.motorcycle.collectAsState()
    val schedules by viewModel.schedules.collectAsState()
    val currentKm = moto?.currentMileageKm ?: 0

    val showUpdateKm by viewModel.showUpdateKmDialog.collectAsState()
    val showAddMaintenance by viewModel.showAddMaintenanceDialog.collectAsState()
    val showAddHistory by viewModel.showAddHistoryDialog.collectAsState()
    val showAddFuel by viewModel.showAddFuelDialog.collectAsState()
    val showAddExpense by viewModel.showAddExpenseDialog.collectAsState()
    val showEditMoto by viewModel.showEditMotoDialog.collectAsState()

    Crossfade(targetState = isOnboardingCompleted, label = "onboarding_transition") { completed ->
        if (!completed) {
            OnboardingScreen(viewModel = viewModel)
        } else {
            Crossfade(targetState = currentScreen, label = "screen_transition") { screen ->
                when (screen) {
                    AppScreen.INICIO -> HomeScreen(viewModel = viewModel)
                    AppScreen.MI_MOTO -> MyMotoScreen(viewModel = viewModel)
                    AppScreen.SERVICIOS -> MaintenanceScreen(viewModel = viewModel)
                    AppScreen.GASTOS -> ExpensesScreen(viewModel = viewModel)
                    AppScreen.PAPELES -> DocumentsScreen(viewModel = viewModel)
                    AppScreen.CONFIGURACION -> SettingsScreen(viewModel = viewModel)
                }
            }
        }
    }

    // Dialogs
    if (showUpdateKm) {
        UpdateMileageDialog(
            currentKm = currentKm,
            onDismiss = { viewModel.showUpdateKmDialog.value = false },
            onConfirm = { newKm ->
                viewModel.updateMileage(newKm)
                viewModel.showUpdateKmDialog.value = false
            }
        )
    }

    if (showAddMaintenance) {
        RegisterMaintenanceDoneDialog(
            schedules = schedules,
            currentKm = currentKm,
            onDismiss = { viewModel.showAddMaintenanceDialog.value = false },
            onConfirm = { scheduleId, title, category, cost, workshop, km, interval, notes ->
                viewModel.registerMaintenanceDone(
                    scheduleId = scheduleId,
                    title = title,
                    category = category,
                    cost = cost,
                    workshop = workshop,
                    currentKm = km,
                    intervalKm = interval,
                    notes = notes
                )
                viewModel.showAddMaintenanceDialog.value = false
            }
        )
    }

    if (showAddHistory) {
        AddHistoryRecordDialog(
            currentKm = currentKm,
            onDismiss = { viewModel.showAddHistoryDialog.value = false },
            onConfirm = { title, category, day, month, year, km, workshop, cost, notes, receiptUri ->
                viewModel.addServiceRecord(
                    title = title,
                    category = category,
                    day = day,
                    month = month,
                    year = year,
                    mileageKm = km,
                    workshop = workshop,
                    cost = cost,
                    notes = notes,
                    receiptPhotoUri = receiptUri
                )
                viewModel.showAddHistoryDialog.value = false
            }
        )
    }

    if (showAddFuel) {
        AddFuelDialog(
            currentKm = currentKm,
            onDismiss = { viewModel.showAddFuelDialog.value = false },
            onConfirm = { liters, cost, km, notes ->
                viewModel.addFuelRecord(
                    liters = liters,
                    totalCost = cost,
                    mileageKm = km,
                    notes = notes
                )
                viewModel.showAddFuelDialog.value = false
            }
        )
    }

    if (showAddExpense) {
        AddExpenseDialog(
            onDismiss = { viewModel.showAddExpenseDialog.value = false },
            onConfirm = { category, title, amount, notes ->
                viewModel.addExpense(
                    category = category,
                    title = title,
                    amount = amount,
                    notes = notes
                )
                viewModel.showAddExpenseDialog.value = false
            }
        )
    }

    if (showEditMoto) {
        EditMotoProfileDialog(
            motorcycle = moto,
            onDismiss = { viewModel.showEditMotoDialog.value = false },
            onConfirm = { updatedMoto ->
                viewModel.updateMotorcycle(updatedMoto)
                viewModel.showEditMotoDialog.value = false
            }
        )
    }

    val selectedManualToEdit by viewModel.selectedManualToEdit.collectAsState()
    selectedManualToEdit?.let { manual ->
        EditManualDialog(
            manual = manual,
            onDismiss = { viewModel.selectedManualToEdit.value = null },
            onSave = { title, newUri ->
                viewModel.updateManual(manual.id, title, newUri)
            },
            onResetToDefault = if (manual.isCustom) {
                { viewModel.resetManualToDefault(manual.id) }
            } else null
        )
    }

    val selectedDocumentToEdit by viewModel.selectedDocumentToEdit.collectAsState()
    selectedDocumentToEdit?.let { doc ->
        EditDocumentDialog(
            document = doc,
            onDismiss = { viewModel.selectedDocumentToEdit.value = null },
            onSave = { title, status, validityDate, referenceNumber, newUri ->
                viewModel.updateDocument(doc.id, title, status, validityDate, referenceNumber, newUri)
            },
            onResetToDefault = if (doc.isCustom) {
                { viewModel.resetDocumentToDefault(doc.id) }
            } else null
        )
    }

    val selectedPdfToView by viewModel.selectedPdfToView.collectAsState()
    selectedPdfToView?.let { pdfRequest ->
        RealPdfViewerDialog(
            id = pdfRequest.id,
            title = pdfRequest.title,
            category = pdfRequest.category,
            assetPath = pdfRequest.assetPath,
            customFilePath = pdfRequest.customFilePath,
            fileName = pdfRequest.fileName,
            isCustom = pdfRequest.isCustom,
            onDismiss = { viewModel.selectedPdfToView.value = null },
            onReplacePdf = { uri -> viewModel.replaceCurrentOpenPdf(uri) },
            onResetToDefault = { viewModel.resetCurrentOpenPdf() }
        )
    }

    val selectedCalendarReminder by viewModel.selectedCalendarReminder.collectAsState()
    selectedCalendarReminder?.let { reminderData ->
        CalendarReminderDialog(
            data = reminderData,
            onDismiss = { viewModel.dismissCalendarReminder() }
        )
    }
}
