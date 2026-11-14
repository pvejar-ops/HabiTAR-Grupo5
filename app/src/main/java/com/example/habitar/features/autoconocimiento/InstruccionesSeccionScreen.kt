package com.example.habitar.features.autoconocimiento

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InstruccionesSeccionScreen(
    idSeccion: Int,
    onNavigateBack: () -> Unit,
    onStartSeccion: (Int) -> Unit
) {
    // Tokens de color oficiales
    val colorHeader = Color(0xFF5E72E4)
    val colorPrimario = Color(0xFF6C63FF)
    val colorFondo = Color(0xFFF8F9FE)
    val colorHero = Color(0xFFEEF0FC)
    val colorBordeCard = Color(0xFFE2E4F0)

    // Datos dinámicos según la sección elegida
    val (tituloHeader, tituloHero, iconoHero, opcionesEscala) = when (idSeccion) {
        1 -> Quadruple(
            "Cómo me relaciono",
            "Sobre cómo te relacionas",
            "😌",
            listOf(
                Pair(1, "Totalmente en desacuerdo"),
                Pair(4, "Punto neutro / término medio"),
                Pair(7, "Totalmente de acuerdo")
            )
        )
        else -> Quadruple(
            "Rutinas e intereses",
            "Sobre tus rutinas e intereses",
            "🔄",
            listOf(
                Pair(1, "Nunca o casi nunca"),
                Pair(2, "A veces"),
                Pair(3, "Con frecuencia")
            )
        )
    }

    Scaffold(
        containerColor = colorFondo,
        topBar = {
            // Cabecera Cápsula flotante con el nombre de la sección
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = colorHeader, shape = CircleShape)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { onNavigateBack() },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "←",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = tituloHeader,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f).padding(end = 36.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // --- 1. HERO BOX CON ÍCONO DE SECCIÓN ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(color = colorHero, shape = RoundedCornerShape(22.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = iconoHero,
                    fontSize = 52.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- 2. TÍTULO Y DESCRIPCIÓN ---
            Text(
                text = tituloHero,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1B26),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Leerás frases sobre cosas que algunas personas hacen o sienten. Elige qué tan seguido te pasan a ti.",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF4A4A58),
                lineHeight = 22.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // --- 3. TARJETA: CÓMO RESPONDER (PREVIA DE LA ESCALA) ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = colorBordeCard, shape = RoundedCornerShape(18.dp)),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Text(
                        text = "Cómo responder",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF1E1B26)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Opciones de vista previa
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        opcionesEscala.forEach { (numero, texto) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp)
                                    .border(
                                        width = 1.dp,
                                        color = colorBordeCard,
                                        shape = RoundedCornerShape(23.dp)
                                    )
                                    .padding(horizontal = 14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Círculo con número
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(
                                            color = Color(0xFFF0F1FC),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = numero.toString(),
                                        color = colorHeader,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    text = texto,
                                    fontSize = 14.sp,
                                    color = Color(0xFF1E1B26)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- 4. TEXTO TRANQUILIZADOR ---
            Text(
                text = "No hay respuestas buenas ni malas. Si dudas, elige la que más se acerque.",
                fontSize = 14.sp,
                color = Color(0xFF6B7280),
                lineHeight = 20.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f, fill = false))
            Spacer(modifier = Modifier.height(30.dp))

            // --- 5. BOTÓN EMPEZAR SECCIÓN ---
            Button(
                onClick = { onStartSeccion(idSeccion) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorPrimario,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "EMPEZAR SECCIÓN",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

// Clase auxiliar para agrupar los 4 datos de la sección
private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)