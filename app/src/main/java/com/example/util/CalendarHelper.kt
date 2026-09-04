package com.example.util

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.widget.Toast
import java.util.Calendar

object CalendarHelper {

    /**
     * Opens Google Calendar or default device calendar with an event pre-filled.
     * Uses Android's official CalendarContract Intent so no invasive permissions are needed.
     */
    fun openCalendarInsert(
        context: Context,
        title: String,
        description: String,
        beginTimeMillis: Long,
        endTimeMillis: Long? = null,
        isAllDay: Boolean = true,
        reminderMinutesBefore: Int = 1440, // 1 day before by default
        location: String = "Taller / Tránsito"
    ) {
        try {
            val endMillis = endTimeMillis ?: (beginTimeMillis + 2 * 60 * 60 * 1000)
            val intent = Intent(Intent.ACTION_INSERT).apply {
                data = CalendarContract.Events.CONTENT_URI
                putExtra(CalendarContract.Events.TITLE, title)
                putExtra(CalendarContract.Events.DESCRIPTION, description)
                putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTimeMillis)
                putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
                putExtra(CalendarContract.Events.ALL_DAY, isAllDay)
                putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                if (reminderMinutesBefore > 0) {
                    putExtra(CalendarContract.Reminders.MINUTES, reminderMinutesBefore)
                    putExtra(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
                }
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                context,
                "No se encontró una aplicación de calendario compatible.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
