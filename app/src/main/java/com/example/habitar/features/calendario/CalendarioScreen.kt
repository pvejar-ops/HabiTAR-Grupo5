package com.example.habitar.features.calendario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Add

@Composable
fun CalendarioScreen(onNavigateToOrganizaDia: () -> Unit) { // 1. Recibe la función
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToOrganizaDia, // 2. Al hacer clic, navega
                icon = { Icon(Icons.Default.Add, "Añadir") },
                text = { Text("Organiza tu día") },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        }
    ) { padding -> Column(modifier = Modifier.padding(padding)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Mi Historial",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Revisa cómo te has sentido últimamente",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Simulación de una lista de registros diarios
            val registros = listOf(
                RegistroDia("Hoy", "Calma", MaterialTheme.colorScheme.primaryContainer),
                RegistroDia("Ayer", "Ansiedad", MaterialTheme.colorScheme.errorContainer),
                RegistroDia("Lunes 28", "Felicidad", MaterialTheme.colorScheme.tertiaryContainer),
                RegistroDia("Domingo 27", "Tristeza", MaterialTheme.colorScheme.secondaryContainer)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(registros) { registro ->
                    ItemCalendario(registro)
                }
            }
        }
    }
    }
    }

@Composable
fun ItemCalendario(registro: RegistroDia) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Círculo de color que representa la emoción
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(registro.color)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(text = registro.fecha, fontWeight = FontWeight.Bold)
                Text(text = "Estado: ${registro.emocion}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

// Clase de datos para los registros
data class RegistroDia(val fecha: String, val emocion: String, val color: androidx.compose.ui.graphics.Color)