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
import androidx.compose.material.icons.filled.History
import androidx.compose.runtime.LaunchedEffect

@Composable
fun PerfilScreen(
    navController: NavController,
    //viewModel: PerfilViewModel = viewModel()
    viewModel: PerfilViewModel
) {
    val perfil = viewModel.perfil
    val estaCargando = viewModel.estaCargando

    LaunchedEffect(Unit) {
        viewModel.cargarPerfil()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
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
            PerfilOptionItem(
                titulo = "Editar Perfil",
                icono = Icons.Default.Edit,
                onClick = {
                    android.util.Log.d("PERFIL", "Navegando a editar perfil")
                    navController.navigate("editar_perfil_screen") }
            )
            PerfilOptionItem(
                titulo = "Ver mi historial",
                icono = Icons.Default.History,
                onClick = { navController.navigate("historial_screen") }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // 4. Botón de Cerrar Sesión
        Button(
            onClick = {
                viewModel.limpiarPerfil()
                viewModel.cerrarSesion {
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
fun PerfilOptionItem(titulo: String, icono: ImageVector, onClick: () -> Unit = {}) {
    Surface(
        onClick = onClick,
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