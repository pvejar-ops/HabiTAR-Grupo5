package com.example.habitar.features.autoconocimiento

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color as AndroidColor
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val MoradoHeader = Color(0xFF5E72E4)
private val MoradoPrimario = Color(0xFF6C63FF)
private val FondoPantalla = Color(0xFFF8F9FE)
private val TextoNegro = Color(0xFF1C1B1F)
private val TextoSecundario = Color(0xFF49454F)
private val GrisApoyo = Color(0xFF6B7280)

@Composable
fun ProximosPasosScreen(
    onVolverClick: () -> Unit,
    onVolverAlMenuClick: () -> Unit,
    onIrAHerramientasClick: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        containerColor = FondoPantalla
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Cabecera Cápsula "Próximos pasos"
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MoradoHeader,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onVolverClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "Próximos pasos",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 48.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Si quieres profundizar",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextoNegro
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Esta orientación puede ser un buen punto de partida para conversar con alguien de apoyo.",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = TextoSecundario,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            TarjetaApoyo(
                icono = "✚",
                iconoFondo = Color(0xFFEDF0FA),
                iconoColor = MoradoHeader,
                titulo = "Servicios de apoyo de tu universidad",
                subtitulo = "Atención psicológica y orientación estudiantil (DAE / SEMDA)"
            )

            Spacer(modifier = Modifier.height(14.dp))

            TarjetaApoyo(
                icono = "⚕",
                iconoFondo = Color(0xFFF0EEFA),
                iconoColor = MoradoPrimario,
                titulo = "Profesional de salud mental",
                subtitulo = "Puedes compartir este informe orientativo en tu consulta"
            )

            Spacer(modifier = Modifier.height(14.dp))

            TarjetaApoyo(
                icono = "❀",
                iconoFondo = Color(0xFFE0F2E9),
                iconoColor = Color(0xFF3B7A57),
                titulo = "Herramientas de HabiTAR",
                subtitulo = "Meditación, Sensaciones y Organiza tu Día",
                onClick = onIrAHerramientasClick
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Botón: DESCARGAR MI INFORME (Crea PDF real en el almacenamiento)
            Button(
                onClick = {
                    generarYDescargarPdfReal(context)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MoradoPrimario),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Text(
                    text = "DESCARGAR MI INFORME (PDF)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Volver al menú",
                    color = MoradoPrimario,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onVolverAlMenuClick() }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// Función nativa para generar el documento PDF real
private fun generarYDescargarPdfReal(context: Context) {
    try {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // Formato A4
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        val paintTitulo = Paint().apply {
            color = AndroidColor.parseColor("#5E72E4")
            textSize = 18f
            isFakeBoldText = true
        }
        val paintSubtitulo = Paint().apply {
            color = AndroidColor.parseColor("#1C1B1F")
            textSize = 13f
            isFakeBoldText = true
        }
        val paintTexto = Paint().apply {
            color = AndroidColor.parseColor("#49454F")
            textSize = 10f
        }
        val paintLinea = Paint().apply {
            color = AndroidColor.parseColor("#E0E0E0")
            strokeWidth = 1f
        }
        val paintAlerta = Paint().apply {
            color = AndroidColor.parseColor("#2E5A44")
            textSize = 10f
            isFakeBoldText = true
        }

        var y = 50f

        // Encabezado
        canvas.drawText("HabiTAR — Plataforma de Apoyo Estudiantil", 40f, y, paintTitulo)
        y += 20f
        canvas.drawText("Informe Orientativo de Autoconocimiento (Screening TEA)", 40f, y, paintSubtitulo)
        y += 15f
        canvas.drawLine(40f, y, 555f, y, paintLinea)
        y += 25f

        val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        canvas.drawText("Fecha de emisión: $fecha", 40f, y, paintTexto)
        y += 16f
        canvas.drawText("Participante: Anónimo (Ley N° 21.719 de Neurodivergencia)", 40f, y, paintTexto)
        y += 16f
        canvas.drawText("Finalidad: Autoconocimiento y derivación oportuna (CERO DIAGNÓSTICO)", 40f, y, paintTexto)
        y += 24f

        // Sección 1: Resultados
        canvas.drawText("1. DIMENSIONES EVALUADAS (Instrumentos Clínicos)", 40f, y, paintSubtitulo)
        y += 18f
        canvas.drawText("• Comunicación y Adaptación Social (CAT-Q, Hull et al., 2019):", 40f, y, paintTexto)
        y += 14f
        canvas.drawText("   - Esfuerzo de adaptación en situaciones sociales: Evaluado", 40f, y, paintTexto)
        y += 14f
        canvas.drawText("   - Monitoreo consciente de gestos y expresiones (Camuflaje): Evaluado", 40f, y, paintTexto)
        y += 18f
        canvas.drawText("• Rutinas, Intereses y Procesamiento Sensorial (RBQ-2A, Barrett et al.):", 40f, y, paintTexto)
        y += 14f
        canvas.drawText("   - Preferencia por predictibilidad y entornos estructurados: Evaluado", 40f, y, paintTexto)
        y += 14f
        canvas.drawText("   - Respuestas a estímulos sensoriales y conductas repetitivas: Evaluado", 40f, y, paintTexto)
        y += 28f

        // Sección 2: Marco Ético
        canvas.drawText("2. ALCANCE ÉTICO Y LEGAL", 40f, y, paintSubtitulo)
        y += 18f
        canvas.drawText("Este informe resume patrones autorreportados y no constituye diagnóstico médico ni clínico.", 40f, y, paintAlerta)
        y += 14f
        canvas.drawText("Si estos resultados reflejan dificultades cotidianas, se aconseja consultar con un profesional", 40f, y, paintTexto)
        y += 14f
        canvas.drawText("de salud mental o acudir a la Dirección de Asuntos Estudiantiles (DAE / SEMDA) de tu sede.", 40f, y, paintTexto)
        y += 28f

        // Sección 3: Contactos
        canvas.drawText("3. LÍNEAS DE CONTENCIÓN Y ASISTENCIA (CHILE)", 40f, y, paintSubtitulo)
        y += 18f
        canvas.drawText("• Línea Prevención del Suicidio MINSAL: *4141 (Gratis, confidencial, 24/7)", 40f, y, paintTexto)
        y += 14f
        canvas.drawText("• Fono Salud Responde: 600 360 7777", 40f, y, paintTexto)
        y += 14f
        canvas.drawText("• Servicio de Urgencias Médicas (SAMU): 131", 40f, y, paintTexto)

        pdfDocument.finishPage(page)

        val nombreArchivo = "Informe_Autoconocimiento_HabiTAR_${System.currentTimeMillis()}.pdf"
        var archivoUri: Uri? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, nombreArchivo)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            if (uri != null) {
                resolver.openOutputStream(uri)?.use { out ->
                    pdfDocument.writeTo(out)
                }
                archivoUri = uri
            }
        } else {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, nombreArchivo)
            FileOutputStream(file).use { out ->
                pdfDocument.writeTo(out)
            }
            archivoUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        }

        pdfDocument.close()
        Toast.makeText(context, "¡PDF guardado en la carpeta Descargas!", Toast.LENGTH_LONG).show()

        // Abrir el visor de PDF
        archivoUri?.let { uri ->
            val viewIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            try {
                context.startActivity(viewIntent)
            } catch (_: Exception) {
                // Si no hay lector predeterminado, no revienta
            }
        }
    } catch (e: Exception) {
        Toast.makeText(context, "Error al generar PDF: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

@Composable
private fun TarjetaApoyo(
    icono: String,
    iconoFondo: Color,
    iconoColor: Color,
    titulo: String,
    subtitulo: String,
    onClick: (() -> Unit)? = null
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconoFondo),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icono,
                    fontSize = 20.sp,
                    color = iconoColor,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoNegro
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = subtitulo,
                    fontSize = 13.sp,
                    color = GrisApoyo,
                    lineHeight = 18.sp
                )
            }
        }
    }
}