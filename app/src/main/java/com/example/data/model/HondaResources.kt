package com.example.data.model

import com.example.R

data class MotoPhotoItem(
    val id: String,
    val title: String,
    val shortLabel: String,
    val description: String,
    val drawableRes: Int
)

data class HondaManualItem(
    val id: String,
    val title: String,
    val fileName: String,
    val category: String,
    val summary: String = "",
    val highlights: List<Pair<String, String>> = emptyList(),
    val iconKey: String = "manual",
    val customFilePath: String? = null,
    val isCustom: Boolean = false
) {
    val assetPath: String get() = "manuales/$fileName"
    val isAsset: Boolean get() = customFilePath == null
}

data class LegalDocumentItem(
    val id: String,
    val title: String,
    val fileName: String,
    val category: String,
    val status: String,
    val referenceNumber: String,
    val validityDate: String,
    val summary: String = "",
    val details: List<Pair<String, String>> = emptyList(),
    val iconKey: String = "document",
    val customFilePath: String? = null,
    val isCustom: Boolean = false
) {
    val assetPath: String get() = "documentos/$fileName"
    val isAsset: Boolean get() = customFilePath == null
}

object HondaResourcesRepository {

    val photos: List<MotoPhotoItem> = listOf(
        MotoPhotoItem(
            id = "photo_01",
            title = "Frontal Derecha 45°",
            shortLabel = "Frontal Der",
            description = "Vista tres cuartos delantera derecha destacando farola, cúpula y carenaje Verde.",
            drawableRes = R.drawable.moto_01_frontal_derecha
        ),
        MotoPhotoItem(
            id = "photo_06",
            title = "Lateral Derecha",
            shortLabel = "Lateral Der",
            description = "Perfil derecho completo mostrando escape, frenos y motor monocilíndrico 125 cc.",
            drawableRes = R.drawable.moto_06_lateral_derecha
        ),
        MotoPhotoItem(
            id = "photo_03",
            title = "Frontal Izquierda 45°",
            shortLabel = "Frontal Izq",
            description = "Vista angular delantera izquierda con manillar, espejos y suspensión telescópica.",
            drawableRes = R.drawable.moto_03_frontal_izquierda
        ),
        MotoPhotoItem(
            id = "photo_09",
            title = "Lateral Izquierda",
            shortLabel = "Lateral Izq",
            description = "Perfil izquierdo luciendo la transmisión por cadena, palanca de cambios y pata lateral.",
            drawableRes = R.drawable.moto_09_lateral_izquierda
        ),
        MotoPhotoItem(
            id = "photo_07",
            title = "Frontal Centrada",
            shortLabel = "Frontal",
            description = "Vista frontal directa con visor aerodinámico y luces direccionales.",
            drawableRes = R.drawable.moto_07_frontal
        ),
        MotoPhotoItem(
            id = "photo_05",
            title = "Superior & Tablero",
            shortLabel = "Superior",
            description = "Vista cenital de tanque de combustible de 13 litros, mandos y cuadro de instrumentos.",
            drawableRes = R.drawable.moto_05_superior
        ),
        MotoPhotoItem(
            id = "photo_02",
            title = "Trasera Derecha 45°",
            shortLabel = "Trasera Der",
            description = "Vista posterior angular mostrando amortiguadores traseros y colín deportivo.",
            drawableRes = R.drawable.moto_02_trasera_derecha
        ),
        MotoPhotoItem(
            id = "photo_04",
            title = "Trasera",
            shortLabel = "Trasera",
            description = "Vista posterior completa con luz de freno, guardabarros y portaplacas.",
            drawableRes = R.drawable.moto_04_trasera
        ),
        MotoPhotoItem(
            id = "photo_08",
            title = "Trasera Centrada",
            shortLabel = "Trasera Cen",
            description = "Vista trasera simétrica y neumático trasero 100/90-17.",
            drawableRes = R.drawable.moto_08_trasera_centrada
        )
    )

    val manuals: List<HondaManualItem> = listOf(
        HondaManualItem(
            id = "man_propietario",
            title = "Manual del Propietario CBF125",
            fileName = "Manual del propietario CBF125.ES.pdf",
            category = "Manual de Usuario",
            summary = "Manual oficial del usuario con normas de conducción, ubicación de mandos, programa de mantenimiento periódico y pautas de seguridad.",
            highlights = listOf(
                "Capacidad de combustible" to "13.0 Litros (incluye 2.0 L de reserva)",
                "Presión de neumáticos (Solo)" to "Delante: 25 psi (1.75 bar) / Detrás: 29 psi (2.00 bar)",
                "Presión de neumáticos (Con pasajero)" to "Delante: 25 psi / Detrás: 33 psi (2.25 bar)",
                "Aceite de motor" to "SAE 10W-30 JASO MA (0.9 L cambio / 1.0 L desarmado)",
                "Juego libre de maneta embrague" to "10 a 20 mm",
                "Holgura de cadena de transmisión" to "25 a 35 mm",
                "Bujía recomendada" to "NGK CPR7EA-9 (Separación: 0.8 - 0.9 mm)"
            ),
            iconKey = "owner"
        ),
        HondaManualItem(
            id = "man_taller",
            title = "Manual de Taller y Servicio CBF125",
            fileName = "Manual de taller CBF125.ES.pdf",
            category = "Taller y Mecánica",
            summary = "Guía completa de taller con procedimientos de desarme, tolerancias mecánicas, sistema eléctrico, inyección/alimentación y motor.",
            highlights = listOf(
                "Holgura de válvulas (Frío)" to "Admisión: 0.08 mm / Escape: 0.12 mm",
                "Ralentí del motor" to "1.400 ± 100 rpm",
                "Compresión del cilindro" to "1.200 kPa (12.2 kgf/cm², 174 psi) a 800 rpm",
                "Líquido de frenos" to "DOT 3 o DOT 4 exclusivamente",
                "Espesor mín. pastilla de freno" to "1.0 mm (marca de límite de desgaste)",
                "Espesor mín. forro zapata trasera" to "2.0 mm",
                "Aceite de horquilla telescópica" to "147 ± 2.5 cm³ por barra (Honda Ultra Cushion Oil 10W)"
            ),
            iconKey = "workshop"
        ),
        HondaManualItem(
            id = "man_especificaciones",
            title = "Especificaciones Técnicas CBF125",
            fileName = "Especificaciones CBF125.ES.pdf",
            category = "Ficha Técnica",
            summary = "Hoja técnica integral con dimensiones, pesos, relación de compresión, relaciones de transmisión y capacidades operativas.",
            highlights = listOf(
                "Motor" to "4 tiempos, monocilíndrico OHC refrigerado por aire",
                "Cilindrada exacta" to "124.7 cc (Diámetro x carrera: 52.4 x 57.8 mm)",
                "Relación de compresión" to "9.2 : 1",
                "Potencia máxima" to "11.1 CV (8.3 kW) @ 8.000 rpm",
                "Par motor máximo" to "11.2 Nm @ 6.250 rpm",
                "Dimensiones (L x An x Al)" to "1.955 mm x 760 mm x 1.110 mm",
                "Distancia entre ejes / Altura asiento" to "1.270 mm / 792 mm",
                "Peso en orden de marcha" to "128 kg",
                "Rines y neumáticos" to "Delantero: 80/100-17 46P / Trasero: 100/90-17 55P"
            ),
            iconKey = "specs"
        ),
        HondaManualItem(
            id = "man_pares_torsion",
            title = "Pares de Torsión del Chasis CBF125",
            fileName = "Pares de torsión del chasis CBF125.ES.pdf",
            category = "Torques de Ajuste",
            summary = "Tabla oficial de pares de apriete para todos los pernos y tuercas críticos del chasis, suspensión y ruedas.",
            highlights = listOf(
                "Tuerca del eje delantero" to "59 N·m (6.0 kgf·m)",
                "Tuerca del eje trasero" to "88 N·m (9.0 kgf·m)",
                "Tuerca del eje basculante" to "54 N·m (5.5 kgf·m)",
                "Tornillos de fijación pinza delantera" to "30 N·m (3.1 kgf·m)",
                "Tornillos de puente de horquilla" to "22 N·m (2.2 kgf·m)",
                "Tornillo de drenaje de aceite" to "24 N·m (2.4 kgf·m)",
                "Bujía" to "16 N·m (1.6 kgf·m)",
                "Tuercas de soporte de motor" to "34 N·m (3.5 kgf·m)"
            ),
            iconKey = "torque"
        ),
        HondaManualItem(
            id = "man_checklist",
            title = "Lista de Verificación de Servicio CBF125",
            fileName = "Lista de verificación de servicio CBF125.ES.pdf",
            category = "Puntos de Control",
            summary = "Checklist estandarizado para revisiones de rutina, inspección previa a viajes y mantenimiento cada 4.000 km.",
            highlights = listOf(
                "Revisión diaria" to "Nivel de aceite, frenos, luces, bocina y presión de llantas",
                "Cada 1.000 km" to "Limpieza, lubricación y ajuste de tensión de cadena",
                "Cada 4.000 km" to "Cambio de aceite de motor y limpieza de filtro centrífugo",
                "Cada 8.000 km" to "Inspección de bujía, filtro de aire y holgura de válvulas",
                "Cada 12.000 km" to "Reemplazo de bujía y filtro de aire",
                "Cada 2 años" to "Cambio de líquido de frenos DOT 4"
            ),
            iconKey = "checklist"
        )
    )

    val documents: List<LegalDocumentItem> = listOf(
        LegalDocumentItem(
            id = "doc_soat",
            title = "Seguro Obligatorio (SOAT)",
            fileName = "",
            category = "Seguros y Pólizas",
            status = "Sin cargar",
            referenceNumber = "Pendiente",
            validityDate = "Sin registrar",
            summary = "Póliza de seguro obligatorio de accidentes de tránsito con cobertura de gastos médicos y daños a terceros.",
            details = listOf(
                "Aseguradora" to "Pendiente",
                "Vehículo asegurado" to "Honda CBF-125",
                "Estado" to "Sin documento PDF cargado"
            )
        ),
        LegalDocumentItem(
            id = "doc_tarjeta_propiedad",
            title = "Tarjeta de Propiedad",
            fileName = "",
            category = "Registro Vehicular",
            status = "Sin cargar",
            referenceNumber = "Pendiente",
            validityDate = "Indefinida",
            summary = "Licencia de Tránsito oficial del vehículo expedida por el organismo de tránsito correspondiente.",
            details = listOf(
                "Placa" to "Pendiente",
                "Marca / Línea" to "Honda CBF-125",
                "Estado" to "Sin documento PDF cargado"
            )
        ),
        LegalDocumentItem(
            id = "doc_licencia",
            title = "Licencia de Conducción",
            fileName = "",
            category = "Documento Conductor",
            status = "Sin cargar",
            referenceNumber = "Pendiente",
            validityDate = "Sin registrar",
            summary = "Licencia de conducción categoría A2 autorizada para motocicletas y mototriciclos.",
            details = listOf(
                "Categoría" to "A2 (Motocicletas)",
                "Estado" to "Sin documento PDF cargado"
            )
        ),
        LegalDocumentItem(
            id = "doc_cedula",
            title = "Cédula de Ciudadanía",
            fileName = "",
            category = "Identidad Personal",
            status = "Sin cargar",
            referenceNumber = "Pendiente",
            validityDate = "Vigente",
            summary = "Documento nacional de identidad del propietario registrado de la motocicleta.",
            details = listOf(
                "Tipo" to "Cédula de Ciudadanía",
                "Estado" to "Sin documento PDF cargado"
            )
        )
    )
}
