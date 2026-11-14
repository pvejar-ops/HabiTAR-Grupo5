package com.example.habitar.features.menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.habitar.core.ui.componentes.AppButton

@Composable
fun MenuScreen(
    onNavigateToEmociones: () -> Unit,
    onNavigateToMeditacion: () -> Unit,
    onNavigateToEmergencia: () -> Unit,
    onNavigateToSensaciones: () -> Unit,
    onNavigateToAutoconocimiento: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(top = 24.dp, start = 20.dp, end = 20.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- TARJETA PRINCIPAL DESTACADA: AUTOCONOCIMIENTO (S01) ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(2.dp, Color(0xFF5E72E4)), // Borde morado corporativo
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF5E72E4).copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("✨", fontSize = 22.sp)
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Autoconocimiento",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E1B2E)
                        )
                        Text(
                            text = "Conoce tus patrones y necesidades",
                            fontSize = 13.sp,
                            color = Color(0xFF718096)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onNavigateToAutoconocimiento,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(23.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E72E4))
                ) {
                    Text(
                        "COMENZAR",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }
        }

        // --- TÍTULO TIPO CÁPSULA: MÓDULOS DE AUTORREGULACIÓN ---
        Text(
            text = "Módulos de autorregulación",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(color = Color(0xFF6750A4), shape = CircleShape)
                .padding(vertical = 10.dp),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Selecciona el módulo que se adapte a lo que necesitas",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // --- BLOQUES DE NAVEGACIÓN ORIGINALES ---
        AppButton(
            text = "Emociones",
            onClick = onNavigateToEmociones,
            modifier = Modifier
                .fillMaxWidth()
                .height(84.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD671F5)
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        AppButton(
            text = "Sensaciones Físicas",
            onClick = onNavigateToSensaciones,
            modifier = Modifier
                .fillMaxWidth()
                .height(84.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF9291F3)
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        AppButton(
            text = "Meditación",
            onClick = onNavigateToMeditacion,
            modifier = Modifier
                .fillMaxWidth()
                .height(84.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF5ED9B9)
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        AppButton(
            text = "Emergencia",
            onClick = onNavigateToEmergencia,
            modifier = Modifier
                .fillMaxWidth()
                .height(84.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFA085)
            )
        )
    }
}