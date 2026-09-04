package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "motorcycles")
data class Motorcycle(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 1L,
    val brand: String = "Honda",
    val model: String = "CBF 125",
    val color: String = "Verde Candy",
    val year: Int = 2014,
    val displacement: String = "125 cc",
    val engineType: String = "4 tiempos OHC",
    val transmission: String = "5 velocidades",
    val licensePlate: String = "",
    val purchaseDate: String = "",
    val currentMileageKm: Int = 0,
    val fuelTankCapacityLiters: Double = 13.0,
    val imageResName: String = "honda_cbf_green",
    val engineNumber: String = "",
    val vinNumber: String = "",
    val powerHp: String = "11 HP",
    val transitAuthority: String = "",
    val ownerName: String = "",
    val ownerId: String = "",
    val transitCardNumber: String = ""
)

@Entity(tableName = "maintenance_schedules")
data class MaintenanceSchedule(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val category: String, // "Aceite", "Cadena", "Llantas", "Frenos", "Bujía", "Filtro", "General"
    val intervalKm: Int,
    val intervalMonths: Int,
    val lastServiceKm: Int,
    val nextDueKm: Int,
    val lastServiceDate: String,
    val iconKey: String // "oil", "chain", "tire", "brake", "spark", "filter", "general"
) {
    fun getKmRemaining(currentKm: Int): Int = (nextDueKm - currentKm).coerceAtLeast(0)
    fun isDue(currentKm: Int): Boolean = currentKm >= nextDueKm
}

@Entity(tableName = "service_records")
data class ServiceRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val category: String, // "Mantenimientos", "Repuestos", "Otro"
    val dateDay: String, // "10"
    val dateMonth: String, // "OCT"
    val dateYear: String, // "2024"
    val fullDate: String, // "10/10/2024"
    val mileageKm: Int,
    val workshop: String, // "MotoServicio"
    val cost: Double,
    val notes: String = "",
    val imageDrawableName: String? = null,
    val receiptPhotoUri: String? = null
)

@Entity(tableName = "fuel_records")
data class FuelRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val dateDay: String, // "12"
    val dateMonth: String, // "OCT"
    val dateYear: String, // "2024"
    val fullDate: String,
    val liters: Double, // 8.5
    val totalCost: Double, // 32000.0
    val mileageKm: Int, // 6450
    val isFullTank: Boolean = true,
    val efficiencyKmPerL: Double = 47.6, // km/L
    val notes: String = ""
)

@Entity(tableName = "expenses")
data class ExpenseItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val category: String, // "Combustible", "Mantenimiento", "Repuestos", "Impuestos y SOAT", "Parqueaderos", "Otros"
    val title: String,
    val dateFormatted: String,
    val year: String = "2024",
    val amount: Double,
    val notes: String = ""
)

data class ReminderItem(
    val title: String,
    val dueDate: String,
    val remainingDays: Int,
    val iconKey: String,
    val status: String
)
