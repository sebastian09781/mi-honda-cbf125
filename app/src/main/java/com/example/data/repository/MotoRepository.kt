package com.example.data.repository

import com.example.data.db.AppDatabase
import com.example.data.model.ExpenseItem
import com.example.data.model.FuelRecord
import com.example.data.model.MaintenanceSchedule
import com.example.data.model.Motorcycle
import com.example.data.model.ServiceRecord
import kotlinx.coroutines.flow.Flow

class MotoRepository(private val database: AppDatabase) {

    val motorcycle: Flow<Motorcycle?> = database.motorcycleDao().getMotorcycle()
    val schedules: Flow<List<MaintenanceSchedule>> = database.maintenanceDao().getAllSchedules()
    val serviceRecords: Flow<List<ServiceRecord>> = database.serviceRecordDao().getAllRecords()
    val fuelRecords: Flow<List<FuelRecord>> = database.fuelRecordDao().getAllFuelRecords()
    val expenses: Flow<List<ExpenseItem>> = database.expenseDao().getAllExpenses()

    suspend fun updateMotorcycle(motorcycle: Motorcycle) {
        database.motorcycleDao().insertOrUpdate(motorcycle)
    }

    suspend fun updateMileage(newKm: Int) {
        database.motorcycleDao().updateMileage(newKm)
    }

    suspend fun addMaintenanceSchedule(schedule: MaintenanceSchedule) {
        database.maintenanceDao().insert(schedule)
    }

    suspend fun updateMaintenanceSchedule(schedule: MaintenanceSchedule) {
        database.maintenanceDao().update(schedule)
    }

    suspend fun registerServiceRecord(
        record: ServiceRecord,
        updateMaintScheduleId: Long? = null,
        nextDueIntervalKm: Int? = null
    ) {
        database.serviceRecordDao().insert(record)
        // Also add to expenses
        val expenseCategory = if (record.category == "Repuestos") "Repuestos" else "Mantenimiento"
        database.expenseDao().insert(
            ExpenseItem(
                category = expenseCategory,
                title = record.title,
                dateFormatted = "${record.dateMonth} ${record.dateYear}",
                year = record.dateYear,
                amount = record.cost,
                notes = "${record.workshop} - ${record.mileageKm} km"
            )
        )
        // If related to a maintenance schedule, update the schedule's lastServiceKm and nextDueKm
        if (updateMaintScheduleId != null && nextDueIntervalKm != null) {
            // Updated directly or handled by ViewModel
        }
    }

    suspend fun addFuelRecord(fuelRecord: FuelRecord) {
        database.fuelRecordDao().insert(fuelRecord)
        database.expenseDao().insert(
            ExpenseItem(
                category = "Combustible",
                title = "Tanqueo ${fuelRecord.liters} L",
                dateFormatted = "${fuelRecord.dateMonth} ${fuelRecord.dateYear}",
                year = fuelRecord.dateYear,
                amount = fuelRecord.totalCost,
                notes = "${fuelRecord.mileageKm} km (${fuelRecord.efficiencyKmPerL} km/L)"
            )
        )
        if (fuelRecord.mileageKm > 0) {
            database.motorcycleDao().updateMileage(fuelRecord.mileageKm)
        }
    }

    suspend fun addExpense(expense: ExpenseItem) {
        database.expenseDao().insert(expense)
    }

    suspend fun deleteServiceRecord(id: Long) {
        database.serviceRecordDao().deleteById(id)
    }

    suspend fun deleteFuelRecord(id: Long) {
        database.fuelRecordDao().deleteById(id)
    }

    suspend fun deleteExpense(id: Long) {
        database.expenseDao().deleteById(id)
    }

    suspend fun resetToDefaultSeed() {
        database.motorcycleDao().insertOrUpdate(
            Motorcycle(
                id = 1L,
                brand = "Honda",
                model = "CBF-125",
                color = "Verde Candy",
                year = 2014,
                displacement = "125 cc",
                powerHp = "11 HP",
                engineType = "4 tiempos OHC",
                transmission = "5 velocidades",
                licensePlate = "",
                purchaseDate = "",
                currentMileageKm = 0,
                fuelTankCapacityLiters = 13.0,
                imageResName = "honda_cbf_green",
                engineNumber = "",
                vinNumber = "",
                transitAuthority = "",
                ownerName = "",
                ownerId = "",
                transitCardNumber = ""
            )
        )
    }
}
