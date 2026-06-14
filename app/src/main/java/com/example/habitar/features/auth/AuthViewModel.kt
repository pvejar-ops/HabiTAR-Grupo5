package com.example.habitar.features.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitar.core.SupabaseConfig
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {

    // ESTADOS PARA INICIAR SESIÓN
    var emailLogin by mutableStateOf("")
    var passwordLogin by mutableStateOf("")

    // ESTADOS PARA CREAR CUENTA
    var nombreRegistro by mutableStateOf("")
    var emailRegistro by mutableStateOf("")
    var passwordRegistro by mutableStateOf("")
    var edadRegistro by mutableStateOf("") //
    var telefonoRegistro by mutableStateOf("") //NUEVITO
    var carreraRegistro by mutableStateOf("")

    // Estados de la interfaz (Cargando, Éxito, Error)
    var estaCargando by mutableStateOf(false)
        private set
    var mensajeError by mutableStateOf<String?>(null)
        private set
    var loginExitoso by mutableStateOf(false)
        private set
    var registroExitoso by mutableStateOf(false)
        private set
    var recordarme by mutableStateOf(false)

    // Función para Iniciar Sesión
    fun ejecutarLogin(contexto: android.content.Context) {
        if (emailLogin.isBlank() || passwordLogin.isBlank()) {
            mensajeError = "Por favor, completa todos los campos"
            return
        }

        viewModelScope.launch {
            estaCargando = true
            mensajeError = null
            val resultado = repository.iniciarSesion(emailLogin, passwordLogin)
            estaCargando = false

            if (resultado) {
                // Guardar preferencia de recordarme
                val prefs = contexto.getSharedPreferences("habitar_prefs", android.content.Context.MODE_PRIVATE)
                prefs.edit().putBoolean("recordarme", recordarme).apply()
                loginExitoso = true
            } else {
                mensajeError = "Correo o contraseña incorrectos"
            }
        }
    }

    // Función para Registrar Estudiante
    fun ejecutarRegistro() {
        // Aplicamos .trim() para limpiar espacios invisibles
        val emailLimpio = emailRegistro.trim()
        val passwordLimpia = passwordRegistro.trim()
        val nombreLimpio = nombreRegistro.trim()
        val edadLimpio = edadRegistro.trim()
        val carreraLimpio = carreraRegistro.trim()
        val telefonoLimpio = telefonoRegistro.trim()

        if (emailLimpio.isBlank() || passwordLimpia.isBlank() || nombreLimpio.isBlank() || edadLimpio.isBlank() || carreraLimpio.isBlank() || telefonoLimpio.isBlank()) {
            mensajeError = "Por favor, completa todos los campos"
            return
        }

        if (passwordLimpia.length < 6) {
            mensajeError = "La contraseña debe tener al menos 6 caracteres"
            return
        }

        viewModelScope.launch {
            estaCargando = true
            mensajeError = null
            val edadEntero = edadLimpio.toIntOrNull() ?: 0
            val resultado = repository.registrarEstudiante(emailLimpio, passwordLimpia, nombreLimpio, edadEntero, carreraLimpio, telefonoLimpio)
            estaCargando = false

            if (resultado) {
                registroExitoso = true
            } else {
                mensajeError = "Error al registrar. El usuario ya existe o la contraseña es muy corta."
            }
        }
    }

    // Resetear estados al cambiar de pantalla
    fun limpiarMensajes() {
        mensajeError = null
    }

    var mensajeRestablecimiento by mutableStateOf<String?>(null)

    fun enviarCorreoRestablecimiento() {
        if (emailLogin.isBlank()) {
            mensajeError = "Ingresa tu email primero para restablecer la contraseña."
            return
        }
        viewModelScope.launch {
            estaCargando = true
            try {
                SupabaseConfig.client.auth.resetPasswordForEmail(emailLogin)
                mensajeRestablecimiento = "Te enviamos un correo para restablecer tu contraseña."
            } catch (e: Exception) {
                mensajeError = "Error al enviar el correo: ${e.localizedMessage}"
            } finally {
                estaCargando = false
            }
        }
    }
}