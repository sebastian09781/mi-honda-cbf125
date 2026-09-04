package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Opacity
import com.example.ui.components.CalendarReminderData
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.EditorialBadge
import com.example.ui.components.MotoBackdropBackground
import com.example.ui.components.MotoBottomNavigationBar
import com.example.ui.components.MotoTopHeaderBar
import com.example.ui.theme.DarkSurfaceSubtle
import com.example.ui.theme.EditorialBackground
import com.example.ui.theme.EditorialBorder
import com.example.ui.theme.EditorialContainerPill
import com.example.ui.theme.EditorialDivider
import com.example.ui.theme.EditorialPrimary
import com.example.ui.theme.EditorialPurpleAccent
import com.example.ui.theme.EditorialSuccess
import com.example.ui.theme.EditorialSurface
import com.example.ui.theme.EditorialTextPrimary
import com.example.ui.theme.EditorialTextSecondary
import com.example.ui.theme.EditorialTextTertiary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MotoViewModel
import java.text.DecimalFormat

@Composable
fun HomeScreen(
    viewModel: MotoViewModel,
    modifier: Modifier = Modifier
) {
    val moto by viewModel.motorcycle.collectAsState()
    val schedules by viewModel.schedules.collectAsState()
    val documents by viewModel.documents.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()

    val currentKm = moto?.currentMileageKm ?: 0
    val kmFormatter = DecimalFormat("#,###")
    val nextSchedule = schedules.firstOrNull()
    val kmRemaining = nextSchedule?.getKmRemaining(currentKm) ?: 0

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
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
            drawableRes = R.drawable.moto_06_lateral_derecha
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 22.dp, vertical = 12.dp)
            ) {
                // Header: Honda Wing & Settings
                MotoTopHeaderBar(
                    title = null,
                    showBackButton = false,
                    showSettings = true,
                    onSettingsClick = { viewModel.navigateTo(AppScreen.CONFIGURACION) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 1. HERO MOTO LIMPIO & FLOTANTE (Sin silueta duplicada fantasma)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(190.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Resplandor ambiental de estudio verde Honda
                        Box(
                            modifier = Modifier
                                .size(240.dp, 130.dp)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            EditorialPrimary.copy(alpha = 0.16f),
                                            Color.Transparent
                                        )
                                    )
                                )
                        )

                        // Primer Plano Nítido: FRONTAL DERECHA (moto_01_frontal_derecha)
                        Image(
                            painter = painterResource(id = R.drawable.moto_01_frontal_derecha),
                            contentDescription = "Honda CBF 125 Frontal Derecha",
                            modifier = Modifier
                                .height(185.dp)
                                .fillMaxWidth(),
                            contentScale = ContentScale.Fit
                        )
                    }

                Spacer(modifier = Modifier.height(12.dp))

                // Identidad del Modelo: Logo estilizado CBF125
                Image(
                    painter = painterResource(id = R.drawable.logo_cbf125_white),
                    contentDescription = "CBF 125",
                    modifier = Modifier.height(30.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = EditorialContainerPill
                    ) {
                        Text(
                            text = if (moto?.licensePlate.isNullOrBlank()) "Por registrar" else moto!!.licensePlate,
                            color = EditorialPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = "·",
                        color = EditorialTextTertiary,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "${moto?.color ?: "Verde Candy"} · Modelo ${moto?.year ?: 2014}",
                        color = EditorialTextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // 2. ODÓMETRO PRINCIPAL EN GRANDE Y ESPACIOSO
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("odometer_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = EditorialSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "KILOMETRAJE ACTUAL",
                            color = EditorialTextTertiary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )

                        Surface(
                            shape = CircleShape,
                            color = EditorialContainerPill,
                            modifier = Modifier.clickable { viewModel.showUpdateKmDialog.value = true }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = null,
                                    tint = EditorialPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Actualizar",
                                    color = EditorialPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = kmFormatter.format(currentKm).replace(",", "."),
                            color = EditorialTextPrimary,
                            fontSize = 38.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-1).sp
                        )
                        Text(
                            text = "KM",
                            color = EditorialPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3. ACCIONES RÁPIDAS (3 botones amplios, limpios y separados)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MinimalActionButton(
                    icon = Icons.Filled.LocalGasStation,
                    label = "Tanqueo",
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.showAddFuelDialog.value = true }
                )
                MinimalActionButton(
                    icon = Icons.Filled.Build,
                    label = "Servicio",
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.showAddMaintenanceDialog.value = true }
                )
                MinimalActionButton(
                    icon = Icons.Filled.AttachMoney,
                    label = "Gasto",
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.showAddExpenseDialog.value = true }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. ESTADO & PRÓXIMO SERVICIO (1 sola tarjeta unificada, sin saturación)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.navigateTo(AppScreen.SERVICIOS) },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = EditorialSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Próximo mantenimiento
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
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(EditorialContainerPill),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Opacity,
                                    contentDescription = null,
                                    tint = EditorialPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = nextSchedule?.title ?: "Cambio de aceite",
                                    color = EditorialTextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "Mobil Super 4T 20W-50 · en ${kmFormatter.format(kmRemaining).replace(",", ".")} km",
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
                                        val serviceTitle = nextSchedule?.title ?: "Cambio de aceite"
                                        viewModel.openCalendarReminder(
                                            CalendarReminderData(
                                                title = "Mantenimiento Honda CBF-125: $serviceTitle",
                                                description = "Servicio preventivo para Honda CBF 125 [$plate]. Restan ${kmFormatter.format(kmRemaining).replace(",", ".")} km.",
                                                defaultNoticeMinutes = 1440,
                                                location = "Taller de confianza / Serviteca"
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

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = null,
                                tint = EditorialTextTertiary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    HorizontalDivider(color = EditorialDivider)

                    // SOAT & Tránsito
                    val soatDoc = documents.firstOrNull { it.id == "doc_soat" }
                    val isSoatLoaded = soatDoc?.customFilePath != null || soatDoc?.isCustom == true || (soatDoc?.fileName?.isNotBlank() == true)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.navigateTo(AppScreen.PAPELES) },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(EditorialContainerPill),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Shield,
                                    contentDescription = null,
                                    tint = EditorialSuccess,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = if (moto?.licensePlate.isNullOrBlank()) "Billetera Digital" else "Documentos · ${moto!!.licensePlate}",
                                    color = EditorialTextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = if (isSoatLoaded) "SOAT ${soatDoc?.validityDate ?: "Al día"} · En regla" else "Toca para subir tus documentos en PDF",
                                    color = if (isSoatLoaded) EditorialSuccess else EditorialTextSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
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
                                                title = "Vencimiento SOAT · Honda CBF 125 [$plate]",
                                                description = "Renovación obligatoria de SOAT para la motocicleta Honda CBF-125 placa $plate. Vigencia: ${soatDoc?.validityDate ?: "Anual"}.",
                                                defaultNoticeMinutes = 10080,
                                                location = "Aseguradora / Tránsito"
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

                            EditorialBadge(
                                text = if (isSoatLoaded) "AL DÍA" else "PENDIENTE",
                                backgroundColor = if (isSoatLoaded) EditorialContainerPill else com.example.ui.theme.EditorialSurfaceSubtle,
                                textColor = if (isSoatLoaded) EditorialSuccess else EditorialTextSecondary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ACCESO RÁPIDO A RECORDATORIOS DE GOOGLE CALENDAR
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
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(EditorialContainerPill),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CalendarMonth,
                                contentDescription = null,
                                tint = EditorialPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Recordatorios de Calendario",
                                color = EditorialTextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "Sincroniza alertas de SOAT y citas de taller con Google Calendar",
                                color = EditorialTextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = EditorialContainerPill,
                        border = androidx.compose.foundation.BorderStroke(1.dp, EditorialPrimary),
                        modifier = Modifier.clickable {
                            val plate = moto?.licensePlate?.takeIf { it.isNotBlank() } ?: "CBF-125"
                            viewModel.openCalendarReminder(
                                CalendarReminderData(
                                    title = "Recordatorio Honda CBF 125 [$plate]",
                                    description = "Recordatorio agendado desde Mi Honda CBF-125 para placa $plate.",
                                    defaultNoticeMinutes = 1440
                                )
                            )
                        }
                    ) {
                        Text(
                            text = "+ Agendar",
                            color = EditorialPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
}

@Composable
private fun MinimalActionButton(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(76.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = EditorialSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = EditorialPrimary,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                color = EditorialTextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            )
        }
    }
}
