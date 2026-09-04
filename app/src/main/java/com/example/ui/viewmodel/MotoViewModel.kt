package com.example.ui.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.ExpenseItem
import com.example.data.model.FuelRecord
import com.example.data.model.HondaManualItem
import com.example.data.model.HondaResourcesRepository
import com.example.data.model.LegalDocumentItem
import com.example.data.model.MaintenanceSchedule
import com.example.data.model.Motorcycle
import com.example.data.model.ReminderItem
import com.example.data.model.ServiceRecord
import com.example.data.repository.MotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.util.Locale
import org.json.JSONArray
import org.json.JSONObject

data class PdfViewRequest(
    val id: String,
    val title: String,
    val category: String,
    val assetPath: String? = null,
    val customFilePath: String? = null,
    val fileName: String,
    val isCustom: Boolean = false,
    val isManual: Boolean = false
)

enum class AppScreen {
    INICIO,
    MI_MOTO,
    SERVICIOS,
    GASTOS,
    PAPELES,
    CONFIGURACION
}

class MotoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MotoRepository

    init {
        val database = AppDatabase.getDatabase(application, viewModelScope)
        repository = MotoRepository(database)
    }

    private val prefs = application.getSharedPreferences("app_settings", android.content.Context.MODE_PRIVATE)
    private val _isOnboardingCompleted = MutableStateFlow(prefs.getBoolean("onboarding_completed", false))
    val isOnboardingCompleted: StateFlow<Boolean> = _isOnboardingCompleted.asStateFlow()

    fun completeOnboarding(
        licensePlate: String,
        color: String,
        year: Int,
        initialKm: Int,
        ownerName: String = "",
        ownerId: String = "",
        soatUri: Uri? = null,
        tarjetaUri: Uri? = null,
        licenciaUri: Uri? = null,
        cedulaUri: Uri? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentMoto = motorcycle.value ?: Motorcycle(id = 1L)
            val updated = currentMoto.copy(
                licensePlate = licensePlate.trim().uppercase(),
                color = color.trim().ifBlank { "Verde Candy" },
                year = if (year > 0) year else 2024,
                currentMileageKm = initialKm,
                ownerName = ownerName.trim(),
                ownerId = ownerId.trim()
            )
            repository.updateMotorcycle(updated)

            val plateForDoc = updated.licensePlate.ifBlank { "PENDIENTE" }

            if (soatUri != null) {
                saveDocumentFile("doc_soat", "SOAT (Seguro Obligatorio)", "Vigente", "Vigente 1 año", "SOAT-$plateForDoc", soatUri)
            }
            if (tarjetaUri != null) {
                saveDocumentFile("doc_tarjeta_propiedad", "Tarjeta de Propiedad", "Vigente", "Indefinida", "Lic-$plateForDoc", tarjetaUri)
            }
            if (licenciaUri != null) {
                saveDocumentFile("doc_licencia_conduccion", "Licencia de Conducción A2", "Vigente", "Vigente 10 años", "A2-$plateForDoc", licenciaUri)
            }
            if (cedulaUri != null) {
                saveDocumentFile("doc_cedula_ciudadania", "Cédula de Ciudadanía", "Vigente", "Indefinida", ownerId.ifBlank { "C.C." }, cedulaUri)
            }

            prefs.edit().putBoolean("onboarding_completed", true).apply()
            _isOnboardingCompleted.value = true
        }
    }

    private fun saveDocumentFile(
        docId: String,
        title: String,
        status: String,
        validityDate: String,
        referenceNumber: String,
        uri: Uri
    ) {
        try {
            val docsDir = File(getApplication<Application>().filesDir, "user_docs")
            if (!docsDir.exists()) docsDir.mkdirs()
            val targetFile = File(docsDir, "${docId}_custom.pdf")
            getApplication<Application>().contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(targetFile).use { output ->
                    input.copyTo(output)
                }
            }
            val customPath = targetFile.absolutePath
            val newFileName = uri.lastPathSegment ?: "${docId}.pdf"
            _documents.update { list ->
                list.map { doc ->
                    if (doc.id == docId) {
                        doc.copy(
                            title = title,
                            status = status,
                            validityDate = validityDate,
                            referenceNumber = referenceNumber,
                            customFilePath = customPath,
                            fileName = newFileName,
                            isCustom = true
                        )
                    } else doc
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun restartOnboarding() {
        prefs.edit().putBoolean("onboarding_completed", false).apply()
        _isOnboardingCompleted.value = false
    }

    // Navigation & Screen State
    private val _currentScreen = MutableStateFlow(AppScreen.INICIO)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // Sub-tab selection for Servicios: 0 = Programados, 1 = Historial
    private val _servicesSubTab = MutableStateFlow(0)
    val servicesSubTab: StateFlow<Int> = _servicesSubTab.asStateFlow()

    fun setServicesSubTab(tab: Int) {
        _servicesSubTab.value = tab
    }

    // Sub-tab selection for Gastos: 0 = Gastos, 1 = Combustible
    private val _expensesSubTab = MutableStateFlow(0)
    val expensesSubTab: StateFlow<Int> = _expensesSubTab.asStateFlow()

    fun setExpensesSubTab(tab: Int) {
        _expensesSubTab.value = tab
    }

    // Motorcycle Profile
    val motorcycle: StateFlow<Motorcycle?> = repository.motorcycle
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Maintenance Schedules
    val schedules: StateFlow<List<MaintenanceSchedule>> = repository.schedules
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Maintenance Tab selection inside Programados (0 = Por kilometraje, 1 = Por tiempo)
    private val _maintTab = MutableStateFlow(0)
    val maintTab: StateFlow<Int> = _maintTab.asStateFlow()

    // History Records & Filter
    private val _historyFilter = MutableStateFlow("Todos")
    val historyFilter: StateFlow<String> = _historyFilter.asStateFlow()

    val serviceRecords: StateFlow<List<ServiceRecord>> = combine(
        repository.serviceRecords,
        _historyFilter
    ) { records, filter ->
        if (filter == "Todos") {
            records
        } else {
            records.filter { it.category.equals(filter, ignoreCase = true) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Fuel Records
    val fuelRecords: StateFlow<List<FuelRecord>> = repository.fuelRecords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Expenses & Period Filter
    private val _expensesPeriod = MutableStateFlow("Año") // "Mes", "Año", "Total"
    val expensesPeriod: StateFlow<String> = _expensesPeriod.asStateFlow()

    val expenses: StateFlow<List<ExpenseItem>> = repository.expenses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Legal Documents and Manuals Management (Dynamic & Editable)
    private val _documents = MutableStateFlow<List<LegalDocumentItem>>(HondaResourcesRepository.documents)
    val documents: StateFlow<List<LegalDocumentItem>> = _documents.asStateFlow()

    private val _manuals = MutableStateFlow<List<HondaManualItem>>(HondaResourcesRepository.manuals)
    val manuals: StateFlow<List<HondaManualItem>> = _manuals.asStateFlow()

    // Reminders
    val reminders: StateFlow<List<ReminderItem>> = MutableStateFlow(
        listOf(
            ReminderItem(
                title = "SOAT (Seguro Obligatorio)",
                dueDate = "10/03/2025",
                remainingDays = 185,
                iconKey = "shield",
                status = "Vigente"
            ),
            ReminderItem(
                title = "Revisión Técnico-Mecánica",
                dueDate = "10/03/2026",
                remainingDays = 550,
                iconKey = "tool",
                status = "Vigente (Moto Nueva 2 años)"
            ),
            ReminderItem(
                title = "Impuesto Vehicular",
                dueDate = "15/06/2025",
                remainingDays = 280,
                iconKey = "receipt",
                status = "Al día"
            ),
            ReminderItem(
                title = "Garantía de Fábrica Honda",
                dueDate = "10/03/2025",
                remainingDays = 185,
                iconKey = "star",
                status = "Activa (20.000 km o 1 año)"
            )
        )
    ).asStateFlow()

    // Dialog Visibility States
    val showUpdateKmDialog = MutableStateFlow(false)
    val showAddMaintenanceDialog = MutableStateFlow(false)
    val showAddHistoryDialog = MutableStateFlow(false)
    val showAddFuelDialog = MutableStateFlow(false)
    val showAddExpenseDialog = MutableStateFlow(false)
    val showEditMotoDialog = MutableStateFlow(false)

    // Manuals, Documents & Real PDF Viewer States
    val selectedManualToEdit = MutableStateFlow<HondaManualItem?>(null)
    val selectedDocumentToEdit = MutableStateFlow<LegalDocumentItem?>(null)
    val selectedPdfToView = MutableStateFlow<PdfViewRequest?>(null)

    // Calendar Reminder Dialog State
    val selectedCalendarReminder = MutableStateFlow<com.example.ui.components.CalendarReminderData?>(null)

    fun openCalendarReminder(data: com.example.ui.components.CalendarReminderData) {
        selectedCalendarReminder.value = data
    }

    fun dismissCalendarReminder() {
        selectedCalendarReminder.value = null
    }

    fun openManualPdf(manual: HondaManualItem) {
        selectedPdfToView.value = PdfViewRequest(
            id = manual.id,
            title = manual.title,
            category = manual.category,
            assetPath = if (manual.isCustom) null else manual.assetPath,
            customFilePath = manual.customFilePath,
            fileName = manual.fileName,
            isCustom = manual.isCustom,
            isManual = true
        )
    }

    fun openDocumentPdf(doc: LegalDocumentItem) {
        selectedPdfToView.value = PdfViewRequest(
            id = doc.id,
            title = doc.title,
            category = doc.category,
            assetPath = if (doc.isCustom) null else doc.assetPath,
            customFilePath = doc.customFilePath,
            fileName = doc.fileName,
            isCustom = doc.isCustom,
            isManual = false
        )
    }

    fun updateDocument(
        docId: String,
        title: String,
        status: String,
        validityDate: String,
        referenceNumber: String,
        newUri: Uri?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            var customPath: String? = null
            var newFileName: String? = null
            if (newUri != null) {
                try {
                    val docsDir = File(getApplication<Application>().filesDir, "user_docs")
                    if (!docsDir.exists()) docsDir.mkdirs()
                    val targetFile = File(docsDir, "${docId}_custom.pdf")
                    getApplication<Application>().contentResolver.openInputStream(newUri)?.use { input ->
                        FileOutputStream(targetFile).use { output ->
                            input.copyTo(output)
                        }
                    }
                    customPath = targetFile.absolutePath
                    newFileName = newUri.lastPathSegment ?: "documento_actualizado.pdf"
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            _documents.update { list ->
                list.map { doc ->
                    if (doc.id == docId) {
                        doc.copy(
                            title = title,
                            status = status,
                            validityDate = validityDate,
                            referenceNumber = referenceNumber,
                            customFilePath = customPath ?: doc.customFilePath,
                            fileName = newFileName ?: doc.fileName,
                            isCustom = (customPath != null) || doc.isCustom
                        )
                    } else doc
                }
            }

            val currentView = selectedPdfToView.value
            if (currentView != null && currentView.id == docId) {
                val updatedDoc = _documents.value.find { it.id == docId }
                if (updatedDoc != null) {
                    openDocumentPdf(updatedDoc)
                }
            }
        }
    }

    fun resetDocumentToDefault(docId: String) {
        val defaultDoc = HondaResourcesRepository.documents.find { it.id == docId } ?: return
        _documents.update { list ->
            list.map { if (it.id == docId) defaultDoc else it }
        }
        val currentView = selectedPdfToView.value
        if (currentView != null && currentView.id == docId) {
            openDocumentPdf(defaultDoc)
        }
    }

    fun updateManual(
        manualId: String,
        title: String,
        newUri: Uri?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            var customPath: String? = null
            var newFileName: String? = null
            if (newUri != null) {
                try {
                    val manualsDir = File(getApplication<Application>().filesDir, "user_manuals")
                    if (!manualsDir.exists()) manualsDir.mkdirs()
                    val targetFile = File(manualsDir, "${manualId}_custom.pdf")
                    getApplication<Application>().contentResolver.openInputStream(newUri)?.use { input ->
                        FileOutputStream(targetFile).use { output ->
                            input.copyTo(output)
                        }
                    }
                    customPath = targetFile.absolutePath
                    newFileName = newUri.lastPathSegment ?: "manual_actualizado.pdf"
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            _manuals.update { list ->
                list.map { man ->
                    if (man.id == manualId) {
                        man.copy(
                            title = title,
                            customFilePath = customPath ?: man.customFilePath,
                            fileName = newFileName ?: man.fileName,
                            isCustom = (customPath != null) || man.isCustom
                        )
                    } else man
                }
            }

            val currentView = selectedPdfToView.value
            if (currentView != null && currentView.id == manualId) {
                val updatedMan = _manuals.value.find { it.id == manualId }
                if (updatedMan != null) {
                    openManualPdf(updatedMan)
                }
            }
        }
    }

    fun resetManualToDefault(manualId: String) {
        val defaultMan = HondaResourcesRepository.manuals.find { it.id == manualId } ?: return
        _manuals.update { list ->
            list.map { if (it.id == manualId) defaultMan else it }
        }
        val currentView = selectedPdfToView.value
        if (currentView != null && currentView.id == manualId) {
            openManualPdf(defaultMan)
        }
    }

    fun replaceCurrentOpenPdf(uri: Uri) {
        val current = selectedPdfToView.value ?: return
        if (current.isManual) {
            updateManual(current.id, current.title, uri)
        } else {
            val doc = _documents.value.find { it.id == current.id }
            if (doc != null) {
                updateDocument(doc.id, doc.title, doc.status, doc.validityDate, doc.referenceNumber, uri)
            }
        }
    }

    fun resetCurrentOpenPdf() {
        val current = selectedPdfToView.value ?: return
        if (current.isManual) {
            resetManualToDefault(current.id)
        } else {
            resetDocumentToDefault(current.id)
        }
    }

    // User Actions
    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun setMaintTab(tab: Int) {
        _maintTab.value = tab
    }

    fun setHistoryFilter(filter: String) {
        _historyFilter.value = filter
    }

    fun setExpensesPeriod(period: String) {
        _expensesPeriod.value = period
    }

    fun updateMileage(newKm: Int) {
        viewModelScope.launch {
            repository.updateMileage(newKm)
        }
    }

    fun updateMotorcycle(moto: Motorcycle) {
        viewModelScope.launch {
            repository.updateMotorcycle(moto)
        }
    }

    fun registerMaintenanceDone(
        scheduleId: Long,
        title: String,
        category: String,
        cost: Double,
        workshop: String,
        currentKm: Int,
        intervalKm: Int,
        notes: String,
        receiptPhotoUri: String? = null
    ) {
        viewModelScope.launch {
            val nextKm = currentKm + intervalKm
            val sched = schedules.value.find { it.id == scheduleId }
            if (sched != null) {
                repository.updateMaintenanceSchedule(
                    sched.copy(
                        lastServiceKm = currentKm,
                        nextDueKm = nextKm,
                        lastServiceDate = "Hoy"
                    )
                )
            }

            repository.registerServiceRecord(
                ServiceRecord(
                    title = title,
                    category = category,
                    dateDay = "HOY",
                    dateMonth = "ACT",
                    dateYear = "2024",
                    fullDate = "Hoy",
                    mileageKm = currentKm,
                    workshop = workshop.ifBlank { "MotoServicio" },
                    cost = cost,
                    notes = notes,
                    imageDrawableName = null,
                    receiptPhotoUri = receiptPhotoUri
                )
            )
        }
    }

    fun addCustomMaintenanceSchedule(
        title: String,
        category: String,
        intervalKm: Int,
        intervalMonths: Int,
        lastServiceKm: Int,
        iconKey: String
    ) {
        viewModelScope.launch {
            val currKm = motorcycle.value?.currentMileageKm ?: 0
            val nextDue = if (lastServiceKm > 0) lastServiceKm + intervalKm else currKm + intervalKm
            repository.addMaintenanceSchedule(
                MaintenanceSchedule(
                    title = title,
                    category = category,
                    intervalKm = intervalKm,
                    intervalMonths = intervalMonths,
                    lastServiceKm = lastServiceKm,
                    nextDueKm = nextDue,
                    lastServiceDate = "Registro",
                    iconKey = iconKey
                )
            )
        }
    }

    fun addServiceRecord(
        title: String,
        category: String,
        day: String,
        month: String,
        year: String,
        mileageKm: Int,
        workshop: String,
        cost: Double,
        notes: String,
        receiptPhotoUri: String? = null
    ) {
        viewModelScope.launch {
            repository.registerServiceRecord(
                ServiceRecord(
                    title = title,
                    category = category,
                    dateDay = day.ifBlank { "01" },
                    dateMonth = month.ifBlank { "NOV" }.uppercase(),
                    dateYear = year.ifBlank { "2024" },
                    fullDate = "$day/$month/$year",
                    mileageKm = mileageKm,
                    workshop = workshop.ifBlank { "MotoServicio" },
                    cost = cost,
                    notes = notes,
                    imageDrawableName = null,
                    receiptPhotoUri = receiptPhotoUri
                )
            )
        }
    }

    fun addFuelRecord(
        liters: Double,
        totalCost: Double,
        mileageKm: Int,
        notes: String
    ) {
        viewModelScope.launch {
            val lastKm = motorcycle.value?.currentMileageKm ?: 0
            val distance = (mileageKm - lastKm).coerceAtLeast(1)
            val efficiency = if (liters > 0) (distance / liters).coerceIn(35.0, 65.0) else 47.6

            repository.addFuelRecord(
                FuelRecord(
                    dateDay = "HOY",
                    dateMonth = "OCT",
                    dateYear = "2024",
                    fullDate = "Hoy",
                    liters = liters,
                    totalCost = totalCost,
                    mileageKm = mileageKm,
                    isFullTank = true,
                    efficiencyKmPerL = String.format(Locale.US, "%.1f", efficiency).toDoubleOrNull() ?: 47.6,
                    notes = notes
                )
            )
        }
    }

    fun addExpense(
        category: String,
        title: String,
        amount: Double,
        notes: String
    ) {
        viewModelScope.launch {
            repository.addExpense(
                ExpenseItem(
                    category = category,
                    title = title,
                    dateFormatted = "Octubre 2024",
                    year = "2024",
                    amount = amount,
                    notes = notes
                )
            )
        }
    }

    fun exportBackupJson(): String {
        val root = JSONObject()
        val moto = motorcycle.value
        if (moto != null) {
            val motoObj = JSONObject().apply {
                put("brand", moto.brand)
                put("model", moto.model)
                put("color", moto.color)
                put("year", moto.year)
                put("displacement", moto.displacement)
                put("engineType", moto.engineType)
                put("transmission", moto.transmission)
                put("licensePlate", moto.licensePlate)
                put("purchaseDate", moto.purchaseDate)
                put("currentMileageKm", moto.currentMileageKm)
                put("fuelTankCapacityLiters", moto.fuelTankCapacityLiters)
                put("engineNumber", moto.engineNumber)
                put("vinNumber", moto.vinNumber)
                put("powerHp", moto.powerHp)
                put("transitAuthority", moto.transitAuthority)
                put("ownerName", moto.ownerName)
                put("ownerId", moto.ownerId)
                put("transitCardNumber", moto.transitCardNumber)
            }
            root.put("motorcycle", motoObj)
        }

        val schedArr = JSONArray()
        schedules.value.forEach { s ->
            schedArr.put(JSONObject().apply {
                put("title", s.title)
                put("category", s.category)
                put("intervalKm", s.intervalKm)
                put("intervalMonths", s.intervalMonths)
                put("lastServiceKm", s.lastServiceKm)
                put("nextDueKm", s.nextDueKm)
                put("lastServiceDate", s.lastServiceDate)
                put("iconKey", s.iconKey)
            })
        }
        root.put("schedules", schedArr)

        val recordsArr = JSONArray()
        serviceRecords.value.forEach { r ->
            recordsArr.put(JSONObject().apply {
                put("title", r.title)
                put("category", r.category)
                put("dateDay", r.dateDay)
                put("dateMonth", r.dateMonth)
                put("dateYear", r.dateYear)
                put("fullDate", r.fullDate)
                put("mileageKm", r.mileageKm)
                put("workshop", r.workshop)
                put("cost", r.cost)
                put("notes", r.notes)
                put("receiptPhotoUri", r.receiptPhotoUri ?: "")
            })
        }
        root.put("serviceRecords", recordsArr)

        val fuelArr = JSONArray()
        fuelRecords.value.forEach { f ->
            fuelArr.put(JSONObject().apply {
                put("dateDay", f.dateDay)
                put("dateMonth", f.dateMonth)
                put("dateYear", f.dateYear)
                put("fullDate", f.fullDate)
                put("liters", f.liters)
                put("totalCost", f.totalCost)
                put("mileageKm", f.mileageKm)
                put("notes", f.notes)
            })
        }
        root.put("fuelRecords", fuelArr)

        val expArr = JSONArray()
        expenses.value.forEach { e ->
            expArr.put(JSONObject().apply {
                put("category", e.category)
                put("title", e.title)
                put("dateFormatted", e.dateFormatted)
                put("year", e.year)
                put("amount", e.amount)
                put("notes", e.notes)
            })
        }
        root.put("expenses", expArr)

        return root.toString(2)
    }

    fun restoreBackupJson(jsonString: String): Boolean {
        return try {
            val root = JSONObject(jsonString)
            viewModelScope.launch(Dispatchers.IO) {
                if (root.has("motorcycle")) {
                    val m = root.getJSONObject("motorcycle")
                    val updated = Motorcycle(
                        id = 1L,
                        brand = m.optString("brand", "Honda"),
                        model = m.optString("model", "CBF-125"),
                        color = m.optString("color", ""),
                        year = m.optInt("year", 2024),
                        displacement = m.optString("displacement", "125 cc"),
                        engineType = m.optString("engineType", "4 tiempos OHC"),
                        transmission = m.optString("transmission", "5 velocidades"),
                        licensePlate = m.optString("licensePlate", ""),
                        purchaseDate = m.optString("purchaseDate", ""),
                        currentMileageKm = m.optInt("currentMileageKm", 0),
                        fuelTankCapacityLiters = m.optDouble("fuelTankCapacityLiters", 13.0),
                        engineNumber = m.optString("engineNumber", ""),
                        vinNumber = m.optString("vinNumber", ""),
                        powerHp = m.optString("powerHp", "11 HP"),
                        transitAuthority = m.optString("transitAuthority", ""),
                        ownerName = m.optString("ownerName", ""),
                        ownerId = m.optString("ownerId", ""),
                        transitCardNumber = m.optString("transitCardNumber", "")
                    )
                    repository.updateMotorcycle(updated)
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun formatCurrency(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
        format.maximumFractionDigits = 0
        return format.format(amount).replace("COP", "").trim()
    }
}
