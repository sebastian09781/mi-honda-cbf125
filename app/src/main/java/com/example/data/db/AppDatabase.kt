package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.ExpenseItem
import com.example.data.model.FuelRecord
import com.example.data.model.MaintenanceSchedule
import com.example.data.model.Motorcycle
import com.example.data.model.ServiceRecord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Motorcycle::class,
        MaintenanceSchedule::class,
        ServiceRecord::class,
        FuelRecord::class,
        ExpenseItem::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun motorcycleDao(): MotorcycleDao
    abstract fun maintenanceDao(): MaintenanceDao
    abstract fun serviceRecordDao(): ServiceRecordDao
    abstract fun fuelRecordDao(): FuelRecordDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "moto_cbf125_clean_db"
                )
                .fallbackToDestructiveMigration()
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database)
                    }
                }
            }
        }

        suspend fun populateInitialData(database: AppDatabase) {
            val motoDao = database.motorcycleDao()
            val maintDao = database.maintenanceDao()

            // 1. Initial Motorcycle Profile Template (Clean for first-use)
            motoDao.insertOrUpdate(
                Motorcycle(
                    id = 1L,
                    brand = "Honda",
                    model = "CBF-125",
                    color = "Verde Candy",
                    year = 2014,
                    displacement = "125 cc",
                    engineType = "4 tiempos OHC",
                    transmission = "5 velocidades",
                    licensePlate = "",
                    purchaseDate = "",
                    currentMileageKm = 0,
                    fuelTankCapacityLiters = 13.0,
                    imageResName = "honda_cbf_green",
                    engineNumber = "",
                    vinNumber = "",
                    powerHp = "11 HP",
                    transitAuthority = "",
                    ownerName = "",
                    ownerId = "",
                    transitCardNumber = ""
                )
            )

            // 2. Official Maintenance Schedule Templates ready for tracking from 0 km
            val initialSchedules = listOf(
                MaintenanceSchedule(
                    id = 1L,
                    title = "Cambio de aceite",
                    category = "Aceite",
                    intervalKm = 2000,
                    intervalMonths = 3,
                    lastServiceKm = 0,
                    nextDueKm = 2000,
                    lastServiceDate = "Pendiente",
                    iconKey = "oil"
                ),
                MaintenanceSchedule(
                    id = 2L,
                    title = "Lubricación y tensión de cadena",
                    category = "Cadena",
                    intervalKm = 500,
                    intervalMonths = 1,
                    lastServiceKm = 0,
                    nextDueKm = 500,
                    lastServiceDate = "Pendiente",
                    iconKey = "chain"
                ),
                MaintenanceSchedule(
                    id = 3L,
                    title = "Revisión de llantas y presión",
                    category = "Llantas",
                    intervalKm = 3000,
                    intervalMonths = 6,
                    lastServiceKm = 0,
                    nextDueKm = 3000,
                    lastServiceDate = "Pendiente",
                    iconKey = "tire"
                ),
                MaintenanceSchedule(
                    id = 4L,
                    title = "Revisión de frenos y pastillas",
                    category = "Frenos",
                    intervalKm = 4000,
                    intervalMonths = 6,
                    lastServiceKm = 0,
                    nextDueKm = 4000,
                    lastServiceDate = "Pendiente",
                    iconKey = "brake"
                ),
                MaintenanceSchedule(
                    id = 5L,
                    title = "Cambio de bujía",
                    category = "Bujía",
                    intervalKm = 6000,
                    intervalMonths = 12,
                    lastServiceKm = 0,
                    nextDueKm = 6000,
                    lastServiceDate = "Pendiente",
                    iconKey = "spark"
                ),
                MaintenanceSchedule(
                    id = 6L,
                    title = "Filtro de aire y carburación",
                    category = "Filtro",
                    intervalKm = 6000,
                    intervalMonths = 12,
                    lastServiceKm = 0,
                    nextDueKm = 6000,
                    lastServiceDate = "Pendiente",
                    iconKey = "filter"
                )
            )
            maintDao.insertAll(initialSchedules)

            // No fake service records, no fake fuel records, no fake expenses.
            // Starts completely clean from 0 as a fully functional real application.
        }
    }
}
