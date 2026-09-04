package com.example.ui.screens

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import com.example.ui.components.CalendarReminderData
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.data.model.MaintenanceSchedule
import com.example.data.model.ServiceRecord
import com.example.ui.components.EditorialBadge
import com.example.ui.components.MotoBackdropBackground
import com.example.ui.components.MotoBottomNavigationBar
import com.example.ui.components.MotoTopHeaderBar
import com.example.ui.theme.EditorialBackground
import com.example.ui.theme.EditorialBorder
import com.example.ui.theme.EditorialContainer
import com.example.ui.theme.EditorialContainerPill
import com.example.ui.theme.EditorialHighlightRose
import com.example.ui.theme.EditorialHighlightRoseText
import com.example.ui.theme.EditorialPrimary
import com.example.ui.theme.EditorialPurpleAccent
import com.example.ui.theme.EditorialSuccess
import com.example.ui.theme.EditorialSuccessContainer
import com.example.ui.theme.EditorialSurface
import com.example.ui.theme.EditorialSurfaceSubtle
import com.example.ui.theme.EditorialTextPrimary
import com.example.ui.theme.EditorialTextSecondary
import com.example.ui.theme.EditorialTextTertiary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MotoViewModel
import java.text.DecimalFormat

@Composable
fun MaintenanceScreen(
    viewModel: MotoViewModel,
    modifier: Modifier = Modifier
) {
    val moto by viewModel.motorcycle.collectAsState()
    val schedules by viewModel.schedules.collectAsState()
    val records by viewModel.serviceRecords.collectAsState()
    val servicesSubTab by viewModel.servicesSubTab.collectAsState()
    val maintTab by viewModel.maintTab.collectAsState()
    val historyFilter by viewModel.historyFilter.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()

    val currentKm = moto?.currentMileageKm ?: 0
    val kmFormatter = DecimalFormat("#,###")
    val filterOptions = listOf("Todos", "Mantenimientos", "Repuestos", "Otro")

    var previewReceiptUri by remember { mutableStateOf<String?>(null) }

    // Dialog to view attached receipt photo
    if (previewReceiptUri != null) {
        Dialog(onDismissRequest = { previewReceiptUri = null }) {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = EditorialSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Comprobante / Factura",
                        fontWeight = FontWeight.Bold,
                        color = EditorialTextPrimary,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    AsyncImage(
                        model = Uri.parse(previewReceiptUri),
                        contentDescription = "Foto de factura",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { previewReceiptUri = null },
                        colors = ButtonDefaults.buttonColors(containerColor = EditorialPrimary)
                    ) {
                        Text("Cerrar", color = Color(0xFF0F1704), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("services_screen"),
        containerColor = EditorialBackground,
        bottomBar = {
            MotoBottomNavigationBar(
                currentScreen = currentScreen,
                onNavigate = { viewModel.navigateTo(it) }
            )
        }
    ) { innerPadding ->
        MotoBackdropBackground(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            alpha = 0.08f,
            drawableRes = com.example.R.drawable.moto_01_frontal_derecha
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 22.dp, vertical = 12.dp)
            ) {
                // Top Header Bar with Settings
                MotoTopHeaderBar(
                    title = "Servicios y Taller",
                    showBackButton = false,
                    showSettings = true,
                    onSettingsClick = { viewModel.navigateTo(AppScreen.CONFIGURACION) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // SUB-PESTAÑAS PRINCIPALES: [ Programados ] | [ Historial ]
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(23.dp),
                    color = EditorialSurfaceSubtle,
                    border = BorderStroke(1.dp, EditorialBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (servicesSubTab == 0) EditorialPrimary else Color.Transparent)
                                .clickable { viewModel.setServicesSubTab(0) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Programados",
                                color = if (servicesSubTab == 0) Color(0xFF0F1704) else EditorialTextSecondary,
                                fontWeight = if (servicesSubTab == 0) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (servicesSubTab == 1) EditorialPrimary else Color.Transparent)
                                .clickable { viewModel.setServicesSubTab(1) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Historial",
                                color = if (servicesSubTab == 1) Color(0xFF0F1704) else EditorialTextSecondary,
                                fontWeight = if (servicesSubTab == 1) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // CONTENIDO SEGÚN SUB-PESTAÑA SELECCIONADA
                if (servicesSubTab == 0) {
                    // SUB-PESTAÑA 0: MANTENIMIENTOS PROGRAMADOS
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Sub-conmutador: Por kilometraje vs Por tiempo
                        Surface(
                            modifier = Modifier.height(36.dp),
                            shape = RoundedCornerShape(18.dp),
                            color = EditorialSurfaceSubtle,
                            border = BorderStroke(1.dp, EditorialBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(if (maintTab == 0) EditorialContainerPill else Color.Transparent)
                                        .clickable { viewModel.setMaintTab(0) }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "Km",
                                        color = if (maintTab == 0) EditorialPrimary else EditorialTextSecondary,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(if (maintTab == 1) EditorialContainerPill else Color.Transparent)
                                        .clickable { viewModel.setMaintTab(1) }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "Tiempo",
                                        color = if (maintTab == 1) EditorialPrimary else EditorialTextSecondary,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }

                        // Botón para registrar mantenimiento rápido
                        Button(
                            onClick = { viewModel.showAddMaintenanceDialog.value = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EditorialContainerPill,
                                contentColor = EditorialPrimary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = EditorialPrimary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Registrar",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = EditorialPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val displayedSchedules = remember(schedules, maintTab, currentKm) {
                        if (maintTab == 0) {
                            schedules.sortedBy { it.getKmRemaining(currentKm) }
                        } else {
                            schedules.sortedBy { it.intervalMonths }
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(displayedSchedules) { sched ->
                            val remaining = sched.getKmRemaining(currentKm)
                            val isDue = sched.isDue(currentKm)

                            val icon = getMaterialYouIcon(sched.iconKey)
                            val iconColor = if (isDue && maintTab == 0) EditorialHighlightRoseText else EditorialPrimary
                            val containerColor = if (isDue && maintTab == 0) EditorialHighlightRose else EditorialContainerPill

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(22.dp),
                                colors = CardDefaults.cardColors(containerColor = EditorialSurface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    verticalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    // Fila 1: Icono, Título y Botón Hecho
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(46.dp)
                                                    .clip(CircleShape)
                                                    .background(containerColor),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = icon,
                                                    contentDescription = null,
                                                    tint = iconColor,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            }

                                            Spacer(modifier = Modifier.width(14.dp))

                                            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                                Text(
                                                    text = sched.title,
                                                    color = EditorialTextPrimary,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 15.sp
                                                )
                                                Text(
                                                    text = if (maintTab == 0) {
                                                        "Cada ${kmFormatter.format(sched.intervalKm).replace(",", ".")} km · ${sched.category}"
                                                    } else {
                                                        "Cada ${sched.intervalMonths} mes${if (sched.intervalMonths > 1) "es" else ""} · ${sched.category}"
                                                    },
                                                    color = EditorialTextSecondary,
                                                    fontSize = 12.sp
                                                )
                                            }
                                        }

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(34.dp)
                                                    .clip(CircleShape)
                                                    .background(EditorialContainerPill)
                                                    .clickable {
                                                        val plate = moto?.licensePlate?.takeIf { it.isNotBlank() } ?: "CBF-125"
                                                        viewModel.openCalendarReminder(
                                                            CalendarReminderData(
                                                                title = "Mantenimiento Honda CBF-125: ${sched.title}",
                                                                description = "Servicio preventivo para Honda CBF 125 [$plate]. Categoría: ${sched.category}. Próximo a los ${sched.nextDueKm} km (Faltan $remaining km).",
                                                                defaultNoticeMinutes = 1440,
                                                                location = "Taller de confianza / Taller Honda"
                                                            )
                                                        )
                                                    },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.CalendarMonth,
                                                    contentDescription = "Agendar en Google Calendar",
                                                    tint = EditorialPrimary,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }

                                            Button(
                                                onClick = {
                                                    viewModel.registerMaintenanceDone(
                                                        scheduleId = sched.id,
                                                        title = sched.title,
                                                        category = sched.category,
                                                        cost = 0.0,
                                                        workshop = "Taller Particular",
                                                        currentKm = currentKm,
                                                        intervalKm = sched.intervalKm,
                                                        notes = "Mantenimiento al día"
                                                    )
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = EditorialContainerPill,
                                                    contentColor = EditorialPrimary
                                                ),
                                                shape = CircleShape,
                                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        imageVector = Icons.Filled.Check,
                                                        contentDescription = null,
                                                        tint = EditorialPrimary,
                                                        modifier = Modifier.size(13.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = "Hecho",
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = EditorialPrimary
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    // Fila 2: Barra de progreso visual
                                    val progress = if (maintTab == 0) {
                                        if (sched.intervalKm > 0) {
                                            (1f - (remaining.toFloat() / sched.intervalKm.toFloat())).coerceIn(0f, 1f)
                                        } else 0f
                                    } else {
                                        (1f - (sched.intervalMonths.toFloat() / 14f)).coerceIn(0.15f, 1f)
                                    }

                                    LinearProgressIndicator(
                                        progress = { progress },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(6.dp)
                                            .clip(CircleShape),
                                        color = if (isDue && maintTab == 0) EditorialHighlightRoseText else EditorialPrimary,
                                        trackColor = EditorialSurfaceSubtle
                                    )

                                    // Fila 3: Información de meta y AVISO ESPACIOSO INTEGRADO
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = if (maintTab == 0) {
                                                "Meta: a los ${kmFormatter.format(sched.nextDueKm).replace(",", ".")} km"
                                            } else {
                                                "Último: ${sched.lastServiceDate.ifEmpty { "Al día" }}"
                                            },
                                            color = EditorialTextTertiary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium
                                        )

                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = if (maintTab == 0) {
                                                if (isDue) EditorialHighlightRose else EditorialContainerPill
                                            } else {
                                                EditorialContainerPill
                                            }
                                        ) {
                                            Text(
                                                text = if (maintTab == 0) {
                                                    if (isDue) "¡VENCIDO!" else "Faltan ${kmFormatter.format(remaining).replace(",", ".")} km"
                                                } else {
                                                    "Revisión cada ${sched.intervalMonths} mes${if (sched.intervalMonths > 1) "es" else ""}"
                                                },
                                                color = if (maintTab == 0 && isDue) EditorialHighlightRoseText else EditorialPrimary,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // SUB-PESTAÑA 1: HISTORIAL DE SERVICIOS REALIZADOS
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        filterOptions.forEach { opt ->
                            val isSelected = historyFilter == opt
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { viewModel.setHistoryFilter(opt) },
                                color = if (isSelected) EditorialPrimary else EditorialSurfaceSubtle,
                                border = BorderStroke(1.dp, if (isSelected) EditorialPrimary else EditorialBorder),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    text = opt,
                                    color = if (isSelected) Color(0xFF0F1704) else EditorialTextSecondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (records.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.History,
                                    contentDescription = null,
                                    tint = EditorialTextTertiary,
                                    modifier = Modifier.size(48.dp)
                                )
                                Text(
                                    text = "Sin registros de servicio aún",
                                    color = EditorialTextSecondary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Button(
                                    onClick = { viewModel.showAddHistoryDialog.value = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = EditorialPrimary)
                                ) {
                                    Text("Registrar Primer Servicio", color = Color(0xFF0F1704), fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(records) { record ->
                                val kmText = kmFormatter.format(record.mileageKm).replace(",", ".")
                                val costText = viewModel.formatCurrency(record.cost)
                                val icon = when {
                                    record.title.contains("aceite", ignoreCase = true) -> Icons.Filled.Opacity
                                    record.title.contains("cadena", ignoreCase = true) -> Icons.Filled.Settings
                                    record.title.contains("bujía", ignoreCase = true) || record.title.contains("bujia", ignoreCase = true) -> Icons.Filled.FlashOn
                                    record.title.contains("freno", ignoreCase = true) -> Icons.Filled.Shield
                                    record.title.contains("filtro", ignoreCase = true) -> Icons.Filled.Air
                                    record.title.contains("llanta", ignoreCase = true) -> Icons.Filled.Speed
                                    else -> Icons.Filled.Build
                                }

                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(22.dp),
                                    colors = CardDefaults.cardColors(containerColor = EditorialSurface),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(18.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Fecha
                                        Box(
                                            modifier = Modifier
                                                .size(50.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(EditorialContainerPill),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text(
                                                    text = record.dateDay,
                                                    color = EditorialPrimary,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    fontSize = 15.sp
                                                )
                                                Text(
                                                    text = record.dateMonth,
                                                    color = EditorialPurpleAccent,
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Column(
                                            modifier = Modifier.weight(1f),
                                            verticalArrangement = Arrangement.spacedBy(2.dp)
                                        ) {
                                            Text(
                                                text = record.title,
                                                color = EditorialTextPrimary,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                            Text(
                                                text = "$kmText km · ${record.workshop}",
                                                color = EditorialTextSecondary,
                                                fontSize = 12.sp
                                            )
                                            Text(
                                                text = costText,
                                                color = EditorialPrimary,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp
                                            )
                                        }

                                        // Foto de Factura adjunta (si existe)
                                        if (record.receiptPhotoUri != null) {
                                            IconButton(
                                                onClick = { previewReceiptUri = record.receiptPhotoUri },
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .clip(CircleShape)
                                                    .background(EditorialSurfaceSubtle)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.ReceiptLong,
                                                    contentDescription = "Ver factura",
                                                    tint = EditorialPrimary,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        } else {
                                            Box(
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .clip(CircleShape)
                                                    .background(EditorialSurfaceSubtle),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = icon,
                                                    contentDescription = null,
                                                    tint = EditorialTextTertiary,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun getMaterialYouIcon(iconKey: String): ImageVector {
    return when (iconKey.lowercase()) {
        "oil", "aceite" -> Icons.Filled.Opacity
        "chain", "cadena" -> Icons.Filled.Settings
        "spark", "bujia", "bujía" -> Icons.Filled.FlashOn
        "brake", "frenos", "freno" -> Icons.Filled.Shield
        "filter", "filtro" -> Icons.Filled.Air
        "tire", "llanta", "llantas" -> Icons.Filled.Speed
        else -> Icons.Filled.Build
    }
}
