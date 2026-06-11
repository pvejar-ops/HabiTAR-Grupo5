package com.example.habitar.features.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.unit.dp
import com.example.habitar.core.ui.componentes.AppButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habitar.features.auth.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AutoScrollTextField(content: @Composable (Modifier) -> Unit) {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    content(
        Modifier.bringIntoViewRequester(bringIntoViewRequester)
            .onFocusEvent { focusState ->
                if (focusState.isFocused) {
                    coroutineScope.launch {
                        delay(300) // Espera a que el teclado suba
                        bringIntoViewRequester.bringIntoView()
                    }
                }
            }
    )
}


@Composable
fun LoginScreen(
    onAuthSuccess: () -> Unit,    // Se ejecuta cuando el login es exitoso (navega al Menú)
    onRegisterSuccess: () -> Unit, // Se ejecuta al crear cuenta (navega al flujo de Avatar)
    viewModel: AuthViewModel = viewModel() // Recibe el motor de Supabase
) {
    // Escuchar si Supabase da luz verde al Login
    LaunchedEffect(viewModel.loginExitoso) {
        if (viewModel.loginExitoso) {
            onAuthSuccess()
        }
    }

    // Escuchar si Supabase da luz verde al Registro
    LaunchedEffect(viewModel.registroExitoso) {
        if (viewModel.registroExitoso) {
            onRegisterSuccess()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEEEEF8)) // 👈 primero el fondo
            .imePadding()                  // 👈 luego el teclado
    ) {
    // Column: Organiza todos los elementos de forma vertical (uno sobre otro)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF3EDF7))
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
        // --- VARIABLES DE ESTADO (Manejo de Datos) ---
        // 'remember' permite que el valor persista aunque la pantalla se redibuje
        // 'mutableStateOf' hace que la interfaz se actualice automáticamente al escribir
        //var email by remember { mutableStateOf("") }
        //var contraseña by remember { mutableStateOf("") }
        //var nombre by remember { mutableStateOf("") }
        //var edad by remember { mutableStateOf("") }// var telefonoRegistro by mutableStateOf("")


        // Spacer: Crea un espacio vacío para separar elementos (en este caso, del borde superior)
        Spacer(modifier = Modifier.height(40.dp))

        // Título de la sección de inicio de sesión
        Text(
            text = "Inicia sesión",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        // OutlinedTextField: Campo de texto con borde para la entrada del correo
        AutoScrollTextField { autoScrollModifier ->
            OutlinedTextField(
                value = viewModel.emailLogin,
                onValueChange = { viewModel.emailLogin = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth().then(autoScrollModifier),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de texto para la contraseña
        AutoScrollTextField { autoScrollModifier ->
        OutlinedTextField(
            value = viewModel.passwordLogin,
            onValueChange = { viewModel.passwordLogin = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth().then(autoScrollModifier),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
        }
        Spacer(modifier = Modifier.height(32.dp))

        // AppButton: Botón de inicio de sesión
        AppButton(
            text = if (viewModel.estaCargando) "Cargando..." else "Iniciar Sesión",
            onClick = { viewModel.ejecutarLogin() }, // <-- CAMBIADO
            enabled = !viewModel.estaCargando,       // <-- NUEVO
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF5E5CE6), // Color principal personalizado (Morado/Azul)
                contentColor = Color.White         // Color del texto blanco para legibilidad
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- SEPARADOR VISUAL ---
        // Row: Alinea elementos horizontalmente para crear el efecto de la línea partida
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // HorizontalDivider: Dibuja la línea izquierda
            HorizontalDivider(
                modifier = Modifier.weight(1f), // weight(1f) hace que la línea ocupe todo el espacio sobrante
                thickness = 5.dp,
                color = Color(0xFF6061D1)
            )

            // Spacer central: Crea el hueco donde visualmente se "rompe" la línea
            Spacer(modifier = Modifier.width(75.dp))

            // HorizontalDivider: Dibuja la línea derecha
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 5.dp,
                color = Color(0xFF6061D1)
            )
        }

        // Título secundario para la sección de registro
        Text(
            text = "Crea una cuenta",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 24.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // --- CAMPOS DE REGISTRO ---
            AutoScrollTextField { autoScrollModifier ->
                OutlinedTextField(
                    value = viewModel.nombreRegistro,
                    onValueChange = { viewModel.nombreRegistro = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth().then(autoScrollModifier),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

// Edad y Teléfono lado a lado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AutoScrollTextField { autoScrollModifier ->
                    OutlinedTextField(
                        value = viewModel.edadRegistro,
                        onValueChange = { viewModel.edadRegistro = it },
                        label = { Text("Edad") },
                        modifier = Modifier.weight(0.35f).then(autoScrollModifier),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }
                AutoScrollTextField { autoScrollModifier ->
                    OutlinedTextField(
                        value = viewModel.telefonoRegistro,
                        onValueChange = { viewModel.telefonoRegistro = it },
                        label = { Text("Teléfono") },
                        modifier = Modifier.weight(0.65f).then(autoScrollModifier),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

// --- Carrera registro ---
            AutoScrollTextField { autoScrollModifier ->
                OutlinedTextField(
                    value = viewModel.carreraRegistro,
                    onValueChange = { viewModel.carreraRegistro = it },
                    label = { Text("Carrera") },
                    modifier = Modifier.fillMaxWidth().then(autoScrollModifier),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

// Email registro
            AutoScrollTextField { autoScrollModifier ->
                OutlinedTextField(
                    value = viewModel.emailRegistro,
                    onValueChange = { viewModel.emailRegistro = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth().then(autoScrollModifier),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

// Contraseña registro
            AutoScrollTextField { autoScrollModifier ->
                OutlinedTextField(
                    value = viewModel.passwordRegistro,
                    onValueChange = { viewModel.passwordRegistro = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth().then(autoScrollModifier),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }


        Spacer(modifier = Modifier.height(20.dp))

        // Botón final que dispara la lógica de registro y navegación al avatar
        AppButton(
            text = if (viewModel.estaCargando) "Registrando..." else "Crear Cuenta",
            onClick = { viewModel.ejecutarRegistro() }, // <-- CAMBIADO
            enabled = !viewModel.estaCargando,          // <-- NUEVO
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF5E5CE6), // Color principal personalizado (Morado/Azul)
                contentColor = Color.White         // Color del texto blanco para legibilidad
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (viewModel.registroExitoso) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "¡Cuenta creada con éxito! Bienvenido a HabiTAR.",
                color = Color(0xFF4CAF50), // Verde amigable de éxito
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
        }

        if (viewModel.mensajeError != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = viewModel.mensajeError ?: "Error desconocido",
                color = Color.Red, // Rojo de advertencia
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
        }
    }
  }
}