package com.example.habitar.features.autorregulacion.emociones

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun EmocionesScreen() {
    // ESTADOS: Para que la pantalla "recuerde" qué escribió el usuario y qué seleccionó
    var textoEmocion by remember { mutableStateOf("") }
    val etiquetasDisponibles = listOf("Feliz", "Triste", "Ansioso", "Enojado", "Frustrado", "Calmado")
    val etiquetasSeleccionadas = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "¿Cómo te sientes?",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 1. Selección de Etiquetas (Chips)
        Text(text = "Selecciona etiquetas:", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))

        // Usamos LazyRow para que el usuario pueda deslizar las etiquetas hacia los lados
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(etiquetasDisponibles) { etiqueta ->
                val estaSeleccionada = etiquetasSeleccionadas.contains(etiqueta)
                EmotionChip(
                    text = etiqueta,
                    isSelected = estaSeleccionada,
                    onCheckedChange = {
                        if (estaSeleccionada) etiquetasSeleccionadas.remove(etiqueta)
                        else etiquetasSeleccionadas.add(etiqueta)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2. Campo de texto libre
        OutlinedTextField(
            value = textoEmocion,
            onValueChange = { textoEmocion = it },
            label = { Text("Escribe más detalles sobre tu sentir...") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 3. Botón para enviar a la IA
        Button(
            onClick = { /* Aquí llamaremos a Gemini más adelante */ },
            modifier = Modifier.fillMaxWidth(),
            enabled = textoEmocion.isNotBlank() || etiquetasSeleccionadas.isNotEmpty()
        ) {
            Text("Pedir consejo al Avatar")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 4. Placeholder para la respuesta de la IA (Card de respuesta)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Respuesta de tu Avatar:",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Aquí aparecerá el consejo generado por la IA basado en tus emociones...",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun EmotionChip(
    text: String,
    isSelected: Boolean,
    onCheckedChange: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.clickable { onCheckedChange() }
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
}