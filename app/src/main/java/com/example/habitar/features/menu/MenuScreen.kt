package com.example.habitar.features.menu

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habitar.core.ui.componentes.AppButton

@Composable
fun MenuScreen(
    onNavigateToEmociones: () -> Unit,
    onNavigateToMeditacion: () -> Unit,
    onNavigateToEmergencia: () -> Unit,
    onNavigateToSensaciones: () -> Unit,
    onNavigateToAutoconocimiento: () -> Unit = {} // <-- NUEVO: Navegación al módulo de screening
) {
    val context = LocalContext.current

    // Verificamos si el usuario tiene progreso guardado para cambiar el texto del botón
    val prefs = remember { context.getSharedPreferences("habitar_screening_prefs", Context.MODE_PRIVATE) }
    val tieneProgreso = remember { prefs.getBoolean("tiene_progreso_guardado", false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Color(0xFFF8F9FE)) // Token de Fondo General
            .verticalScroll(rememberScrollState())
            .padding(top = 16.dp, start = 20.dp, end = 20.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- CABECERA CÁPSULA BASE ---
        Text(
            text = "Módulos de autorregulación",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(color = Color(0xFF6750A4), shape = CircleShape)
                .padding(vertical = 12.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Selecciona el módulo que se adapte a lo que necesitas",
            fontSize = 14.sp,
            color = Color(0xFF5A5A65),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(18.dp))

        // --- 1. MÓDULO EMOCIONES (Heredado) ---
        AppButton(
            text = "Emociones",
            onClick = onNavigateToEmociones,
            modifier = Modifier
                .fillMaxWidth()
                .height(84.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE879F9) // Token Emociones
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // --- 2. MÓDULO SENSACIONES (Heredado) ---
        AppButton(
            text = "Sensaciones",
            onClick = onNavigateToSensaciones,
            modifier = Modifier
                .fillMaxWidth()
                .height(84.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF818CF8) // Token Sensaciones
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // --- 3. MEDITACIÓN Y EMERGENCIA (Fila lado a lado como en Figma) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Meditación
            Button(
                onClick = onNavigateToMeditacion,
                modifier = Modifier
                    .weight(1f)
                    .height(76.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6C9A8B) // Token Calma / Meditación
                )
            ) {
                Text(
                    text = "Meditación",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            // Emergencia
            Button(
                onClick = onNavigateToEmergencia,
                modifier = Modifier
                    .weight(1f)
                    .height(76.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFCC6774) // Color Emergencia Figma
                )
            ) {
                Text(
                    text = "Emergencia",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- 4. NUEVA TARJETA: AUTOCONOCIMIENTO (S01 FIGMA) ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.5.dp,
                    color = Color(0xFF5E72E4), // Token Módulo Autoconocimiento
                    shape = RoundedCornerShape(20.dp)
                ),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                // Fila superior: Ícono Brillo + Textos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Contenedor del ícono ✨
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(
                                color = Color(0xFF5E72E4),
                                shape = RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✦", // Estrella/Brillo de 4 puntas de Figma
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    // Textos descriptivos
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Autoconocimiento",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF1E1B26)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Explora cómo te relacionas, tus rutinas y lo que te ayuda. A tu ritmo.",
                            fontSize = 13.sp,
                            color = Color(0xFF6B7280),
                            lineHeight = 17.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón Píldora: COMENZAR / CONTINUAR
                Button(
                    onClick = onNavigateToAutoconocimiento,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp), // Área táctil mínima accesible
                    shape = CircleShape, // Estilo Píldora
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5E72E4),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = if (tieneProgreso) "CONTINUAR" else "COMENZAR",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        letterSpacing = 1.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}