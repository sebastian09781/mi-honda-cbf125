package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import com.example.ui.components.CalendarReminderData
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.EditorialBadge
import com.example.ui.components.MotoBackdropBackground
import com.example.ui.components.MotoBottomNavigationBar
import com.example.ui.components.MotoTopHeaderBar
import com.example.ui.theme.EditorialBackground
import com.example.ui.theme.EditorialContainerPill
import com.example.ui.theme.EditorialPrimary
import com.example.ui.theme.EditorialSuccess
import com.example.ui.theme.EditorialSuccessContainer
import com.example.ui.theme.EditorialSurface
import com.example.ui.theme.EditorialTextPrimary
import com.example.ui.theme.EditorialTextSecondary
import com.example.ui.theme.EditorialTextTertiary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MotoViewModel

@Composable
fun DocumentsScreen(
    viewModel: MotoViewModel,
    modifier: Modifier = Modifier
) {
    val documents by viewModel.documents.collectAsState()
    val moto by viewModel.motorcycle.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("documents_screen"),
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
            drawableRes = R.drawable.moto_03_frontal_izquierda
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 22.dp, vertical = 12.dp)
            ) {
            // Header
            MotoTopHeaderBar(
                title = "Papeles de Tránsito",
                showBackButton = false,
                showSettings = true,
                onSettingsClick = { viewModel.navigateTo(AppScreen.CONFIGURACION) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Título con aire y subtítulo minimalista
            Text(
                text = "BILLETERA OFICIAL",
                color = EditorialPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Toca para abrir a pantalla completa en retén",
                color = EditorialTextSecondary,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // LAS 4 TARJETAS CON ESPACIO GENEROSO
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                documents.forEach { doc ->
                    val isLoaded = doc.customFilePath != null || doc.isCustom || doc.fileName.isNotBlank()
                    val (icon, subtitle, badgeText) = when (doc.id) {
                        "doc_soat" -> Triple(
                            Icons.Filled.Shield,
                            if (isLoaded) "Vigencia: ${doc.validityDate}" else "Toca para cargar tu SOAT en PDF",
                            if (isLoaded) doc.status else "Sin cargar"
                        )
                        "doc_tarjeta_propiedad" -> Triple(
                            Icons.Filled.Description,
                            if (isLoaded) "Documento cargado en billetera" else "Toca para cargar Tarjeta de Propiedad en PDF",
                            if (isLoaded) doc.status else "Sin cargar"
                        )
                        "doc_licencia" -> Triple(
                            Icons.Filled.Badge,
                            if (isLoaded) "Categoría A2 · Conductor" else "Toca para cargar Licencia de Conducción",
                            if (isLoaded) doc.status else "Sin cargar"
                        )
                        "doc_cedula" -> Triple(
                            Icons.Filled.Person,
                            if (isLoaded) "Cédula digital cargada" else "Toca para cargar Cédula de Ciudadanía",
                            if (isLoaded) doc.status else "Sin cargar"
                        )
                        else -> Triple(
                            Icons.Filled.Description,
                            if (isLoaded) "Vigencia: ${doc.validityDate}" else "Sin documento cargado",
                            if (isLoaded) doc.status else "Sin cargar"
                        )
                    }

                    SpaciousDocCard(
                        title = doc.title,
                        subtitle = subtitle,
                        badgeText = badgeText,
                        icon = icon,
                        onScheduleCalendar = {
                            val plate = moto?.licensePlate?.takeIf { it.isNotBlank() } ?: "CBF-125"
                            viewModel.openCalendarReminder(
                                CalendarReminderData(
                                    title = "Vencimiento ${doc.title} · Honda CBF 125 [$plate]",
                                    description = "Recordatorio para trámite y vigencia de ${doc.title} de la motocicleta Honda CBF-125 con placa $plate. Referencia: ${doc.referenceNumber}.",
                                    defaultNoticeMinutes = if (doc.id == "doc_soat") 10080 else 1440,
                                    location = if (doc.id == "doc_soat") "Aseguradora / Tránsito" else "Organismo de Tránsito"
                                )
                            )
                        },
                        onClick = {
                            if (isLoaded) {
                                viewModel.openDocumentPdf(doc)
                            } else {
                                viewModel.selectedDocumentToEdit.value = doc
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
}

@Composable
private fun SpaciousDocCard(
    title: String,
    subtitle: String,
    badgeText: String,
    icon: ImageVector,
    onScheduleCalendar: (() -> Unit)? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("spacious_doc_card_$title"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = EditorialSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(EditorialContainerPill),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = EditorialPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = title,
                        color = EditorialTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        letterSpacing = (-0.3).sp
                    )
                    Text(
                        text = subtitle,
                        color = EditorialTextSecondary,
                        fontSize = 13.sp
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (onScheduleCalendar != null) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(EditorialContainerPill)
                            .clickable { onScheduleCalendar() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CalendarMonth,
                            contentDescription = "Agendar en Google Calendar",
                            tint = EditorialPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = EditorialContainerPill
                ) {
                    Text(
                        text = badgeText,
                        color = EditorialPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    tint = EditorialTextTertiary,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
