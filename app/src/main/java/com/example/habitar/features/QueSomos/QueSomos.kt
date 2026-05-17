package com.example.habitar.features.QueSomos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun QueSomosScreen(onNavigateToAuth: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "¡Bienvenid@ a HabiTAR!")
        Text(text = "Somos un espacio de bienestar para estudiantes.")

        Spacer(modifier = Modifier.height(20.dp))

        // Botón para ir al Login después de leer quiénes son
        Button(onClick = onNavigateToAuth) {
            Text("Entendido, ir al Login")
        }
    }
}