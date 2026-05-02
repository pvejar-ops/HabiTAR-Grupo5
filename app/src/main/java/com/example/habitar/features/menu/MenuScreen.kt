package com.example.habitar.features.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun MenuScreen(
    onNavigateToEmociones: () -> Unit,
    onNavigateToMeditacion: () -> Unit,
    onNavigateToEmergencia: () -> Unit,
    onNavigateToSensaciones: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 1. Espacio para el Avatar (Placeholder por ahora)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Aquí irá el Avatar saludando", style = MaterialTheme.typography.bodyLarge)
            // Aquí integrarás el componente de Avatar SDK más adelante
        }

        Text(
            text = "Módulos de ayuda",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // 2. Cuadrícula de botones (2x2) como en el wireframe
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            item { MenuCard("Emociones", onNavigateToEmociones) }
            item { MenuCard("Meditación", onNavigateToMeditacion) }
            item { MenuCard("Emergencia", onNavigateToEmergencia) }
            item { MenuCard("Sensaciones", onNavigateToSensaciones) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuCard(titulo: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .height(120.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = titulo, fontWeight = FontWeight.Medium)
        }
    }
}