package com.example.habitar.features.autorregulacion.emergencia

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

@Composable
fun EmergenciaScreen() {
    var ubicacionSeleccionada by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Botón de Emergencia",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "¿Dónde te encuentras ahora mismo?",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Opciones de ubicación según el wireframe
        val opciones = listOf(
            UbicacionOp("Universidad", Icons.Default.School),
            UbicacionOp("Vía Pública", Icons.Default.LocationOn),
            UbicacionOp("Casa", Icons.Default.Home)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(opciones) { opcion ->
                OutlinedButton(
                    onClick = { ubicacionSeleccionada = opcion.nombre },
                    modifier = Modifier.fillMaxWidth().height(80.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (ubicacionSeleccionada == opcion.nombre)
                            MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                    )
                ) {
                    Icon(opcion.icono, contentDescription = null)
                    Spacer(Modifier.width(12.dp))
                    Text(opcion.nombre, style = MaterialTheme.typography.titleMedium)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (ubicacionSeleccionada != null) {
            // Card de respuesta de IA (Gemini)
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Instrucción de Calma Inmediata:",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Estás en ${ubicacionSeleccionada}. Respira hondo. Busca un lugar tranquilo. Estoy aquí para ayudarte...",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

data class UbicacionOp(val nombre: String, val icono: ImageVector)