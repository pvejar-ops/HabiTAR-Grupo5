package com.example.habitar.features.autoconocimiento

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val MoradoHeader = Color(0xFF5E72E4)
private val MoradoPrimario = Color(0xFF6C63FF)
private val FondoPantalla = Color(0xFFF8F9FE)
private val TextoNegro = Color(0xFF1C1B1F)
private val TextoSecundario = Color(0xFF49454F)
private val GrisApoyo = Color(0xFF798E9C)
private val GrisBordeChip = Color(0xFFDCDCE5)

@Composable
fun PreguntasSugeridasScreen(
    onVolverClick: () -> Unit,
    onSeleccionarPregunta: (idPregunta: String, textoPregunta: String) -> Unit
) {
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

            // Cabecera Cápsula "Explora tu perfil"
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
                        text = "Explora tu perfil",
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

            // Texto explicativo
            Text(
                text = "Elige un tema y te daremos ideas basadas en tus respuestas y en información confiable.",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = TextoSecundario,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Bloque 1: En lo social
            Text(
                text = "En lo social",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextoNegro
            )
            Spacer(modifier = Modifier.height(10.dp))

            ChipPregunta(
                texto = "¿Cómo prepararme para situaciones sociales?",
                onClick = { onSeleccionarPregunta("social_1", "¿Cómo prepararme para situaciones sociales?") }
            )
            Spacer(modifier = Modifier.height(10.dp))

            ChipPregunta(
                texto = "¿Cómo comunicar cómo me siento?",
                onClick = { onSeleccionarPregunta("social_2", "¿Cómo comunicar cómo me siento?") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Bloque 2: Rutinas y cambios
            Text(
                text = "Rutinas y cambios",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextoNegro
            )
            Spacer(modifier = Modifier.height(10.dp))

            ChipPregunta(
                texto = "¿Cómo anticipar cambios en mi semana?",
                onClick = { onSeleccionarPregunta("rutinas_1", "¿Cómo anticipar cambios en mi semana?") }
            )
            Spacer(modifier = Modifier.height(10.dp))

            ChipPregunta(
                texto = "¿Cómo organizarme en semanas de pruebas?",
                onClick = { onSeleccionarPregunta("rutinas_2", "¿Cómo organizarme en semanas de pruebas?") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Bloque 3: Energía y calma
            Text(
                text = "Energía y calma",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextoNegro
            )
            Spacer(modifier = Modifier.height(10.dp))

            ChipPregunta(
                texto = "¿Qué hacer cuando me siento sobrecargado/a?",
                destacado = true,
                onClick = { onSeleccionarPregunta("energia_1", "¿Qué hacer cuando me siento sobrecargado/a?") }
            )
            Spacer(modifier = Modifier.height(10.dp))

            ChipPregunta(
                texto = "¿Cómo recuperar energía tras un día intenso?",
                onClick = { onSeleccionarPregunta("energia_2", "¿Cómo recuperar energía tras un día intenso?") }
            )
            Spacer(modifier = Modifier.height(10.dp))

            ChipPregunta(
                texto = "¿Qué estímulos podrían estar afectándome?",
                onClick = { onSeleccionarPregunta("energia_3", "¿Qué estímulos podrían estar afectándome?") }
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Nota al pie
            Text(
                text = "No es un chat abierto: solo puedes elegir entre estos temas.",
                fontSize = 13.sp,
                color = GrisApoyo,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ChipPregunta(
    texto: String,
    destacado: Boolean = false,
    onClick: () -> Unit
) {
    val colorBorde = if (destacado) MoradoPrimario else GrisBordeChip
    val grosorBorde = if (destacado) 1.5.dp else 1.dp
    val pesoTexto = if (destacado) FontWeight.SemiBold else FontWeight.Normal

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = BorderStroke(grosorBorde, colorBorde)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = texto,
                fontSize = 14.sp,
                fontWeight = pesoTexto,
                color = if (destacado) MoradoPrimario else TextoNegro
            )
        }
    }
}