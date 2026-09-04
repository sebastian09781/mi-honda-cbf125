package com.example.ui.components

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EditorialBackground
import com.example.ui.theme.EditorialBorder
import com.example.ui.theme.EditorialContainerPill
import com.example.ui.theme.EditorialDivider
import com.example.ui.theme.EditorialPrimary
import com.example.ui.theme.EditorialSurface
import com.example.ui.theme.EditorialSurfaceSubtle
import com.example.ui.theme.EditorialTextPrimary
import com.example.ui.theme.EditorialTextSecondary
import com.example.ui.theme.EditorialTextTertiary
import com.example.util.CalendarHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class CalendarReminderData(
    val title: String,
    val description: String = "",
    val initialDateMillis: Long = System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000, // default 30 days
    val defaultNoticeMinutes: Int = 1440, // 1 day before
    val location: String = "CDA / Tránsito / Taller"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CalendarReminderDialog(
    data: CalendarReminderData,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    var eventTitle by remember { mutableStateOf(data.title) }
    var eventDescription by remember { mutableStateOf(data.description) }
    var eventLocation by remember { mutableStateOf(data.location) }
    var eventDateMillis by remember { mutableLongStateOf(data.initialDateMillis) }
    var selectedNoticeMinutes by remember { mutableStateOf(data.defaultNoticeMinutes) }

    val dateFormat = remember { SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy", Locale("es", "CO")) }
    val shortDateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale("es", "CO")) }

    val calendar = remember(eventDateMillis) {
        Calendar.getInstance().apply { timeInMillis = eventDateMillis }
    }

    val noticeOptions = listOf(
        Pair("El mismo día", 0),
        Pair("1 día antes", 1440),
        Pair("3 días antes", 4320),
        Pair("1 semana antes", 10080),
        Pair("2 semanas antes", 20160)
    )

    fun showNativeDatePicker() {
        val c = Calendar.getInstance().apply { timeInMillis = eventDateMillis }
        val dialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val newCal = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    set(Calendar.HOUR_OF_DAY, 9)
                    set(Calendar.MINUTE, 0)
                }
                eventDateMillis = newCal.timeInMillis
            },
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH)
        )
        dialog.show()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = EditorialSurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
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
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Agendar en Calendario",
                        color = EditorialTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                    Text(
                        text = "Google Calendar / Dispositivo",
                        color = EditorialTextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Título del evento
                OutlinedTextField(
                    value = eventTitle,
                    onValueChange = { eventTitle = it },
                    label = { Text("Título del Evento") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialPrimary,
                        unfocusedBorderColor = EditorialBorder,
                        focusedTextColor = EditorialTextPrimary,
                        unfocusedTextColor = EditorialTextPrimary,
                        focusedLabelColor = EditorialPrimary,
                        unfocusedLabelColor = EditorialTextSecondary,
                        cursorColor = EditorialPrimary
                    )
                )

                // Selector de Fecha
                Column {
                    Text(
                        text = "Fecha del evento / Vencimiento",
                        color = EditorialTextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = EditorialSurfaceSubtle,
                        border = androidx.compose.foundation.BorderStroke(1.dp, EditorialBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showNativeDatePicker() }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.CalendarToday,
                                    contentDescription = null,
                                    tint = EditorialPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = dateFormat.format(calendar.time).replaceFirstChar { it.uppercase() },
                                        color = EditorialTextPrimary,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "Toca para cambiar la fecha",
                                        color = EditorialTextTertiary,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.Filled.EditCalendar,
                                contentDescription = null,
                                tint = EditorialTextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Accesos rápidos de fecha
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        QuickDateChip(label = "+1 mes") {
                            val c = Calendar.getInstance()
                            c.add(Calendar.MONTH, 1)
                            eventDateMillis = c.timeInMillis
                        }
                        QuickDateChip(label = "+3 meses") {
                            val c = Calendar.getInstance()
                            c.add(Calendar.MONTH, 3)
                            eventDateMillis = c.timeInMillis
                        }
                        QuickDateChip(label = "+6 meses") {
                            val c = Calendar.getInstance()
                            c.add(Calendar.MONTH, 6)
                            eventDateMillis = c.timeInMillis
                        }
                        QuickDateChip(label = "+1 año") {
                            val c = Calendar.getInstance()
                            c.add(Calendar.YEAR, 1)
                            eventDateMillis = c.timeInMillis
                        }
                    }
                }

                HorizontalDivider(color = EditorialDivider)

                // Anticipación de Notificación
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.NotificationsActive,
                            contentDescription = null,
                            tint = EditorialPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Avisarme con anticipación",
                            color = EditorialTextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        noticeOptions.forEach { (label, minutes) ->
                            val isSelected = selectedNoticeMinutes == minutes
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) EditorialContainerPill else EditorialSurfaceSubtle,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) EditorialPrimary else EditorialBorder
                                ),
                                modifier = Modifier.clickable { selectedNoticeMinutes = minutes }
                            ) {
                                Text(
                                    text = label,
                                    color = if (isSelected) EditorialPrimary else EditorialTextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }

                // Descripción o notas adicionales
                OutlinedTextField(
                    value = eventDescription,
                    onValueChange = { eventDescription = it },
                    label = { Text("Notas / Detalles adicionales") },
                    minLines = 2,
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialPrimary,
                        unfocusedBorderColor = EditorialBorder,
                        focusedTextColor = EditorialTextPrimary,
                        unfocusedTextColor = EditorialTextPrimary,
                        focusedLabelColor = EditorialPrimary,
                        unfocusedLabelColor = EditorialTextSecondary,
                        cursorColor = EditorialPrimary
                    )
                )

                // Ubicación
                OutlinedTextField(
                    value = eventLocation,
                    onValueChange = { eventLocation = it },
                    label = { Text("Lugar sugerido") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EditorialPrimary,
                        unfocusedBorderColor = EditorialBorder,
                        focusedTextColor = EditorialTextPrimary,
                        unfocusedTextColor = EditorialTextPrimary,
                        focusedLabelColor = EditorialPrimary,
                        unfocusedLabelColor = EditorialTextSecondary,
                        cursorColor = EditorialPrimary
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    CalendarHelper.openCalendarInsert(
                        context = context,
                        title = eventTitle,
                        description = eventDescription,
                        beginTimeMillis = eventDateMillis,
                        endTimeMillis = eventDateMillis + 2 * 60 * 60 * 1000,
                        isAllDay = true,
                        reminderMinutesBefore = selectedNoticeMinutes,
                        location = eventLocation
                    )
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = EditorialPrimary,
                    contentColor = EditorialBackground
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Text("Abrir Google Calendar", fontWeight = FontWeight.Bold)
                }
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
private fun QuickDateChip(label: String, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = EditorialSurfaceSubtle,
        border = androidx.compose.foundation.BorderStroke(1.dp, EditorialBorder),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = label,
            color = EditorialTextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
