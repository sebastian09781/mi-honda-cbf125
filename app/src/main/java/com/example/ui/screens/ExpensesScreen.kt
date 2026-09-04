package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.FuelRecord
import com.example.ui.components.EditorialBadge
import com.example.ui.components.MotoBackdropBackground
import com.example.ui.components.MotoBottomNavigationBar
import com.example.ui.components.MotoTopHeaderBar
import com.example.ui.theme.EditorialBackground
import com.example.ui.theme.EditorialBorder
import com.example.ui.theme.EditorialContainerPill
import com.example.ui.theme.EditorialDivider
import com.example.ui.theme.EditorialPrimary
import com.example.ui.theme.EditorialPurpleAccent
import com.example.ui.theme.EditorialSurface
import com.example.ui.theme.EditorialSurfaceSubtle
import com.example.ui.theme.EditorialTextPrimary
import com.example.ui.theme.EditorialTextSecondary
import com.example.ui.theme.EditorialTextTertiary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MotoViewModel
import java.text.DecimalFormat

@Composable
fun ExpensesScreen(
    viewModel: MotoViewModel,
    modifier: Modifier = Modifier
) {
    val fuelRecords by viewModel.fuelRecords.collectAsState()
    val expenses by viewModel.expenses.collectAsState()
    val expensesPeriod by viewModel.expensesPeriod.collectAsState()
    val expensesSubTab by viewModel.expensesSubTab.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()

    val totalFuelCost = fuelRecords.sumOf { it.totalCost }
    val totalOtherExpenses = expenses.sumOf { it.amount }
    val totalAmount = totalFuelCost + totalOtherExpenses
    val totalLiters = fuelRecords.sumOf { it.liters }
    val kmFormatter = DecimalFormat("#,###")

    val periods = listOf("Mes", "Año", "Total")

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("expenses_screen"),
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
            drawableRes = com.example.R.drawable.moto_02_trasera_derecha
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 22.dp, vertical = 12.dp)
            ) {
                // Top Header Bar with Settings
                MotoTopHeaderBar(
                    title = "Gastos y Gasolina",
                    showBackButton = false,
                    showSettings = true,
                    onSettingsClick = { viewModel.navigateTo(AppScreen.CONFIGURACION) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // SUB-PESTAÑAS PRINCIPALES: [ Gastos ] | [ Combustible ]
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
                                .background(if (expensesSubTab == 0) EditorialPrimary else Color.Transparent)
                                .clickable { viewModel.setExpensesSubTab(0) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Gastos",
                                color = if (expensesSubTab == 0) Color(0xFF0F1704) else EditorialTextSecondary,
                                fontWeight = if (expensesSubTab == 0) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (expensesSubTab == 1) EditorialPrimary else Color.Transparent)
                                .clickable { viewModel.setExpensesSubTab(1) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Combustible",
                                color = if (expensesSubTab == 1) Color(0xFF0F1704) else EditorialTextSecondary,
                                fontWeight = if (expensesSubTab == 1) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (expensesSubTab == 0) {
                    // SUB-PESTAÑA 0: GASTOS GENERALES
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Selector de período
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            periods.forEach { p ->
                                val isSelected = expensesPeriod == p
                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(14.dp))
                                        .clickable { viewModel.setExpensesPeriod(p) },
                                    color = if (isSelected) EditorialContainerPill else EditorialSurfaceSubtle,
                                    border = BorderStroke(1.dp, if (isSelected) EditorialPrimary else EditorialBorder),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Text(
                                        text = p,
                                        color = if (isSelected) EditorialPrimary else EditorialTextSecondary,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }

                        // Botón para registrar gasto
                        Button(
                            onClick = { viewModel.showAddExpenseDialog.value = true },
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
                                text = "Nuevo Gasto",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = EditorialPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Tarjeta de Gran Total
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = EditorialSurface),
                        border = BorderStroke(1.dp, EditorialBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "GASTO TOTAL ($expensesPeriod)".uppercase(),
                                color = EditorialPurpleAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.1.sp
                            )
                            Text(
                                text = viewModel.formatCurrency(totalAmount),
                                color = EditorialTextPrimary,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = (-0.5).sp
                            )
                            Text(
                                text = "Gasolina + Mantenimientos + Repuestos + Seguros",
                                color = EditorialTextTertiary,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "HISTORIAL DE GASTOS REGISTRADOS",
                        color = EditorialPurpleAccent,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 1.1.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (expenses.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No hay gastos registrados en este período",
                                color = EditorialTextSecondary,
                                fontSize = 13.sp
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(expenses) { exp ->
                                val icon = when {
                                    exp.category.contains("Mantenimiento", ignoreCase = true) -> Icons.Filled.Build
                                    exp.category.contains("Repuesto", ignoreCase = true) -> Icons.Filled.Settings
                                    exp.category.contains("SOAT", ignoreCase = true) || exp.category.contains("Seguro", ignoreCase = true) -> Icons.Filled.Shield
                                    exp.category.contains("Parqueadero", ignoreCase = true) -> Icons.Filled.DirectionsCar
                                    else -> Icons.Filled.ReceiptLong
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
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .clip(RoundedCornerShape(10.dp))
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

                                            Spacer(modifier = Modifier.width(12.dp))

                                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                                Text(
                                                    text = exp.title,
                                                    color = EditorialTextPrimary,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 14.sp
                                                )
                                                Text(
                                                    text = "${exp.category} · ${exp.dateFormatted}",
                                                    color = EditorialTextTertiary,
                                                    fontSize = 12.sp
                                                )
                                            }
                                        }

                                        Text(
                                            text = viewModel.formatCurrency(exp.amount),
                                            color = EditorialPrimary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // SUB-PESTAÑA 1: COMBUSTIBLE / TANQUEOS
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CONTROL DE GASOLINA",
                            color = EditorialPurpleAccent,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 1.1.sp
                        )

                        Button(
                            onClick = { viewModel.showAddFuelDialog.value = true },
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
                                text = "Tanqueo",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = EditorialPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Tarjeta Resumen Combustible
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = EditorialSurface),
                        border = BorderStroke(1.dp, EditorialBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "GASOLINA TOTAL",
                                    color = EditorialPurpleAccent,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.1.sp
                                )
                                Text(
                                    text = viewModel.formatCurrency(totalFuelCost),
                                    color = EditorialTextPrimary,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "LITROS CARGADOS",
                                    color = EditorialPurpleAccent,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.1.sp
                                )
                                Text(
                                    text = "${String.format("%.1f", totalLiters)} L",
                                    color = EditorialPrimary,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "HISTORIAL DE TANQUEOS",
                        color = EditorialPurpleAccent,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 1.1.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (fuelRecords.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Sin tanqueos registrados aún",
                                color = EditorialTextSecondary,
                                fontSize = 13.sp
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(fuelRecords) { fuel ->
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
                                            val stationDrawable = when {
                                                fuel.notes.contains("terpel", ignoreCase = true) -> R.drawable.bomba_terpel
                                                fuel.notes.contains("primax", ignoreCase = true) -> R.drawable.bomba_primax
                                                fuel.notes.contains("texaco", ignoreCase = true) -> R.drawable.bomba_texaco
                                                fuel.notes.contains("biomax", ignoreCase = true) -> R.drawable.bomba_biomax
                                                fuel.notes.contains("mobil", ignoreCase = true) -> R.drawable.bomba_mobil
                                                else -> null
                                            }

                                            if (stationDrawable != null) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(46.dp)
                                                        .clip(RoundedCornerShape(12.dp))
                                                        .background(com.example.ui.theme.EditorialSurfaceSubtle)
                                                        .border(1.dp, com.example.ui.theme.EditorialBorder, RoundedCornerShape(12.dp))
                                                        .padding(horizontal = 4.dp, vertical = 6.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Image(
                                                        painter = painterResource(id = stationDrawable),
                                                        contentDescription = "Estación de servicio",
                                                        modifier = Modifier.fillMaxSize(),
                                                        contentScale = ContentScale.Fit
                                                    )
                                                }
                                            } else {
                                                Box(
                                                    modifier = Modifier
                                                        .size(42.dp)
                                                        .clip(RoundedCornerShape(10.dp))
                                                        .background(EditorialContainerPill),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Filled.LocalGasStation,
                                                        contentDescription = null,
                                                        tint = EditorialPrimary,
                                                        modifier = Modifier.size(22.dp)
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.width(12.dp))

                                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                                Text(
                                                    text = "${fuel.liters} Litros · ${kmFormatter.format(fuel.mileageKm).replace(",", ".")} km",
                                                    color = EditorialTextPrimary,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 14.sp
                                                )
                                                Text(
                                                    text = "${fuel.dateDay} ${fuel.dateMonth} ${fuel.dateYear} ${if (fuel.notes.isNotBlank()) "· ${fuel.notes}" else ""}",
                                                    color = EditorialTextTertiary,
                                                    fontSize = 12.sp
                                                )
                                            }
                                        }

                                        Text(
                                            text = viewModel.formatCurrency(fuel.totalCost),
                                            color = EditorialPrimary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
