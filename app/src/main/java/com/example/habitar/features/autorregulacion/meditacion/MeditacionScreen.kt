package com.example.habitar.features.autorregulacion.meditacion

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MeditacionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Zona de Calma",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Encuentra paz en este momento",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Lista de recursos (Simulando lo que se ve en el video)
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                CalmResourceCard(
                    titulo = "Meditación Guiada: Mañana",
                    descripcion = "5 minutos para empezar tu día con enfoque.",
                    tipo = "Audio"
                )
            }
            item {
                CalmResourceCard(
                    titulo = "Respiración Consciente",
                    descripcion = "Ejercicio visual para calmar la ansiedad.",
                    tipo = "Video / Visual"
                )
            }
            item {
                CalmResourceCard(
                    titulo = "Sonidos de la Naturaleza",
                    descripcion = "Lluvia y bosque para estudiar o descansar.",
                    tipo = "Sonido ambiente"
                )
            }
        }
    }
}

@Composable
fun CalmResourceCard(titulo: String, descripcion: String, tipo: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = tipo, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            Text(text = titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = descripcion, style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(12.dp))

            Button(onClick = { /* Reproducir recurso */ }) {
                Text("Reproducir")
            }
        }
    }
}