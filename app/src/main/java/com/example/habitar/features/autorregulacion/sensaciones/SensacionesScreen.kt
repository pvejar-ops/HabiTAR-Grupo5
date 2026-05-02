package com.example.habitar.features.autorregulacion.sensaciones

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SensacionesScreen() {
    var detalleSensacion by remember { mutableStateOf("") }
    val sensacionesDisponibles = listOf("Tensión en cuello", "Nudo en el estómago", "Palpitaciones", "Sudoración", "Falta de aire", "Hormigueo")
    val seleccionadas = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Sensaciones Físicas",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "¿Qué está sintiendo tu cuerpo ahora?",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 1. Selector de sensaciones (Chips)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(sensacionesDisponibles) { sensacion ->
                val estaSeleccionada = seleccionadas.contains(sensacion)
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (estaSeleccionada) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.clickable {
                        if (estaSeleccionada) seleccionadas.remove(sensacion)
                        else seleccionadas.add(sensacion)
                    }
                ) {
                    Text(
                        text = sensacion,
                        color = if (estaSeleccionada) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2. Descripción libre
        OutlinedTextField(
            value = detalleSensacion,
            onValueChange = { detalleSensacion = it },
            label = { Text("Describe más sobre esta sensación...") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 3. Botón de acción
        Button(
            onClick = { /* Lógica para Gemini */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Analizar sensaciones con la IA")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 4. Card de Respuesta (Placeholder)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Análisis del Avatar:", fontWeight = FontWeight.Bold)
                Text(text = "Tu cuerpo está reaccionando. Vamos a realizar un ejercicio de escaneo corporal para relajar esas zonas...", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}