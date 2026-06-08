package com.example.habitar.features.menu

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.habitar.core.ui.componentes.AppButton

@Composable
fun MenuScreen(
    //Permiten que el NavHost controle hacia dónde va el usuario al hacer clic.
    onNavigateToEmociones: () -> Unit,
    onNavigateToMeditacion: () -> Unit,
    onNavigateToEmergencia: () -> Unit,
    onNavigateToSensaciones: () -> Unit
) {
    // Column: Organiza los elementos de forma vertical uno debajo del otro.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState()) // Habilita el desplazamiento si el contenido es más alto que la pantalla.
            .padding(top = 40.dp, start = 24.dp, end = 24.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Centra todos los componentes hijos horizontalmente.
    ) {
        // --- TÍTULO TIPO CÁPSULA
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Módulos de autorregulación",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .background(color = Color(0xFF6750A4), shape = CircleShape)
                .padding(vertical = 12.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Selecciona el módulo que se adapte a lo que necesitas",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        // --- BLOQUES DE NAVEGACIÓN (Basados en image_71c9c7.png) ---

        // AppButton (Emociones): Bloque de color rosado vibrante.
        AppButton(
            text = "Emociones",
            onClick = onNavigateToEmociones, // Ejecuta la navegación hacia la pantalla de emociones.
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp), // Altura fija de 90dp para darle aspecto de bloque/tarjeta.
            shape = RoundedCornerShape(16.dp), // Bordes suavizados con un radio de 16dp.
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD671F5) // Color de fondo específico para este bloque.
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // AppButton (Sensaciones): Bloque de color lila suave.
        AppButton(
            text = "Sensaciones Físicas",
            onClick = onNavigateToSensaciones,
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF9291F3)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // AppButton (Meditación): Bloque de color verde menta/turquesa.
        AppButton(
            text = "Meditación",
            onClick = onNavigateToMeditacion,
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF5ED9B9)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // AppButton (Emergencia): Bloque de color naranja pastel.
        AppButton(
            text = "Emergencia",
            onClick = onNavigateToEmergencia,
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFA085)
            )
        )
    }
}