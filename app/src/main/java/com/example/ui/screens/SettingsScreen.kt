package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import coil.compose.AsyncImage
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.EditorialBadge
import com.example.ui.components.MotoBackdropBackground
import com.example.ui.components.MotoBottomNavigationBar
import com.example.ui.components.MotoTopHeaderBar
import com.example.ui.theme.EditorialBackground
import com.example.ui.theme.EditorialBorder
import com.example.ui.theme.EditorialContainer
import com.example.ui.theme.EditorialContainerPill
import com.example.ui.theme.EditorialDivider
import com.example.ui.theme.EditorialHighlightRose
import com.example.ui.theme.EditorialHighlightRoseText
import com.example.ui.theme.EditorialPrimary
import com.example.ui.theme.EditorialPurpleAccent
import com.example.ui.theme.EditorialSurface
import com.example.ui.theme.EditorialSurfaceSubtle
import com.example.ui.theme.EditorialTextPrimary
import com.example.ui.theme.EditorialTextSecondary
import com.example.ui.theme.EditorialTextTertiary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MotoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@Composable
fun SettingsScreen(
    viewModel: MotoViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val moto by viewModel.motorcycle.collectAsState()
    val documents by viewModel.documents.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()

    // File Picker for Backup Restore
    val restoreLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            scope.launch(Dispatchers.IO) {
                try {
                    val content = context.contentResolver.openInputStream(uri)?.use {
                        it.bufferedReader().readText()
                    }
                    if (content != null) {
                        val success = viewModel.restoreBackupJson(content)
                        withContext(Dispatchers.Main) {
                            if (success) {
                                Toast.makeText(context, "Copia de seguridad restaurada con éxito", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(context, "Error al leer el archivo de copia", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Error al restaurar: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("settings_screen"),
        containerColor = EditorialBackground,
        bottomBar = {
            MotoBottomNavigationBar(
                currentScreen = currentScreen,
                onNavigate = { viewModel.navigateTo(it) }
            )
        }
    ) { innerPadding ->
        MotoBackdropBackground(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            alpha = 0.05f
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 22.dp, vertical = 12.dp)
            ) {
                // Top Header Bar
                MotoTopHeaderBar(
                    title = "Configuración",
                    showBackButton = true,
                    onBackClick = { viewModel.navigateTo(AppScreen.INICIO) },
                    showSettings = false
                )

                Spacer(modifier = Modifier.height(20.dp))

                // SECCIÓN 1: DATOS DEL VEHÍCULO
                Text(
                    text = "DATOS DE LA MOTOCICLETA",
                    color = EditorialPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 1.2.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = EditorialSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(EditorialContainerPill),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.TwoWheeler,
                                        contentDescription = null,
                                        tint = EditorialPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(
                                        text = "${moto?.brand ?: "Honda"} ${moto?.model ?: "CBF-125"}",
                                        color = EditorialTextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    Text(
                                        text = "Placa: ${moto?.licensePlate?.takeIf { it.isNotBlank() } ?: "Por registrar"} · ${moto?.color?.takeIf { it.isNotBlank() } ?: "Color por definir"}",
                                        color = EditorialTextSecondary,
                                        fontSize = 13.sp
                                    )
                                }
                            }

                            EditorialBadge(
                                text = "${moto?.year ?: 2014}",
                                backgroundColor = EditorialContainerPill,
                                textColor = EditorialPrimary
                            )
                        }

                        HorizontalDivider(color = EditorialDivider)

                        Button(
                            onClick = { viewModel.showEditMotoDialog.value = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EditorialPrimary,
                                contentColor = Color(0xFF0F1704)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = Color(0xFF0F1704)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Editar Datos de la Moto",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F1704)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // SECCIÓN 2: ADMINISTRAR DOCUMENTOS (PDFs & FECHAS)
                Text(
                    text = "ADMINISTRAR DOCUMENTOS & PAPELES",
                    color = EditorialPurpleAccent,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 1.1.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = EditorialSurface),
                    border = BorderStroke(1.dp, EditorialBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        documents.forEachIndexed { index, doc ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.selectedDocumentToEdit.value = doc }
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(EditorialContainerPill),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Description,
                                            contentDescription = null,
                                            tint = EditorialPrimary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                        Text(
                                            text = doc.title,
                                            color = EditorialTextPrimary,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = "Vigencia: ${doc.validityDate} · ${if (doc.isCustom) "PDF Personalizado" else "PDF Original"}",
                                            color = EditorialTextTertiary,
                                            fontSize = 12.sp
                                        )
                                    }
                                }

                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                    contentDescription = "Cambiar o editar",
                                    tint = EditorialTextTertiary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }

                            if (index < documents.size - 1) {
                                HorizontalDivider(color = EditorialDivider, thickness = 0.8.dp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // SECCIÓN 3: COPIA DE SEGURIDAD (BACKUP)
                Text(
                    text = "COPIA DE SEGURIDAD (BACKUP)",
                    color = EditorialPurpleAccent,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 1.1.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = EditorialSurface),
                    border = BorderStroke(1.dp, EditorialBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Respaldo y Restauración",
                            color = EditorialTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Guarda tus datos de mantenimientos, kilometraje y gastos en un archivo para que nunca los pierdas al cambiar de dispositivo.",
                            color = EditorialTextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    val json = viewModel.exportBackupJson()
                                    try {
                                        val backupFile = File(context.cacheDir, "backup_mi_moto.json")
                                        FileOutputStream(backupFile).use { it.write(json.toByteArray()) }

                                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                            type = "application/json"
                                            putExtra(Intent.EXTRA_STREAM, androidx.core.content.FileProvider.getUriForFile(
                                                context,
                                                "${context.packageName}.fileprovider",
                                                backupFile
                                            ))
                                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                        }
                                        context.startActivity(Intent.createChooser(shareIntent, "Compartir o Guardar Copia de Seguridad"))
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Error exportando copia: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = EditorialPrimary,
                                    contentColor = Color(0xFF0F1704)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.CloudUpload,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = Color(0xFF0F1704)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Exportar",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F1704),
                                    fontSize = 13.sp
                                )
                            }

                            OutlinedButton(
                                onClick = { restoreLauncher.launch("application/json") },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = EditorialPrimary),
                                border = BorderStroke(1.dp, EditorialPrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.CloudDownload,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = EditorialPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Restaurar",
                                    fontWeight = FontWeight.Bold,
                                    color = EditorialPrimary,
                                    fontSize = 13.sp
                                )
                            }
                        }

                        HorizontalDivider(color = EditorialDivider)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Asistente de Primer Uso",
                                    color = EditorialTextPrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Volver a abrir el asistente inicial para registrar datos o documentos",
                                    color = EditorialTextTertiary,
                                    fontSize = 12.sp
                                )
                            }
                            OutlinedButton(
                                onClick = { viewModel.restartOnboarding() },
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, EditorialBorder)
                            ) {
                                Text("Abrir", color = EditorialPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                // SECCIÓN 4: HOMENAJE A MI PRIMERA MOTO & ACERCA DE
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("tribute_card"),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = EditorialSurface
                    ),
                    border = BorderStroke(1.dp, EditorialPrimary.copy(alpha = 0.5f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = null,
                                tint = EditorialHighlightRoseText,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "HOMENAJE A MI PRIMERA MOTO",
                                color = EditorialTextPrimary,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp,
                                letterSpacing = 0.8.sp
                            )
                        }

                        HorizontalDivider(color = EditorialDivider)

                        // Texto Conmemorativo Emotivo (sin placa)
                        Text(
                            text = "\"La primera moto nunca se olvida. Más que un medio de transporte, esta Honda CBF 125 representa el inicio del camino, la emoción de las primeras rutas, la libertad de rodar y la compañera fiel que despertó la pasión por las dos ruedas.\n\nEsta aplicación fue creada con dedicación y cariño para preservar cada kilómetro de su historia, sus mantenimientos y sus vivencias en el tiempo.\"",
                            color = EditorialTextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 19.sp,
                            fontWeight = FontWeight.Normal
                        )

                        HorizontalDivider(color = EditorialDivider)

                        // Creador con Foto de Perfil real
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEjO-Z9b7C9Ifw8ORwcqSROj7IXhx1pWvofIUG-0Q9n9757KxKjIfvdxSd1HHTg4pm0cPZOs-1X0SJbwJLqluOfLwhyphenhyphen0m7Erf3pIFB83aj1VcNHhTrOjvB1tw6_ls5DGjyFzrKMS4_UD7pK077FcUHekqo13mRRqCgNKLd5nxGWkKs7OslAF93daFQPfjIo/s1600/Edicion%20-%20Foto%20Perfil.png",
                                contentDescription = "Juan Sebastian Rodriguez Casas",
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, EditorialPrimary, CircleShape),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    text = "Juan Sebastian Rodriguez Casas",
                                    color = EditorialTextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "Desarrollador y Propietario",
                                    color = EditorialTextTertiary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        // Enlaces interactivos Blog y GitHub
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://sebastian-09781.blogspot.com/"))
                                    context.startActivity(intent)
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, EditorialBorder)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Language,
                                    contentDescription = null,
                                    tint = EditorialPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Blog",
                                    fontSize = 12.sp,
                                    color = EditorialTextPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            OutlinedButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/sebastian09781"))
                                    context.startActivity(intent)
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, EditorialBorder)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                    contentDescription = null,
                                    tint = EditorialPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "GitHub",
                                    fontSize = 12.sp,
                                    color = EditorialTextPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Versión de la app & Hecho con amor desde Colombia
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "My Honda · Versión 1.0",
                                color = EditorialTextTertiary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Hecho con ❤️ desde Colombia 🇨🇴",
                                color = EditorialTextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
