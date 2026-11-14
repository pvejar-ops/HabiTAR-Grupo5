package com.example.habitar.features.autoconocimiento

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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class EstadoSeccion {
    COMPLETA, EN_CURSO, POR_EMPEZAR
}

data class ItemSeccion(
    val id: Int,
    val titulo: String,
    val subtitulo: String,
    val iconoSimbolo: String,
    val iconoFondo: Color,
    val iconoColor: Color,
    val estado: EstadoSeccion,
    val textoEstado: String,
    val habilitada: Boolean = true
)

private val MoradoHeader = Color(0xFF5E72E4)
private val MoradoPrimario = Color(0xFF6C63FF)
private val FondoPantalla = Color(0xFFF8F9FE)
private val TextoNegro = Color(0xFF1C1B1F)
private val TextoSecundario = Color(0xFF49454F)
private val GrisApoyo = Color(0xFF798E9C)

@Composable
fun HubSeccionesScreen(
    viewModel: AutoconocimientoViewModel,
    onNavigateBack: () -> Unit,
    onSelectSeccion: (Int) -> Unit,
    onNavigateToApoyoInmediato: () -> Unit,
    onVerOrientacion: () -> Unit
) {
    val context = LocalContext.current
    var mostrarDialogoReiniciar by remember { mutableStateOf(false) }

    // 1. Estado real de la Sección 1
    val respondidasCatq = viewModel.obtenerCantidadRespondidas(1)
    val catqCompleta = viewModel.estaSeccionCompletada(1) || respondidasCatq >= 25
    val estado1 = when {
        catqCompleta -> EstadoSeccion.COMPLETA
        respondidasCatq > 0 -> EstadoSeccion.EN_CURSO
        else -> EstadoSeccion.POR_EMPEZAR
    }
    val textoEstado1 = when (estado1) {
        EstadoSeccion.COMPLETA -> "Completa"
        EstadoSeccion.EN_CURSO -> "En curso · $respondidasCatq/25"
        EstadoSeccion.POR_EMPEZAR -> "Por empezar"
    }

    // 2. Estado real de la Sección 2
    val respondidasRbq = viewModel.obtenerCantidadRespondidas(2)
    val rbqCompleta = viewModel.estaSeccionCompletada(2) || respondidasRbq >= 20
    val estado2 = when {
        rbqCompleta -> EstadoSeccion.COMPLETA
        respondidasRbq > 0 -> EstadoSeccion.EN_CURSO
        else -> EstadoSeccion.POR_EMPEZAR
    }
    val textoEstado2 = when (estado2) {
        EstadoSeccion.COMPLETA -> "Completa"
        EstadoSeccion.EN_CURSO -> "En curso · $respondidasRbq/20"
        EstadoSeccion.POR_EMPEZAR -> "Por empezar"
    }

    val ambasCompletas = catqCompleta && rbqCompleta

    val secciones = listOf(
        ItemSeccion(
            id = 1,
            titulo = "Cómo me relaciono",
            subtitulo = "Comunicación y situaciones sociales",
            iconoSimbolo = "👥",
            iconoFondo = Color(0xFFE8F0FE),
            iconoColor = MoradoHeader,
            estado = estado1,
            textoEstado = textoEstado1,
            habilitada = true
        ),
        ItemSeccion(
            id = 2,
            titulo = "Rutinas e intereses",
            subtitulo = "Hábitos, cambios y temas favoritos",
            iconoSimbolo = "📋",
            iconoFondo = Color(0xFFF3E8FF),
            iconoColor = Color(0xFF9333EA),
            estado = estado2,
            textoEstado = textoEstado2,
            habilitada = true
        ),
        ItemSeccion(
            id = 3,
            titulo = "Cómo percibo las cosas",
            subtitulo = "Detalles, patrones y atención",
            iconoSimbolo = "🔍",
            iconoFondo = Color(0xFFE0F2FE),
            iconoColor = Color(0xFF0284C7),
            estado = EstadoSeccion.POR_EMPEZAR,
            textoEstado = "Por empezar",
            habilitada = false
        ),
        ItemSeccion(
            id = 4,
            titulo = "Mi experiencia cotidiana",
            subtitulo = "Sensaciones y vivencias personales",
            iconoSimbolo = "🏠",
            iconoFondo = Color(0xFFFEF3C7),
            iconoColor = Color(0xFFD97706),
            estado = EstadoSeccion.POR_EMPEZAR,
            textoEstado = "Por empezar",
            habilitada = false
        )
    )

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

            // Cabecera Cápsula "Tu recorrido"
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
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "Tu recorrido",
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

            Spacer(modifier = Modifier.height(20.dp))

            // Subtítulo
            Text(
                text = "Completa las secciones para ver tu orientación. Puedes hacerlas en el orden que quieras y pausar cuando lo necesites.",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = TextoSecundario,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Barra de progreso dinámica
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val colorSeg1 = when {
                    catqCompleta -> MoradoPrimario
                    respondidasCatq > 0 -> MoradoHeader
                    else -> Color(0xFFE2E4F0)
                }
                val colorSeg2 = when {
                    rbqCompleta -> MoradoPrimario
                    respondidasRbq > 0 -> MoradoHeader
                    else -> Color(0xFFE2E4F0)
                }

                Box(modifier = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(3.dp)).background(colorSeg1))
                Box(modifier = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(3.dp)).background(colorSeg2))
                Box(modifier = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(3.dp)).background(Color(0xFFE2E4F0)))
                Box(modifier = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(3.dp)).background(Color(0xFFE2E4F0)))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Las 4 Tarjetas de sección
            secciones.forEach { seccion ->
                TarjetaHubSeccion(
                    seccion = seccion,
                    onClick = {
                        if (seccion.habilitada) {
                            onSelectSeccion(seccion.id)
                        } else {
                            Toast.makeText(context, "Esta sección corresponde a otra etapa del proyecto", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Botón Dinámico Inteligente
            val textoBoton = when {
                ambasCompletas -> "VER TU ORIENTACIÓN"
                !catqCompleta -> if (respondidasCatq > 0) "CONTINUAR SECCIÓN" else "COMENZAR SECCIÓN"
                else -> if (respondidasRbq > 0) "CONTINUAR SECCIÓN" else "COMENZAR SECCIÓN"
            }

            Button(
                onClick = {
                    when {
                        ambasCompletas -> onVerOrientacion()
                        !catqCompleta -> onSelectSeccion(1)
                        else -> onSelectSeccion(2)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MoradoPrimario),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = textoBoton,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Enlace de Apoyo Inmediato (Lleva a S17)
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "¿Necesitas apoyo ahora?",
                    color = MoradoPrimario,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onNavigateToApoyoInmediato() }
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            // Enlace para Reiniciar Cuestionario (Ley 21.719)
            if (respondidasCatq > 0 || respondidasRbq > 0 || catqCompleta || rbqCompleta) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Reiniciar cuestionario y borrar respuestas",
                        color = Color(0xFFE53935),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { mostrarDialogoReiniciar = true }
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Diálogo de Confirmación para borrar
        if (mostrarDialogoReiniciar) {
            AlertDialog(
                onDismissRequest = { mostrarDialogoReiniciar = false },
                title = { Text(text = "¿Reiniciar cuestionario?", fontWeight = FontWeight.Bold) },
                text = {
                    Text(
                        text = "Se borrarán todas las respuestas guardadas en tu teléfono (cumpliendo la Ley 21.719 de privacidad) para que puedas comenzar de cero.",
                        fontSize = 14.sp,
                        color = TextoSecundario
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.reiniciarProgreso()
                            mostrarDialogoReiniciar = false
                            Toast.makeText(context, "Respuestas borradas. Puedes comenzar de nuevo.", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
                    ) {
                        Text(text = "Sí, borrar", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogoReiniciar = false }) {
                        Text(text = "Cancelar", color = GrisApoyo)
                    }
                }
            )
        }
    }
}

@Composable
private fun TarjetaHubSeccion(
    seccion: ItemSeccion,
    onClick: () -> Unit
) {
    val (badgeFondo, badgeTextoColor) = when (seccion.estado) {
        EstadoSeccion.COMPLETA -> Color(0xFFE0F2E9) to Color(0xFF2E5A44)
        EstadoSeccion.EN_CURSO -> Color(0xFFE6E9FC) to MoradoPrimario
        EstadoSeccion.POR_EMPEZAR -> Color(0xFFF0F1F5) to GrisApoyo
    }

    val alphaCard = if (seccion.habilitada) 1f else 0.75f

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = Color.White.copy(alpha = alphaCard),
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
                    .background(seccion.iconoFondo),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = seccion.iconoSimbolo,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = seccion.titulo,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoNegro
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = seccion.subtitulo,
                    fontSize = 13.sp,
                    color = GrisApoyo
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(badgeFondo)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = seccion.textoEstado,
                    color = badgeTextoColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}