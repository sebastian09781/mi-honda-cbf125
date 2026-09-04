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
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.ui.theme.EditorialBackground
import com.example.ui.theme.EditorialContainerPill
import com.example.ui.theme.EditorialDivider
import com.example.ui.theme.EditorialPrimary
import com.example.ui.theme.EditorialSurface
import com.example.ui.theme.EditorialTextPrimary
import com.example.ui.theme.EditorialTextSecondary
import com.example.ui.theme.EditorialTextTertiary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MotoViewModel

@Composable
fun MyMotoScreen(
    viewModel: MotoViewModel,
    modifier: Modifier = Modifier
) {
    val moto by viewModel.motorcycle.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()
    val manuals by viewModel.manuals.collectAsState()

    // Galería de ángulos de la CBF 125 (Frontal por defecto)
    val motoAngles = listOf(
        Triple("Frontal", R.drawable.moto_07_frontal, "Vista frontal"),
        Triple("Frontal Der", R.drawable.moto_01_frontal_derecha, "Frontal 3/4"),
        Triple("Lateral Izq", R.drawable.moto_09_lateral_izquierda, "Lateral izquierda"),
        Triple("Lateral Der", R.drawable.moto_06_lateral_derecha, "Lateral derecha"),
        Triple("Trasera", R.drawable.moto_08_trasera_centrada, "Vista trasera")
    )
    var selectedAngleIndex by remember { mutableIntStateOf(0) }
    val currentAngle = motoAngles[selectedAngleIndex]

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("my_moto_screen"),
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
                // Top Header Bar
                MotoTopHeaderBar(
                    title = "Mi Moto",
                    showBackButton = false,
                    showSettings = true,
                    onSettingsClick = { viewModel.navigateTo(AppScreen.CONFIGURACION) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 1. HERO MOTO CINEMATOGRÁFICO (Frontal en primer plano)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(190.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                selectedAngleIndex = (selectedAngleIndex + 1) % motoAngles.size
                            },
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

                        // Primer Plano Nítido: (Toca la moto para rotar)
                        Image(
                            painter = painterResource(id = currentAngle.second),
                            contentDescription = "Honda CBF 125 ${currentAngle.first}",
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
                            text = moto?.licensePlate?.takeIf { it.isNotBlank() } ?: "POR REGISTRAR",
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
                        text = "${moto?.color?.takeIf { it.isNotBlank() } ?: "Color por definir"} · Modelo ${moto?.year?.takeIf { it > 0 } ?: 2024}",
                        color = EditorialTextSecondary,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // 2. GUÍA DE REPUESTOS Y FLUIDOS (Espaciosa y Clara)
            Text(
                text = "GUÍA DE REPUESTOS Y FLUIDOS",
                color = EditorialPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Especificaciones oficiales para compra y mantenimiento",
                color = EditorialTextSecondary,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = EditorialSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SpaciousSpecRow(
                        icon = Icons.Filled.Opacity,
                        label = "Aceite de Motor",
                        value = "Mobil Super 4T 20W-50",
                        note = "Capacidad: 1.0 Litro"
                    )
                    HorizontalDivider(color = EditorialDivider)

                    SpaciousSpecRow(
                        icon = Icons.Filled.ElectricBolt,
                        label = "Bujía Oficial",
                        value = "NGK CPR7EA-9",
                        note = "Calibración: 0.8 - 0.9 mm"
                    )
                    HorizontalDivider(color = EditorialDivider)

                    SpaciousSpecRow(
                        icon = Icons.Filled.Speed,
                        label = "Medida de Llantas",
                        value = "80/100-17 (Del) · 90/90-17 (Tras)",
                        note = "Rin 17 de aleación"
                    )
                    HorizontalDivider(color = EditorialDivider)

                    SpaciousSpecRow(
                        icon = Icons.Filled.Air,
                        label = "Presión de Aire",
                        value = "25 PSI adelante · 29 PSI atrás",
                        note = "33 PSI atrás con acompañante"
                    )
                    HorizontalDivider(color = EditorialDivider)

                    SpaciousSpecRow(
                        icon = Icons.Filled.Settings,
                        label = "Cadena de Tracción",
                        value = "Paso 428",
                        note = "Holgura: 15 a 25 mm"
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // 3. FICHA TÉCNICA OFICIAL (Datos Tránsito)
            Text(
                text = "DATOS DE TRÁNSITO & REGISTRO",
                color = EditorialPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = moto?.transitAuthority?.takeIf { it.isNotBlank() } ?: "Organismo de Tránsito y Transporte",
                color = EditorialTextSecondary,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = EditorialSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SpaciousSpecRow(
                        icon = Icons.Filled.Speed,
                        label = "Cilindraje y Potencia",
                        value = "125 cc · 11 HP",
                        note = "Motor 4 tiempos OHC"
                    )
                    HorizontalDivider(color = EditorialDivider)

                    SpaciousSpecRow(
                        icon = Icons.Filled.Settings,
                        label = "Número de Motor",
                        value = moto?.engineNumber?.takeIf { it.isNotBlank() } ?: "Por registrar",
                        note = "Grabado en cárter"
                    )
                    HorizontalDivider(color = EditorialDivider)

                    SpaciousSpecRow(
                        icon = Icons.Filled.ConfirmationNumber,
                        label = "VIN / Chasis",
                        value = moto?.vinNumber?.takeIf { it.isNotBlank() } ?: "Por registrar",
                        note = "Estructura principal"
                    )
                    HorizontalDivider(color = EditorialDivider)

                    SpaciousSpecRow(
                        icon = Icons.Filled.LocalGasStation,
                        label = "Capacidad del Tanque",
                        value = "${moto?.fuelTankCapacityLiters ?: 13.0} Litros",
                        note = "Gasolina corriente"
                    )
                    HorizontalDivider(color = EditorialDivider)

                    SpaciousSpecRow(
                        icon = Icons.Filled.Person,
                        label = "Propietario Registrado",
                        value = moto?.ownerName?.takeIf { it.isNotBlank() } ?: "Por registrar",
                        note = if (!moto?.ownerId.isNullOrBlank()) "C.C. ${moto?.ownerId}" else "Identificación pendiente"
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // 4. MANUALES TÉCNICOS HONDA EN PDF
            Text(
                text = "MANUALES OFICIALES HONDA",
                color = EditorialPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Toca cualquier manual para abrir el PDF",
                color = EditorialTextSecondary,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                manuals.forEach { manual ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.openManualPdf(manual) },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = EditorialSurface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
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
                                        .size(46.dp)
                                        .clip(CircleShape)
                                    .background(EditorialContainerPill),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.MenuBook,
                                        contentDescription = null,
                                        tint = EditorialPrimary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                    Text(
                                        text = manual.title,
                                        color = EditorialTextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = "${manual.category} · PDF Oficial",
                                        color = EditorialTextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
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

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
}

@Composable
private fun SpaciousSpecRow(
    icon: ImageVector,
    label: String,
    value: String,
    note: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(EditorialContainerPill),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = EditorialPrimary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = label,
                color = EditorialTextTertiary,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp
            )
            Text(
                text = value,
                color = EditorialTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = note,
                color = EditorialTextSecondary,
                fontSize = 12.sp
            )
        }
    }
}
