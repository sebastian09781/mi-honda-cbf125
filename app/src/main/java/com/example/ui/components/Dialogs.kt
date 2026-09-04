package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.BorderStroke
import com.example.R
import com.example.data.model.MaintenanceSchedule
import com.example.data.model.Motorcycle
import com.example.ui.theme.EditorialBackground
import com.example.ui.theme.EditorialBorder
import com.example.ui.theme.EditorialContainer
import com.example.ui.theme.EditorialContainerPill
import com.example.ui.theme.EditorialDivider
import com.example.ui.theme.EditorialHighlightRose
import com.example.ui.theme.EditorialHighlightRoseText
import com.example.ui.theme.EditorialPrimary
import com.example.ui.theme.EditorialPurpleAccent
import com.example.ui.theme.EditorialSuccess
import com.example.ui.theme.EditorialSurface
import com.example.ui.theme.EditorialSurfaceSubtle
import com.example.ui.theme.EditorialTextPrimary
import com.example.ui.theme.EditorialTextSecondary
import com.example.ui.theme.EditorialTextTertiary

@Composable
fun UpdateMileageDialog(
    currentKm: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var kmText by remember { mutableStateOf(currentKm.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = EditorialSurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(EditorialContainerPill),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Speed,
                        contentDescription = null,
                        tint = EditorialPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Registrar Kilometraje",
                    color = EditorialTextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        },
        text = {
            Column {
                Text(
                    text = "Ingresa el kilometraje actual del odómetro de tu Honda CBF 125:",
                    color = EditorialTextSecondary,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = kmText,
                    onValueChange = { kmText = it.filter { char -> char.isDigit() } },
                    label = { Text("Kilometraje (km)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("mileage_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialPrimary,
                        unfocusedBorderColor = EditorialBorder,
                        focusedTextColor = EditorialTextPrimary,
                        unfocusedTextColor = EditorialTextPrimary,
                        focusedLabelColor = EditorialPrimary,
                        unfocusedLabelColor = EditorialTextSecondary
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val km = kmText.toIntOrNull() ?: currentKm
                    onConfirm(km)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = EditorialPrimary,
                    contentColor = Color(0xFF0F1704)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("confirm_mileage_button")
            ) {
                Text("Guardar", fontWeight = FontWeight.Bold, color = Color(0xFF0F1704))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = EditorialTextSecondary)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterMaintenanceDoneDialog(
    schedules: List<MaintenanceSchedule>,
    currentKm: Int,
    onDismiss: () -> Unit,
    onConfirm: (scheduleId: Long, title: String, category: String, cost: Double, workshop: String, currentKm: Int, intervalKm: Int, notes: String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedSchedule by remember { mutableStateOf(schedules.firstOrNull()) }
    var customTitle by remember { mutableStateOf(selectedSchedule?.title ?: "Cambio de aceite") }
    var workshop by remember { mutableStateOf("MotoServicio") }
    var costText by remember { mutableStateOf("60000") }
    var notes by remember { mutableStateOf("") }
    var kmText by remember { mutableStateOf(currentKm.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = EditorialSurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(EditorialContainerPill),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Build,
                        contentDescription = null,
                        tint = EditorialPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Registrar Mantenimiento",
                    color = EditorialTextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Selecciona el servicio realizado:",
                    color = EditorialTextSecondary,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedSchedule?.title ?: customTitle,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EditorialPrimary,
                            unfocusedBorderColor = EditorialBorder,
                            focusedTextColor = EditorialTextPrimary,
                            unfocusedTextColor = EditorialTextPrimary
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(EditorialSurfaceSubtle)
                    ) {
                        schedules.forEach { schedule ->
                            DropdownMenuItem(
                                text = { Text(schedule.title, color = EditorialTextPrimary) },
                                onClick = {
                                    selectedSchedule = schedule
                                    customTitle = schedule.title
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = kmText,
                    onValueChange = { kmText = it },
                    label = { Text("Kilometraje al realizarlo") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialPrimary,
                        unfocusedBorderColor = EditorialBorder,
                        focusedTextColor = EditorialTextPrimary,
                        unfocusedTextColor = EditorialTextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = workshop,
                    onValueChange = { workshop = it },
                    label = { Text("Taller / Lugar") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialPrimary,
                        unfocusedBorderColor = EditorialBorder,
                        focusedTextColor = EditorialTextPrimary,
                        unfocusedTextColor = EditorialTextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = costText,
                    onValueChange = { costText = it },
                    label = { Text("Costo ($ COP)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialPrimary,
                        unfocusedBorderColor = EditorialBorder,
                        focusedTextColor = EditorialTextPrimary,
                        unfocusedTextColor = EditorialTextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notas / Repuestos (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialPrimary,
                        unfocusedBorderColor = EditorialBorder,
                        focusedTextColor = EditorialTextPrimary,
                        unfocusedTextColor = EditorialTextPrimary
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val sched = selectedSchedule
                    val cost = costText.toDoubleOrNull() ?: 0.0
                    val km = kmText.toIntOrNull() ?: currentKm
                    val schedId = sched?.id ?: 1L
                    val cat = sched?.category ?: "Mantenimiento"
                    val interval = sched?.intervalKm ?: 2000
                    onConfirm(schedId, customTitle, cat, cost, workshop, km, interval, notes)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = EditorialPrimary,
                    contentColor = Color(0xFF0F1704)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("confirm_maintenance_button")
            ) {
                Text("Guardar Servicio", fontWeight = FontWeight.Bold, color = Color(0xFF0F1704))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = EditorialTextSecondary)
            }
        }
    )
}

@Composable
fun AddFuelDialog(
    currentKm: Int,
    onDismiss: () -> Unit,
    onConfirm: (liters: Double, totalCost: Double, mileageKm: Int, notes: String) -> Unit
) {
    var litersText by remember { mutableStateOf("8.5") }
    var costText by remember { mutableStateOf("32000") }
    var kmText by remember { mutableStateOf(currentKm.toString()) }
    var notes by remember { mutableStateOf("Terpel - Gasolina corriente") }

    val colombiaStations = remember {
        listOf(
            Pair("Terpel", R.drawable.bomba_terpel),
            Pair("Primax", R.drawable.bomba_primax),
            Pair("Texaco", R.drawable.bomba_texaco),
            Pair("Biomax", R.drawable.bomba_biomax),
            Pair("Mobil", R.drawable.bomba_mobil)
        )
    }
    var selectedStation by remember { mutableStateOf<String?>("Terpel") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = EditorialSurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(EditorialContainerPill),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocalGasStation,
                        contentDescription = null,
                        tint = EditorialPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Registrar Tanqueo",
                    color = EditorialTextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Estación de servicio (Colombia):",
                    color = EditorialTextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Selector horizontal de bombas
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    colombiaStations.forEach { (name, drawableRes) ->
                        val isSelected = selectedStation == name
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) EditorialContainerPill else EditorialSurfaceSubtle,
                            border = BorderStroke(
                                1.5.dp,
                                if (isSelected) EditorialPrimary else EditorialBorder
                            ),
                            modifier = Modifier.clickable {
                                selectedStation = if (isSelected) null else name
                                val extra = notes.substringAfter(" - ", "")
                                notes = if (selectedStation != null) {
                                    if (extra.isNotBlank()) "$name - $extra" else "$name - Gasolina corriente"
                                } else {
                                    if (extra.isNotBlank()) extra else "Gasolina corriente"
                                }
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = drawableRes),
                                    contentDescription = name,
                                    modifier = Modifier.height(20.dp),
                                    contentScale = ContentScale.Fit
                                )
                                Text(
                                    text = name,
                                    color = if (isSelected) EditorialPrimary else EditorialTextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = litersText,
                    onValueChange = { litersText = it },
                    label = { Text("Litros / Galones") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialPrimary,
                        unfocusedBorderColor = EditorialBorder,
                        focusedTextColor = EditorialTextPrimary,
                        unfocusedTextColor = EditorialTextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = costText,
                    onValueChange = { costText = it },
                    label = { Text("Costo total ($ COP)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialPrimary,
                        unfocusedBorderColor = EditorialBorder,
                        focusedTextColor = EditorialTextPrimary,
                        unfocusedTextColor = EditorialTextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = kmText,
                    onValueChange = { kmText = it },
                    label = { Text("Kilometraje en odómetro") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialPrimary,
                        unfocusedBorderColor = EditorialBorder,
                        focusedTextColor = EditorialTextPrimary,
                        unfocusedTextColor = EditorialTextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Estación / Notas") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialPrimary,
                        unfocusedBorderColor = EditorialBorder,
                        focusedTextColor = EditorialTextPrimary,
                        unfocusedTextColor = EditorialTextPrimary
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val liters = litersText.toDoubleOrNull() ?: 8.5
                    val cost = costText.toDoubleOrNull() ?: 32000.0
                    val km = kmText.toIntOrNull() ?: currentKm
                    onConfirm(liters, cost, km, notes)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = EditorialPrimary,
                    contentColor = Color(0xFF0F1704)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Registrar Tanqueo", fontWeight = FontWeight.Bold, color = Color(0xFF0F1704))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = EditorialTextSecondary)
            }
        }
    )
}

@Composable
fun AddHistoryRecordDialog(
    currentKm: Int,
    onDismiss: () -> Unit,
    onConfirm: (title: String, category: String, day: String, month: String, year: String, mileageKm: Int, workshop: String, cost: Double, notes: String, receiptPhotoUri: String?) -> Unit
) {
    var title by remember { mutableStateOf("Cambio de pastillas de freno") }
    var category by remember { mutableStateOf("Mantenimientos") }
    var day by remember { mutableStateOf("15") }
    var month by remember { mutableStateOf("OCT") }
    var year by remember { mutableStateOf("2024") }
    var kmText by remember { mutableStateOf(currentKm.toString()) }
    var workshop by remember { mutableStateOf("MotoServicio") }
    var costText by remember { mutableStateOf("45000") }
    var notes by remember { mutableStateOf("") }
    var receiptUri by remember { mutableStateOf<String?>(null) }

    val photoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        receiptUri = uri?.toString()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = EditorialSurface,
        title = {
            Text(
                text = "Agregar Registro de Historial",
                color = EditorialTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título / Servicio") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialPrimary,
                        unfocusedBorderColor = EditorialBorder,
                        focusedTextColor = EditorialTextPrimary,
                        unfocusedTextColor = EditorialTextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Categoría (Mantenimientos / Repuestos / Otro)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialPrimary,
                        unfocusedBorderColor = EditorialBorder,
                        focusedTextColor = EditorialTextPrimary,
                        unfocusedTextColor = EditorialTextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = day,
                        onValueChange = { day = it },
                        label = { Text("Día") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EditorialPrimary,
                            unfocusedBorderColor = EditorialBorder,
                            focusedTextColor = EditorialTextPrimary,
                            unfocusedTextColor = EditorialTextPrimary
                        )
                    )
                    OutlinedTextField(
                        value = month,
                        onValueChange = { month = it },
                        label = { Text("Mes") },
                        modifier = Modifier.weight(1.5f),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EditorialPrimary,
                            unfocusedBorderColor = EditorialBorder,
                            focusedTextColor = EditorialTextPrimary,
                            unfocusedTextColor = EditorialTextPrimary
                        )
                    )
                    OutlinedTextField(
                        value = year,
                        onValueChange = { year = it },
                        label = { Text("Año") },
                        modifier = Modifier.weight(1.5f),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EditorialPrimary,
                            unfocusedBorderColor = EditorialBorder,
                            focusedTextColor = EditorialTextPrimary,
                            unfocusedTextColor = EditorialTextPrimary
                        )
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = kmText,
                    onValueChange = { kmText = it },
                    label = { Text("Kilometraje (km)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialPrimary,
                        unfocusedBorderColor = EditorialBorder,
                        focusedTextColor = EditorialTextPrimary,
                        unfocusedTextColor = EditorialTextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = workshop,
                    onValueChange = { workshop = it },
                    label = { Text("Taller") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialPrimary,
                        unfocusedBorderColor = EditorialBorder,
                        focusedTextColor = EditorialTextPrimary,
                        unfocusedTextColor = EditorialTextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = costText,
                    onValueChange = { costText = it },
                    label = { Text("Costo ($ COP)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialPrimary,
                        unfocusedBorderColor = EditorialBorder,
                        focusedTextColor = EditorialTextPrimary,
                        unfocusedTextColor = EditorialTextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(
                    onClick = { photoLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, if (receiptUri != null) EditorialSuccess else EditorialBorder)
                ) {
                    Icon(
                        imageVector = Icons.Filled.UploadFile,
                        contentDescription = null,
                        tint = if (receiptUri != null) EditorialSuccess else EditorialPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (receiptUri != null) "Factura Adjuntada ✓" else "Adjuntar Foto Factura / Garantía",
                        color = if (receiptUri != null) EditorialSuccess else EditorialTextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val cost = costText.toDoubleOrNull() ?: 0.0
                    val km = kmText.toIntOrNull() ?: currentKm
                    onConfirm(title, category, day, month, year, km, workshop, cost, notes, receiptUri)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = EditorialPrimary,
                    contentColor = Color(0xFF0F1704)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Guardar Registro", fontWeight = FontWeight.Bold, color = Color(0xFF0F1704))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = EditorialTextSecondary)
            }
        }
    )
}

@Composable
fun AddExpenseDialog(
    onDismiss: () -> Unit,
    onConfirm: (category: String, title: String, amount: Double, notes: String) -> Unit
) {
    var category by remember { mutableStateOf("Mantenimiento") }
    var title by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = EditorialSurface,
        title = {
            Text(
                text = "Registrar Gasto",
                color = EditorialTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Concepto") },
                    placeholder = { Text("Ej. Cambio de bujía, Lavado, etc.") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialPrimary,
                        unfocusedBorderColor = EditorialBorder,
                        focusedTextColor = EditorialTextPrimary,
                        unfocusedTextColor = EditorialTextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Categoría (Combustible/Mantenimiento/Repuestos/etc.)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialPrimary,
                        unfocusedBorderColor = EditorialBorder,
                        focusedTextColor = EditorialTextPrimary,
                        unfocusedTextColor = EditorialTextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Monto ($ COP)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialPrimary,
                        unfocusedBorderColor = EditorialBorder,
                        focusedTextColor = EditorialTextPrimary,
                        unfocusedTextColor = EditorialTextPrimary
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull() ?: 0.0
                    onConfirm(category, title.ifBlank { "Gasto de Moto" }, amount, notes)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = EditorialPrimary,
                    contentColor = Color(0xFF0F1704)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Guardar Gasto", fontWeight = FontWeight.Bold, color = Color(0xFF0F1704))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = EditorialTextSecondary)
            }
        }
    )
}

@Composable
fun EditMotoProfileDialog(
    motorcycle: Motorcycle?,
    onDismiss: () -> Unit,
    onConfirm: (Motorcycle) -> Unit
) {
    val moto = motorcycle ?: Motorcycle()
    var brand by remember { mutableStateOf(moto.brand) }
    var model by remember { mutableStateOf(moto.model) }
    var color by remember { mutableStateOf(moto.color) }
    var yearText by remember { mutableStateOf(moto.year.toString()) }
    var displacement by remember { mutableStateOf(moto.displacement) }
    var licensePlate by remember { mutableStateOf(moto.licensePlate) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = EditorialSurface,
        title = {
            Text(
                text = "Editar Perfil de Moto",
                color = EditorialTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = brand,
                    onValueChange = { brand = it },
                    label = { Text("Marca") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialPrimary,
                        unfocusedBorderColor = EditorialBorder,
                        focusedTextColor = EditorialTextPrimary,
                        unfocusedTextColor = EditorialTextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = model,
                    onValueChange = { model = it },
                    label = { Text("Modelo") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialPrimary,
                        unfocusedBorderColor = EditorialBorder,
                        focusedTextColor = EditorialTextPrimary,
                        unfocusedTextColor = EditorialTextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = color,
                    onValueChange = { color = it },
                    label = { Text("Color") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialPrimary,
                        unfocusedBorderColor = EditorialBorder,
                        focusedTextColor = EditorialTextPrimary,
                        unfocusedTextColor = EditorialTextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = licensePlate,
                    onValueChange = { licensePlate = it },
                    label = { Text("Placa") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialPrimary,
                        unfocusedBorderColor = EditorialBorder,
                        focusedTextColor = EditorialTextPrimary,
                        unfocusedTextColor = EditorialTextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = yearText,
                    onValueChange = { yearText = it },
                    label = { Text("Año") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialPrimary,
                        unfocusedBorderColor = EditorialBorder,
                        focusedTextColor = EditorialTextPrimary,
                        unfocusedTextColor = EditorialTextPrimary
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updated = moto.copy(
                        brand = brand,
                        model = model,
                        color = color,
                        licensePlate = licensePlate,
                        year = yearText.toIntOrNull() ?: moto.year,
                        displacement = displacement
                    )
                    onConfirm(updated)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = EditorialPrimary,
                    contentColor = Color(0xFF0F1704)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Guardar Cambios", fontWeight = FontWeight.Bold, color = Color(0xFF0F1704))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = EditorialTextSecondary)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDocumentDialog(
    document: com.example.data.model.LegalDocumentItem,
    onDismiss: () -> Unit,
    onSave: (title: String, status: String, validityDate: String, referenceNumber: String, newUri: Uri?) -> Unit,
    onResetToDefault: (() -> Unit)? = null
) {
    var title by remember { mutableStateOf(document.title) }
    var status by remember { mutableStateOf(document.status) }
    var validityDate by remember { mutableStateOf(document.validityDate) }
    var referenceNumber by remember { mutableStateOf(document.referenceNumber) }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFileName by remember { mutableStateOf<String?>(null) }

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedUri = uri
            selectedFileName = uri.lastPathSegment ?: "nuevo_documento.pdf"
        }
    }

    val statusOptions = listOf("Vigente", "Al día", "Por Vencer", "Vencido", "En trámite", "Digital")
    var statusExpanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 640.dp),
            shape = RoundedCornerShape(20.dp),
            color = EditorialSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, EditorialBorder),
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ACTUALIZAR DOCUMENTO",
                            color = EditorialPurpleAccent,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = document.title,
                            color = EditorialTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Cerrar",
                            tint = EditorialTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // File Attachment / Replacement Block
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = EditorialSurfaceSubtle,
                    border = androidx.compose.foundation.BorderStroke(1.dp, EditorialBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "ARCHIVO PDF DEL DOCUMENTO",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = EditorialPurpleAccent,
                            letterSpacing = 1.1.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Description,
                                    contentDescription = null,
                                    tint = EditorialPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = selectedFileName ?: document.fileName,
                                        color = EditorialTextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = if (selectedUri != null) "Nuevo archivo seleccionado" else if (document.isCustom) "Archivo personalizado" else "Archivo original predeterminado",
                                        color = if (selectedUri != null) EditorialPrimary else EditorialTextTertiary,
                                        fontSize = 10.sp
                                    )
                                }
                            }

                            Button(
                                onClick = { filePicker.launch("application/pdf") },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = EditorialPrimary,
                                    contentColor = Color(0xFF0F1704)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Icon(imageVector = Icons.Filled.CloudUpload, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFF0F1704))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Cambiar", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F1704))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Nombre del Documento") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialPrimary,
                        unfocusedBorderColor = EditorialBorder,
                        focusedContainerColor = EditorialSurface,
                        unfocusedContainerColor = EditorialSurface
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                ExposedDropdownMenuBox(
                    expanded = statusExpanded,
                    onExpandedChange = { statusExpanded = !statusExpanded }
                ) {
                    OutlinedTextField(
                        value = status,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Estado del Documento") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EditorialPrimary,
                            unfocusedBorderColor = EditorialBorder,
                            focusedContainerColor = EditorialSurface,
                            unfocusedContainerColor = EditorialSurface
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = statusExpanded,
                        onDismissRequest = { statusExpanded = false }
                    ) {
                        statusOptions.forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt) },
                                onClick = {
                                    status = opt
                                    statusExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = validityDate,
                    onValueChange = { validityDate = it },
                    label = { Text("Fecha de Vencimiento / Vigencia") },
                    placeholder = { Text("Ej: 15/12/2026 o Indefinida") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialPrimary,
                        unfocusedBorderColor = EditorialBorder,
                        focusedContainerColor = EditorialSurface,
                        unfocusedContainerColor = EditorialSurface
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = referenceNumber,
                    onValueChange = { referenceNumber = it },
                    label = { Text("Número de Referencia / Póliza") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialPrimary,
                        unfocusedBorderColor = EditorialBorder,
                        focusedContainerColor = EditorialSurface,
                        unfocusedContainerColor = EditorialSurface
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (document.isCustom && onResetToDefault != null) {
                        OutlinedButton(
                            onClick = {
                                onResetToDefault()
                                onDismiss()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Restaurar Original", fontSize = 11.sp)
                        }
                    }

                    Button(
                        onClick = {
                            onSave(title, status, validityDate, referenceNumber, selectedUri)
                            onDismiss()
                        },
                        modifier = Modifier.weight(1.2f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EditorialPrimary,
                            contentColor = Color(0xFF0F1704)
                        )
                    ) {
                        Icon(imageVector = Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF0F1704))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Guardar Cambios", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF0F1704))
                    }
                }
            }
        }
    }
}

@Composable
fun EditManualDialog(
    manual: com.example.data.model.HondaManualItem,
    onDismiss: () -> Unit,
    onSave: (title: String, newUri: Uri?) -> Unit,
    onResetToDefault: (() -> Unit)? = null
) {
    var title by remember { mutableStateOf(manual.title) }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFileName by remember { mutableStateOf<String?>(null) }

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedUri = uri
            selectedFileName = uri.lastPathSegment ?: "nuevo_manual.pdf"
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 520.dp),
            shape = RoundedCornerShape(20.dp),
            color = EditorialSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, EditorialBorder),
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ACTUALIZAR MANUAL",
                            color = EditorialPurpleAccent,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = manual.title,
                            color = EditorialTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Cerrar",
                            tint = EditorialTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // File Attachment / Replacement Block
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = EditorialSurfaceSubtle,
                    border = androidx.compose.foundation.BorderStroke(1.dp, EditorialBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "ARCHIVO PDF DEL MANUAL",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = EditorialPurpleAccent,
                            letterSpacing = 1.1.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Description,
                                    contentDescription = null,
                                    tint = EditorialPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = selectedFileName ?: manual.fileName,
                                        color = EditorialTextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = if (selectedUri != null) "Nuevo archivo seleccionado" else if (manual.isCustom) "Archivo personalizado" else "Archivo original de fábrica",
                                        color = if (selectedUri != null) EditorialPrimary else EditorialTextTertiary,
                                        fontSize = 10.sp
                                    )
                                }
                            }

                            Button(
                                onClick = { filePicker.launch("application/pdf") },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = EditorialPrimary,
                                    contentColor = Color(0xFF0F1704)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Icon(imageVector = Icons.Filled.CloudUpload, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFF0F1704))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Cambiar", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F1704))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título del Manual") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialPrimary,
                        unfocusedBorderColor = EditorialBorder,
                        focusedContainerColor = EditorialSurface,
                        unfocusedContainerColor = EditorialSurface
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (manual.isCustom && onResetToDefault != null) {
                        OutlinedButton(
                            onClick = {
                                onResetToDefault()
                                onDismiss()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Restaurar Original", fontSize = 11.sp)
                        }
                    }

                    Button(
                        onClick = {
                            onSave(title, selectedUri)
                            onDismiss()
                        },
                        modifier = Modifier.weight(1.2f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EditorialPrimary,
                            contentColor = Color(0xFF0F1704)
                        )
                    ) {
                        Icon(imageVector = Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF0F1704))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Guardar Cambios", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF0F1704))
                    }
                }
            }
        }
    }
}

