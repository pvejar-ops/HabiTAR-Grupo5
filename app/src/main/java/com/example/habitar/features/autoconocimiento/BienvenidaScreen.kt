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
fun BienvenidaScreen(
    onNavigateBack: () -> Unit,
    onNavigateNext: () -> Unit
) {
    // Tokens de color oficiales de HabiTAR
    val colorHeader = Color(0xFF5E72E4)
    val colorPrimario = Color(0xFF6C63FF)
    val colorFondo = Color(0xFFF8F9FE)
    val colorHero = Color(0xFFEEF0FC)
    val colorBordeCard = Color(0xFFE2E4F0)

    Scaffold(
        containerColor = colorFondo,
        topBar = {
            // Cabecera Cápsula flotante (Token Header Autoconocimiento)
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
                    // Flecha atrás con área táctil accesible
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

                    // Título centrado
                    Text(
                        text = "Autoconocimiento",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f).padding(end = 36.dp) // compensa flecha para centrar
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- 1. HERO BOX (Recuadro pastel con la estrella) ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(color = colorHero, shape = RoundedCornerShape(22.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✦", // Estrella/Brillo central del Figma
                    color = colorHeader,
                    fontSize = 54.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- 2. TÍTULO Y DESCRIPCIÓN (Tipografía Roboto) ---
            Text(
                text = "Conócete un poco más, a tu ritmo",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1B26),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Vas a responder preguntas sobre cómo vives tu día a día en la universidad: lo social, tus rutinas y lo que te ayuda.",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF4A4A58),
                lineHeight = 22.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // --- 3. TARJETA INFORMATIVA: QUÉ ES Y QUÉ NO ES ---
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
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Punto 1: Qué es
                    Column {
                        Text(
                            text = "Qué es:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFF1E1B26)
                        )
                        Text(
                            text = "una orientación para conocerte mejor y encontrar recursos útiles.",
                            fontSize = 13.sp,
                            color = Color(0xFF5A5A65),
                            lineHeight = 18.sp
                        )
                    }

                    // Punto 2: Qué no es (Principio Cero Diagnóstico)
                    Column {
                        Text(
                            text = "Qué no es:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFF1E1B26)
                        )
                        Text(
                            text = "un diagnóstico. Solo un profesional de salud puede hacer una evaluación clínica.",
                            fontSize = 13.sp,
                            color = Color(0xFF5A5A65),
                            lineHeight = 18.sp
                        )
                    }

                    // Punto 3: Sin apuro
                    Column {
                        Text(
                            text = "Sin apuro:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFF1E1B26)
                        )
                        Text(
                            text = "no hay tiempo límite y puedes pausar cuando quieras.",
                            fontSize = 13.sp,
                            color = Color(0xFF5A5A65),
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f, fill = false))
            Spacer(modifier = Modifier.height(28.dp))

            // --- 4. BOTÓN CONTINUAR (Píldora accesible) ---
            Button(
                onClick = onNavigateNext,
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
                    text = "CONTINUAR",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- 5. BOTÓN AHORA NO ---
            TextButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
            ) {
                Text(
                    text = "Ahora no",
                    color = colorPrimario,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}