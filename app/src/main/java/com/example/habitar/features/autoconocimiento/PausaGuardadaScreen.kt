package com.example.habitar.features.autoconocimiento

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val MoradoPrimario = Color(0xFF6C63FF)
private val MoradoHeader = Color(0xFF5E72E4)
private val VerdeSalviaFondo = Color(0xFFE2EFE9)
private val VerdeSalviaIcono = Color(0xFF4A7C59)
private val FondoPantalla = Color(0xFFF8F9FE)
private val TextoNegro = Color(0xFF1C1B1F)
private val TextoSecundario = Color(0xFF49454F)

@Composable
fun PausaGuardadaScreen(
    idSeccion: Int,
    respondidas: Int,
    totalPreguntas: Int,
    onVolverMenuClick: () -> Unit,
    onSeguirRespondiendoClick: () -> Unit
) {
    val nombreSeccion = when (idSeccion) {
        1 -> "Cómo me relaciono"
        2 -> "Rutinas e intereses"
        3 -> "Sensibilidad sensorial"
        else -> "Comunicación"
    }

    val siguientePregunta = (respondidas + 1).coerceAtMost(totalPreguntas)

    val mensajeProgreso = if (respondidas >= totalPreguntas) {
        "Completaste las $totalPreguntas preguntas de “$nombreSeccion”. Puedes revisar tus respuestas o confirmar cuando gustes."
    } else {
        "Respondiste $respondidas de $totalPreguntas preguntas de “$nombreSeccion”. Cuando vuelvas, seguirás desde la pregunta $siguientePregunta."
    }

    Scaffold(
        containerColor = FondoPantalla
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Cabecera Cápsula "Autoconocimiento"
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
                    IconButton(onClick = onVolverMenuClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "Autoconocimiento",
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

            Spacer(modifier = Modifier.weight(1f))

            // Ícono Verde Salvia con Checkmark
            Box(
                modifier = Modifier
                    .size(104.dp)
                    .clip(CircleShape)
                    .background(VerdeSalviaFondo),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Guardado con éxito",
                    tint = VerdeSalviaIcono,
                    modifier = Modifier.size(54.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Título
            Text(
                text = "Tu avance quedó guardado",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextoNegro,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Descripción dinámica
            Text(
                text = mensajeProgreso,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = TextoSecundario,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1.2f))

            // Botón Píldora: VOLVER AL MENÚ
            Button(
                onClick = onVolverMenuClick,
                colors = ButtonDefaults.buttonColors(containerColor = MoradoPrimario),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Text(
                    text = "VOLVER AL MENÚ",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón secundario: Seguir respondiendo
            Text(
                text = "Seguir respondiendo",
                color = MoradoPrimario,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onSeguirRespondiendoClick() }
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}