package com.example.habitar.features.auth

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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

@Composable
fun ResetPasswordScreen(
    token: String,
    onResetSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: ResetPasswordViewModel = viewModel()
) {
    LaunchedEffect(viewModel.resetExitoso) {
        if (viewModel.resetExitoso) {
            onResetSuccess()
        }
    }

    var mostrarPassword by remember { mutableStateOf(false) }
    var mostrarConfirmar by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3EDF7))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier
                .background(color = Color.White, shape = CircleShape)
                .size(48.dp)
                .align(Alignment.Start)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver",
                tint = Color(0xFF6750A4)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Nueva contraseña",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF6750A4), CircleShape)
                .padding(vertical = 12.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Ingresa tu nueva contraseña para continuar usando HabiTAR.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = viewModel.nuevaPassword,
            onValueChange = { viewModel.nuevaPassword = it },
            label = { Text("Nueva contraseña") },
            visualTransformation = if (mostrarPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { mostrarPassword = !mostrarPassword }) {
                    Icon(
                        imageVector = if (mostrarPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (mostrarPassword) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = viewModel.confirmarPassword,
            onValueChange = { viewModel.confirmarPassword = it },
            label = { Text("Confirmar contraseña") },
            visualTransformation = if (mostrarConfirmar) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { mostrarConfirmar = !mostrarConfirmar }) {
                    Icon(
                        imageVector = if (mostrarConfirmar) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (mostrarConfirmar) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.cambiarPassword(token) },
            modifier = Modifier.fillMaxWidth(),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6750A4),
                contentColor = Color.White
            ),
            enabled = !viewModel.estaCargando
        ) {
            Text(
                text = if (viewModel.estaCargando) "Guardando..." else "Guardar nueva contraseña",
                fontWeight = FontWeight.Bold
            )
        }

        viewModel.mensajeError?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = it,
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (viewModel.resetExitoso) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "¡Contraseña actualizada con éxito!",
                color = Color(0xFF4CAF50),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}