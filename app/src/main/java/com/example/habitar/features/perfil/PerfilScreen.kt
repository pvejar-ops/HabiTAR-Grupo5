package com.example.habitar.features.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun PerfilScreen(
    navController: NavController,
    viewModel: PerfilViewModel = viewModel()
) {
    val perfil = viewModel.perfil
    val estaCargando = viewModel.estaCargando

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Mi Perfil",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFF6750A4), shape = CircleShape)
                .padding(vertical = 12.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 1. Foto de Perfil o Avatar (Círculo)
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (estaCargando) {
            CircularProgressIndicator()
        } else if (perfil != null) {
            Text(text = perfil.nombre_usuario, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            // Usamos un manejo seguro para la carrera (por si es nula en la BDD)
            val infoCarrera = perfil.carrera ?: "Sin carrera definida"
            Text(
                text = "$infoCarrera / ${perfil.edad} años",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = "Correo: ${perfil.correo_electronico}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        } else {
            Text("No se pudo cargar el perfil.")
        }

        Spacer(modifier = Modifier.height(32.dp))


        // 3. Opciones de Configuración
        Column(modifier = Modifier.fillMaxWidth()) {
            PerfilOptionItem(titulo = "Editar Perfil", icono = Icons.Default.Edit)
            //PerfilOptionItem(titulo = "Ajustes de la App", icono = Icons.Default.Settings)
        }

        Spacer(modifier = Modifier.weight(1f))

        // 4. Botón de Cerrar Sesión
        Button(
            onClick = { viewModel.cerrarSesion {
                // Navega al Login, por ejemplo:
                navController.navigate("inicio_screen") {
                    popUpTo(0) { inclusive = true } // Limpia la pila de navegación
                }
            } },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar Sesión")
        }
    }
}

@Composable
fun PerfilOptionItem(titulo: String, icono: ImageVector) {
    Surface(
        onClick = { /* Navegar a la opción */ },
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icono, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = titulo, style = MaterialTheme.typography.bodyLarge)
        }
    }
}