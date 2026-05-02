package com.example.habitar.features.avatar

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AvatarScreen(
    onNavigateToMenu: () -> Unit // Cambiado para que coincida con el NavGraph
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Personaliza tu avatar",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Placeholder para el visualizador 3D
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text("Aquí se mostrará el modelo 3D del avatar")
            // Más adelante usaremos SceneView aquí
        }

        Button(
                // Aquí iría la lógica de guardado en Supabase más adelante
                onClick = onNavigateToMenu,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp), // Espacio extra del borde inferior
                shape = MaterialTheme.shapes.medium
        ) {
            Text("Finalizar y Crear Avatar")
        }
    }
}