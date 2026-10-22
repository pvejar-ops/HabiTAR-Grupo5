package com.example.habitar.features.historial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.viewinterop.AndroidView

val colorEmociones = Color(0xFFD671F5)
val colorSensaciones = Color(0xFF928AF0)
val colorEmergencia = Color(0xFFF4B3A3)
val colorMeditacion = Color(0xFF4DB6AC)

@Composable
fun HistorialScreen(
    onNavigateBack: () -> Unit,
    viewModel: HistorialViewModel = viewModel()
) {
    val diaSeleccionado = viewModel.diaSeleccionado

    LazyColumn(  // 👈 cambia Column por LazyColumn
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentPadding = PaddingValues(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Botón volver
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .background(color = Color(0xFFF3EDF7), shape = CircleShape)
                        .size(48.dp)
                        .align(Alignment.CenterStart) // 👈 funciona dentro de Box
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color(0xFF6750A4)
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Mi Historial",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF6750A4), CircleShape)
                    .padding(vertical = 12.dp),
                textAlign = TextAlign.Center
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                LeyendaItem("Emociones", colorEmociones)
                LeyendaItem("Sensaciones", colorSensaciones)
                LeyendaItem("Emergencia", colorEmergencia)
            }
        }

        if (viewModel.estaCargando) {
            item {
                CircularProgressIndicator(modifier = Modifier.padding(top = 32.dp))
            }
        } else if (viewModel.resumenDias.isEmpty()) {
            item {
                Text(
                    text = "Aún no tienes registros en tu historial.",
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(top = 32.dp)
                )
            }
        } else {
            items(viewModel.resumenDias.entries.sortedByDescending { it.key }.toList()) { (fecha, resumen) ->
                TarjetaDia(
                    fecha = fecha,
                    resumen = resumen,
                    onClick = { viewModel.seleccionarDia(fecha) }
                )
            }
        }
    }

    // Dialog al tocar un día
    diaSeleccionado?.let { dia ->
        Dialog(onDismissRequest = { viewModel.limpiarSeleccion() }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Título
                    Text(
                        "Resumen del ${dia.fecha}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Contenido con scroll
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (dia.emociones.isNotEmpty()) {
                            SeccionResumen("Emociones", colorEmociones) {
                                dia.emociones.forEach { reg ->
                                    reg.emociones?.joinToString()?.let {
                                        Text(
                                            "• $it",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    reg.respuesta_ia?.let { respuesta ->
                                        var expandido by remember { mutableStateOf(false) }
                                        val textoMostrar = if (expandido) respuesta
                                        else respuesta.take(80) + if (respuesta.length > 80) "..." else ""
                                        AndroidView(
                                            factory = { context ->
                                                android.widget.TextView(context).also { tv ->
                                                    io.noties.markwon.Markwon.create(context)
                                                        .setMarkdown(tv, textoMostrar)
                                                    tv.textSize = 12f
                                                    tv.setTextColor(android.graphics.Color.GRAY)
                                                }
                                            },
                                            update = { tv ->
                                                io.noties.markwon.Markwon.create(tv.context)
                                                    .setMarkdown(tv, textoMostrar)
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        if (respuesta.length > 80) {
                                            Text(
                                                text = if (expandido) "Ver menos" else "Ver más",
                                                fontSize = 12.sp,
                                                color = Color(0xFF6750A4),
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.clickable {
                                                    expandido = !expandido
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        if (dia.sensaciones.isNotEmpty()) {
                            SeccionResumen("Sensaciones", colorSensaciones) {
                                dia.sensaciones.forEach { reg ->
                                    reg.sensaciones?.joinToString()?.let {
                                        Text(
                                            "• $it",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    reg.respuesta_ia?.let { respuesta ->
                                        var expandido by remember { mutableStateOf(false) }
                                        val textoMostrar = if (expandido) respuesta
                                        else respuesta.take(80) + if (respuesta.length > 80) "..." else ""
                                        AndroidView(
                                            factory = { context ->
                                                android.widget.TextView(context).also { tv ->
                                                    io.noties.markwon.Markwon.create(context)
                                                        .setMarkdown(tv, textoMostrar)
                                                    tv.textSize = 12f
                                                    tv.setTextColor(android.graphics.Color.GRAY)
                                                }
                                            },
                                            update = { tv ->
                                                io.noties.markwon.Markwon.create(tv.context)
                                                    .setMarkdown(tv, textoMostrar)
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        if (respuesta.length > 80) {
                                            Text(
                                                text = if (expandido) "Ver menos" else "Ver más",
                                                fontSize = 12.sp,
                                                color = Color(0xFF6750A4),
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.clickable {
                                                    expandido = !expandido
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        if (dia.emergencias.isNotEmpty()) {
                            SeccionResumen("Emergencia", colorEmergencia) {
                                dia.emergencias.forEach { reg ->
                                    reg.lugar?.let {
                                        Text(
                                            "• $it",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    reg.respuesta_ia?.let { respuesta ->
                                        var expandido by remember { mutableStateOf(false) }
                                        val textoMostrar = if (expandido) respuesta
                                        else respuesta.take(80) + if (respuesta.length > 80) "..." else ""
                                        AndroidView(
                                            factory = { context ->
                                                android.widget.TextView(context).also { tv ->
                                                    io.noties.markwon.Markwon.create(context)
                                                        .setMarkdown(tv, textoMostrar)
                                                    tv.textSize = 12f
                                                    tv.setTextColor(android.graphics.Color.GRAY)
                                                }
                                            },
                                            update = { tv ->
                                                io.noties.markwon.Markwon.create(tv.context)
                                                    .setMarkdown(tv, textoMostrar)
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        if (respuesta.length > 80) {
                                            Text(
                                                text = if (expandido) "Ver menos" else "Ver más",
                                                fontSize = 12.sp,
                                                color = Color(0xFF6750A4),
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.clickable {
                                                    expandido = !expandido
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        if (dia.meditaciones.isNotEmpty()) {
                            SeccionResumen("Meditación", colorMeditacion) {
                                Text("• Sesión de meditación realizada", fontSize = 13.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Botón cerrar
                    TextButton(
                        onClick = { viewModel.limpiarSeleccion() },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Cerrar", color = Color(0xFF6750A4))
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaDia(fecha: String, resumen: ResumenDia, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(fecha, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                if (resumen.emociones.isNotEmpty())
                    PuntoModulo(colorEmociones)
                if (resumen.sensaciones.isNotEmpty())
                    PuntoModulo(colorSensaciones)
                if (resumen.emergencias.isNotEmpty())
                    PuntoModulo(colorEmergencia)
                if (resumen.meditaciones.isNotEmpty())
                    PuntoModulo(colorMeditacion)
            }
        }
    }
}

@Composable
fun PuntoModulo(color: Color) {
    Box(
        modifier = Modifier
            .size(12.dp)
            .background(color, CircleShape)
    )
}

@Composable
fun LeyendaItem(texto: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(10.dp).background(color, CircleShape))
        Spacer(modifier = Modifier.width(4.dp))
        Text(texto, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun SeccionResumen(titulo: String, color: Color, contenido: @Composable () -> Unit) {
    Column {
        Text(titulo, fontWeight = FontWeight.Bold, color = color, fontSize = 14.sp)
        contenido()
    }
}