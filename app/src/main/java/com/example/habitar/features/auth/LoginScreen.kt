package com.example.habitar.features.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.habitar.core.ui.componentes.AppButton

@Composable
fun LoginScreen(onAuthSuccess: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Bienvenido", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        // Aquí irían tus campos de texto para correo y contraseña mas adelante

        AppButton(
            text = "Iniciar Sesión / Registrarse",
            onClick = onAuthSuccess
        )
    }
}