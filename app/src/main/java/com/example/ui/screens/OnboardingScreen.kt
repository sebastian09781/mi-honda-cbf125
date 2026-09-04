package com.example.ui.screens

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.EditorialBackground
import com.example.ui.theme.EditorialBorder
import com.example.ui.theme.EditorialContainer
import com.example.ui.theme.EditorialContainerPill
import com.example.ui.theme.EditorialDivider
import com.example.ui.theme.EditorialPrimary
import com.example.ui.theme.EditorialSurface
import com.example.ui.theme.EditorialSurfaceSubtle
import com.example.ui.theme.EditorialTextPrimary
import com.example.ui.theme.EditorialTextSecondary
import com.example.ui.theme.EditorialTextTertiary
import com.example.ui.viewmodel.MotoViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OnboardingScreen(viewModel: MotoViewModel) {
    val context = LocalContext.current

    // Form states
    var licensePlate by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf("Verde Candy") }
    var customColor by remember { mutableStateOf("") }
    var yearText by remember { mutableStateOf("2014") }
    var mileageText by remember { mutableStateOf("0") }
    var ownerName by remember { mutableStateOf("") }
    var ownerId by remember { mutableStateOf("") }

    // Uploaded URIs & file names
    var soatUri by remember { mutableStateOf<Uri?>(null) }
    var soatName by remember { mutableStateOf<String?>(null) }

    var tarjetaUri by remember { mutableStateOf<Uri?>(null) }
    var tarjetaName by remember { mutableStateOf<String?>(null) }

    var licenciaUri by remember { mutableStateOf<Uri?>(null) }
    var licenciaName by remember { mutableStateOf<String?>(null) }

    var cedulaUri by remember { mutableStateOf<Uri?>(null) }
    var cedulaName by remember { mutableStateOf<String?>(null) }

    // File pickers
    val soatPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            soatUri = uri
            soatName = getFileNameFromUri(context, uri)
        }
    }
    val tarjetaPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            tarjetaUri = uri
            tarjetaName = getFileNameFromUri(context, uri)
        }
    }
    val licenciaPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            licenciaUri = uri
            licenciaName = getFileNameFromUri(context, uri)
        }
    }
    val cedulaPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            cedulaUri = uri
            cedulaName = getFileNameFromUri(context, uri)
        }
    }

    val standardColors = listOf("Verde Candy", "Negro", "Rojo", "Blanco", "Otro")

    fun onFinishOnboarding() {
        val finalColor = if (selectedColor == "Otro" && customColor.isNotBlank()) customColor.trim() else selectedColor
        val year = yearText.toIntOrNull() ?: 2014
        val initialKm = mileageText.toIntOrNull() ?: 0

        viewModel.completeOnboarding(
            licensePlate = licensePlate.trim().uppercase(),
            color = finalColor,
            year = year,
            initialKm = initialKm,
            ownerName = ownerName.trim(),
            ownerId = ownerId.trim(),
            soatUri = soatUri,
            tarjetaUri = tarjetaUri,
            licenciaUri = licenciaUri,
            cedulaUri = cedulaUri
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = EditorialBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            // Header: Branding Honda & CBF 125
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_honda_logo_new),
                        contentDescription = "Honda Wing",
                        modifier = Modifier.size(36.dp),
                        contentScale = ContentScale.Fit
                    )
                    Image(
                        painter = painterResource(id = R.drawable.logo_cbf125_white),
                        contentDescription = "CBF 125",
                        modifier = Modifier.height(26.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "¡Bienvenido a Mi Honda!",
                    color = EditorialTextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Configura tu CBF-125 para sincronizar mantenimientos oficiales, gastos y tu billetera digital.",
                    color = EditorialTextSecondary,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Motorcycle Hero Image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(200.dp, 100.dp)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        EditorialPrimary.copy(alpha = 0.18f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                    Image(
                        painter = painterResource(id = R.drawable.moto_01_frontal_derecha),
                        contentDescription = "Honda CBF 125",
                        modifier = Modifier.height(140.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 1: DATOS DEL VEHÍCULO
            SectionTitle(title = "1. DATOS DE TU CBF-125", icon = Icons.Filled.DirectionsBike)
            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EditorialSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Placa
                    OutlinedTextField(
                        value = licensePlate,
                        onValueChange = { licensePlate = it.uppercase() },
                        label = { Text("Placa del Vehículo") },
                        placeholder = { Text("ej. ABC12D", color = EditorialTextTertiary) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
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

                    // Color selection
                    Column {
                        Text(
                            text = "Color de la moto",
                            color = EditorialTextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            standardColors.forEach { colorName ->
                                val isSelected = selectedColor == colorName
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSelected) EditorialContainerPill else EditorialSurfaceSubtle,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) EditorialPrimary else EditorialBorder
                                    ),
                                    modifier = Modifier.clickable { selectedColor = colorName }
                                ) {
                                    Text(
                                        text = colorName,
                                        color = if (isSelected) EditorialPrimary else EditorialTextSecondary,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }

                        if (selectedColor == "Otro") {
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = customColor,
                                onValueChange = { customColor = it },
                                label = { Text("Especifica el color") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = EditorialPrimary,
                                    unfocusedBorderColor = EditorialBorder,
                                    focusedTextColor = EditorialTextPrimary,
                                    unfocusedTextColor = EditorialTextPrimary
                                )
                            )
                        }
                    }

                    // Año Modelo
                    OutlinedTextField(
                        value = yearText,
                        onValueChange = { yearText = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Año / Modelo") },
                        placeholder = { Text("ej. 2014", color = EditorialTextTertiary) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 2: ODÓMETRO INICIAL
            SectionTitle(title = "2. KILOMETRAJE INICIAL", icon = Icons.Filled.Speed)
            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EditorialSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = mileageText,
                        onValueChange = { mileageText = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Odómetro actual (km)") },
                        placeholder = { Text("ej. 12500", color = EditorialTextTertiary) },
                        trailingIcon = {
                            Text(
                                "km",
                                color = EditorialPrimary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(end = 14.dp)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(EditorialSurfaceSubtle, RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = null,
                            tint = EditorialPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "A partir de este kilometraje calcularemos tus próximos cambios de aceite, bujía, frenos y revisiones periódicas.",
                            color = EditorialTextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 3: BILLETERA DIGITAL (DOCUMENTOS PDF)
            SectionTitle(title = "3. BILLETERA DIGITAL (DOCUMENTOS PDF)", icon = Icons.Filled.Description)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Adjunta copias en PDF de tus documentos obligatorios para tenerlos siempre disponibles en carretera sin consumir datos.",
                color = EditorialTextSecondary,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EditorialSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DocumentUploadRow(
                        title = "SOAT (Seguro Obligatorio)",
                        icon = Icons.Filled.Shield,
                        fileName = soatName,
                        onPickFile = { soatPicker.launch("application/pdf") }
                    )
                    HorizontalDivider(color = EditorialDivider)

                    DocumentUploadRow(
                        title = "Tarjeta de Propiedad",
                        icon = Icons.Filled.CreditCard,
                        fileName = tarjetaName,
                        onPickFile = { tarjetaPicker.launch("application/pdf") }
                    )
                    HorizontalDivider(color = EditorialDivider)

                    DocumentUploadRow(
                        title = "Licencia de Conducción",
                        icon = Icons.Filled.Badge,
                        fileName = licenciaName,
                        onPickFile = { licenciaPicker.launch("application/pdf") }
                    )
                    HorizontalDivider(color = EditorialDivider)

                    DocumentUploadRow(
                        title = "Cédula de Ciudadanía",
                        icon = Icons.Filled.Person,
                        fileName = cedulaName,
                        onPickFile = { cedulaPicker.launch("application/pdf") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 4: DATOS DEL PROPIETARIO (OPCIONAL)
            SectionTitle(title = "4. PROPIETARIO (OPCIONAL)", icon = Icons.Filled.Person)
            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EditorialSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    OutlinedTextField(
                        value = ownerName,
                        onValueChange = { ownerName = it },
                        label = { Text("Nombre del Propietario") },
                        placeholder = { Text("ej. Nombre y Apellidos", color = EditorialTextTertiary) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
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

                    OutlinedTextField(
                        value = ownerId,
                        onValueChange = { ownerId = it },
                        label = { Text("Cédula / Documento de Identidad") },
                        placeholder = { Text("ej. 1020304050", color = EditorialTextTertiary) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
            }

            Spacer(modifier = Modifier.height(30.dp))

            // CTA Button
            Button(
                onClick = { onFinishOnboarding() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EditorialPrimary,
                    contentColor = DarkBackground
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "¡Comenzar a Rodar!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Skip button
            TextButton(
                onClick = { onFinishOnboarding() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Configurar después (Comenzar en blanco)",
                    color = EditorialTextTertiary,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun SectionTitle(title: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = EditorialPrimary,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = title,
            color = EditorialPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
        )
    }
}

@Composable
private fun DocumentUploadRow(
    title: String,
    icon: ImageVector,
    fileName: String?,
    onPickFile: () -> Unit
) {
    val isAttached = fileName != null

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isAttached) EditorialContainerPill else EditorialSurfaceSubtle),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isAttached) Icons.Filled.CheckCircle else icon,
                    contentDescription = null,
                    tint = if (isAttached) EditorialPrimary else EditorialTextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = EditorialTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (isAttached) fileName!! else "Pendiente de cargar",
                    color = if (isAttached) EditorialPrimary else EditorialTextTertiary,
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (isAttached) EditorialSurfaceSubtle else EditorialContainerPill,
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isAttached) EditorialBorder else EditorialPrimary
            ),
            modifier = Modifier.clickable { onPickFile() }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.UploadFile,
                    contentDescription = null,
                    tint = if (isAttached) EditorialTextSecondary else EditorialPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = if (isAttached) "Cambiar" else "Adjuntar",
                    color = if (isAttached) EditorialTextSecondary else EditorialPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun getFileNameFromUri(context: Context, uri: Uri): String {
    var name = ""
    try {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    name = it.getString(nameIndex)
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return if (name.isNotBlank()) name else (uri.lastPathSegment ?: "documento.pdf")
}
