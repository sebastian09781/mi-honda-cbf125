package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ExpenseItem
import com.example.data.model.FuelRecord
import com.example.data.model.MaintenanceSchedule
import com.example.data.model.Motorcycle
import com.example.data.model.ServiceRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface MotorcycleDao {
    @Query("SELECT * FROM motorcycles WHERE id = 1 LIMIT 1")
    fun getMotorcycle(): Flow<Motorcycle?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(motorcycle: Motorcycle)

    @Update
    suspend fun update(motorcycle: Motorcycle)

    @Query("UPDATE motorcycles SET currentMileageKm = :newKm WHERE id = 1")
    suspend fun updateMileage(newKm: Int)
}

@Dao
interface MaintenanceDao {
    @Query("SELECT * FROM maintenance_schedules ORDER BY nextDueKm ASC")
    fun getAllSchedules(): Flow<List<MaintenanceSchedule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(schedules: List<MaintenanceSchedule>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(schedule: MaintenanceSchedule): Long

    @Update
    suspend fun update(schedule: MaintenanceSchedule)

    @Query("DELETE FROM maintenance_schedules WHERE id = :id")
    suspend fun deleteById(id: Long)
}

@Dao
interface ServiceRecordDao {
    @Query("SELECT * FROM service_records ORDER BY id DESC")
    fun getAllRecords(): Flow<List<ServiceRecord>>

    @Query("SELECT * FROM service_records WHERE category = :category ORDER BY id DESC")
    fun getRecordsByCategory(category: String): Flow<List<ServiceRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: ServiceRecord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(records: List<ServiceRecord>)

    @Query("DELETE FROM service_records WHERE id = :id")
    suspend fun deleteById(id: Long)
}

@Dao
interface FuelRecordDao {
    @Query("SELECT * FROM fuel_records ORDER BY id DESC")
    fun getAllFuelRecords(): Flow<List<FuelRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fuelRecord: FuelRecord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(fuelRecords: List<FuelRecord>)

    @Query("DELETE FROM fuel_records WHERE id = :id")
    suspend fun deleteById(id: Long)
}

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY id DESC")
    fun getAllExpenses(): Flow<List<ExpenseItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: ExpenseItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(expenses: List<ExpenseItem>)

    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun deleteById(id: Long)
}
