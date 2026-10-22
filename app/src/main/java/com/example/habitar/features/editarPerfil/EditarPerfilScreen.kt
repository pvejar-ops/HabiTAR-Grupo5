package com.example.habitar.features.editarPerfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habitar.features.perfil.PerfilViewModel

@Composable
fun EditarPerfilScreen(
    viewModel: PerfilViewModel,
    onNavigateBack: () -> Unit
) {
    val perfil = viewModel.perfil

    var nombre by remember { mutableStateOf(perfil?.nombre_usuario ?: "") }
    var edad by remember { mutableStateOf(perfil?.edad?.toString() ?: "") }
    var carrera by remember { mutableStateOf(perfil?.carrera ?: "") }

    LaunchedEffect(viewModel.operacionExitosa) {
        if (viewModel.operacionExitosa == true) {
            viewModel.operacionExitosa = null // <- resetear antes de salir
            onNavigateBack()
        }
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
            text = "Editar Perfil",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFF6750A4), shape = CircleShape)
                .padding(vertical = 12.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre de usuario") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = edad,
            onValueChange = { edad = it },
            label = { Text("Edad") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = carrera,
            onValueChange = { carrera = it },
            label = { Text("Carrera") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        viewModel.mensajeError?.let {
            Text(
                text = it,
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                val edadInt = edad.toIntOrNull() ?: 0
                viewModel.guardarCambios(nombre, edadInt, carrera)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4))
        ) {
            Text("Guardar cambios", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onNavigateBack,
            modifier = Modifier.fillMaxWidth(),
            shape = CircleShape
        ) {
            Text("Cancelar")
        }
    }
}