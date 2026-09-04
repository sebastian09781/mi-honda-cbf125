package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ElectricBike
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.TwoWheeler
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@Composable
fun MotoBottomNavigationBar(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .testTag("bottom_nav_bar"),
        containerColor = EditorialSurfaceSubtle,
        tonalElevation = 3.dp
    ) {
        val items = listOf(
            Triple(AppScreen.INICIO, "Inicio", Pair(Icons.Filled.Home, Icons.Outlined.Home)),
            Triple(AppScreen.MI_MOTO, "Mi Moto", Pair(Icons.Filled.TwoWheeler, Icons.Outlined.TwoWheeler)),
            Triple(AppScreen.SERVICIOS, "Servicios", Pair(Icons.Filled.Build, Icons.Outlined.Build)),
            Triple(AppScreen.GASTOS, "Gastos", Pair(Icons.Filled.LocalGasStation, Icons.Outlined.LocalGasStation)),
            Triple(AppScreen.PAPELES, "Papeles", Pair(Icons.Filled.Badge, Icons.Outlined.Badge))
        )

        items.forEach { (screen, label, iconPair) ->
            val selected = currentScreen == screen
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(screen) },
                icon = {
                    Icon(
                        imageVector = if (selected) iconPair.first else iconPair.second,
                        contentDescription = label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                        letterSpacing = 0.2.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = EditorialPrimary,
                    selectedTextColor = EditorialPrimary,
                    unselectedIconColor = EditorialTextSecondary,
                    unselectedTextColor = EditorialTextSecondary,
                    indicatorColor = EditorialContainerPill
                )
            )
        }
    }
}

@Composable
fun StatusBadge(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = EditorialSuccessContainer,
    textColor: Color = EditorialSuccess
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            letterSpacing = 0.3.sp
        )
    }
}

@Composable
fun EditorialBadge(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = EditorialHighlightRose,
    textColor: Color = EditorialHighlightRoseText
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor
    ) {
        Text(
            text = text.uppercase(),
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun MaintenanceIconBox(
    iconKey: String,
    modifier: Modifier = Modifier
) {
    val icon = when (iconKey) {
        "oil" -> Icons.Filled.Opacity
        "chain" -> Icons.Filled.Settings
        "tire" -> Icons.Filled.Speed
        "brake" -> Icons.Filled.Build
        "spark" -> Icons.Filled.ElectricBike
        else -> Icons.Filled.Build
    }

    Box(
        modifier = modifier
            .size(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(EditorialContainerPill)
            .border(1.dp, EditorialBorder, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = EditorialPrimary,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
fun MotoBackdropBackground(
    modifier: Modifier = Modifier,
    alpha: Float = 0.08f,
    drawableRes: Int = com.example.R.drawable.moto_06_lateral_derecha,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize().background(EditorialBackground)) {
        // Maximized background motorcycle wallpaper across the canvas
        androidx.compose.foundation.Image(
            painter = androidx.compose.ui.res.painterResource(id = drawableRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(1.35f)
                .height(420.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-30).dp)
                .alpha(alpha),
            contentScale = androidx.compose.ui.layout.ContentScale.Fit
        )
        // Ambient smooth dark gradient overlays to blend seamlessly
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            EditorialBackground.copy(alpha = 0.65f),
                            EditorialBackground
                        ),
                        startY = 50f,
                        endY = 850f
                    )
                )
        )
        content()
    }
}

@Composable
fun MotoCenteredForegroundShowcase(
    brand: String,
    model: String,
    colorName: String,
    year: Int,
    licensePlate: String,
    currentKm: Int,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val kmFormatter = java.text.NumberFormat.getNumberInstance(java.util.Locale.GERMAN)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("moto_centered_foreground_showcase"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Motorcycle Display in Free Centered Foreground (Frontal 3/4 Protagonist)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.foundation.Image(
                painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.moto_01_frontal_derecha),
                contentDescription = "$brand $model - Frontal 3/4",
                modifier = Modifier
                    .fillMaxWidth(0.96f)
                    .height(220.dp),
                contentScale = androidx.compose.ui.layout.ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Title & Specs Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$brand $model",
                        color = EditorialTextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Modelo $year · $licensePlate",
                    color = EditorialTextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            EditorialBadge(
                text = colorName,
                backgroundColor = EditorialContainerPill,
                textColor = EditorialPrimary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Info Chips Row (Odometer & Plate)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Odometer Pill
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                color = EditorialSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, EditorialBorder),
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Speed,
                        contentDescription = null,
                        tint = EditorialPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Odómetro",
                            color = EditorialTextTertiary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${kmFormatter.format(currentKm).replace(",", ".")} km",
                            color = EditorialPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Plate Pill
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                color = EditorialSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, EditorialBorder),
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Badge,
                        contentDescription = null,
                        tint = EditorialPurpleAccent,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Placa",
                            color = EditorialTextTertiary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = licensePlate,
                            color = EditorialTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MotoHeroShowcaseCard(
    brand: String,
    model: String,
    colorName: String,
    year: Int,
    licensePlate: String,
    currentKm: Int,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
    drawableRes: Int = com.example.R.drawable.moto_09_lateral_izquierda
) {
    val kmFormatter = java.text.NumberFormat.getNumberInstance(java.util.Locale.GERMAN)
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("moto_hero_showcase_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = EditorialSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, EditorialBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Background glow and subtle ambient moto photo art
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                EditorialSurface,
                                EditorialSurfaceSubtle,
                                EditorialContainerPill.copy(alpha = 0.4f)
                            )
                        )
                    )
            )

            // Motorcycle cutout image on the right side
            androidx.compose.foundation.Image(
                painter = androidx.compose.ui.res.painterResource(id = drawableRes),
                contentDescription = "$brand $model",
                modifier = Modifier
                    .width(220.dp)
                    .height(160.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 16.dp, y = 8.dp),
                contentScale = androidx.compose.ui.layout.ContentScale.Fit
            )

            // Overlay gradient to protect text contrast on the left
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                EditorialSurface,
                                EditorialSurface.copy(alpha = 0.92f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Foreground content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EditorialBadge(
                        text = "$brand · $model".uppercase(),
                        backgroundColor = EditorialContainerPill,
                        textColor = EditorialPrimary
                    )

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = EditorialContainerPill
                    ) {
                        Text(
                            text = licensePlate,
                            color = EditorialPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "$brand $model",
                    color = EditorialTextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )

                Text(
                    text = "$colorName · Modelo $year",
                    color = EditorialTextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Stats Pills
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Odometer Pill
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = EditorialContainerPill,
                        border = androidx.compose.foundation.BorderStroke(1.dp, EditorialBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Speed,
                                contentDescription = null,
                                tint = EditorialPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${kmFormatter.format(currentKm).replace(",", ".")} km",
                                color = EditorialPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }

                    // License Plate Pill
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = EditorialSurfaceSubtle,
                        border = androidx.compose.foundation.BorderStroke(1.dp, EditorialBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Badge,
                                contentDescription = null,
                                tint = EditorialTextTertiary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = licensePlate,
                                color = EditorialTextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyStateWithMoto(
    title: String,
    description: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector = Icons.Filled.Build,
    drawableRes: Int = com.example.R.drawable.moto_01_frontal_derecha
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = EditorialSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, EditorialBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
            // Subtle watermark with specified perspective angle
            androidx.compose.foundation.Image(
                painter = androidx.compose.ui.res.painterResource(id = drawableRes),
                contentDescription = null,
                modifier = Modifier
                    .size(160.dp)
                    .align(Alignment.Center)
                    .alpha(0.07f),
                contentScale = androidx.compose.ui.layout.ContentScale.Fit
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(EditorialContainerPill),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = EditorialPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = title,
                    color = EditorialTextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = description,
                    color = EditorialTextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onButtonClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EditorialPrimary,
                        contentColor = Color(0xFF0F1704)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Color(0xFF0F1704)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = buttonText,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F1704)
                    )
                }
            }
        }
    }
}

@Composable
fun MotoTopHeaderBar(
    title: String? = null,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    showSettings: Boolean = true,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showBackButton) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(EditorialSurfaceSubtle)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Atrás",
                    tint = EditorialPrimary
                )
            }
        } else {
            Spacer(modifier = Modifier.size(42.dp))
        }

        if (title != null) {
            Text(
                text = title,
                color = EditorialTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.3).sp
            )
        } else {
            Icon(
                painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.ic_honda_logo_new),
                contentDescription = "Honda Logo",
                tint = EditorialPrimary,
                modifier = Modifier.height(46.dp)
            )
        }

        if (showSettings) {
            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(EditorialContainerPill)
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Configuración",
                    tint = EditorialPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        } else {
            Spacer(modifier = Modifier.size(42.dp))
        }
    }
}



