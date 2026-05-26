package com.example.habitar.features.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {

    // ESTADOS PARA INICIAR SESIÓN
    var emailLogin by mutableStateOf("")
    var passwordLogin by mutableStateOf("")

    // ESTADOS PARA CREAR CUENTA
    var nombreRegistro by mutableStateOf("")
    var emailRegistro by mutableStateOf("")
    var passwordRegistro by mutableStateOf("")
    var edadRegistro by mutableStateOf("") // Si usas la edad

    // Estados de la interfaz (Cargando, Éxito, Error)
    var estaCargando by mutableStateOf(false)
        private set
    var mensajeError by mutableStateOf<String?>(null)
        private set
    var loginExitoso by mutableStateOf(false)
        private set
    var registroExitoso by mutableStateOf(false)
        private set

    // Función para Iniciar Sesión
    fun ejecutarLogin() {
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

        if (emailLimpio.isBlank() || passwordLimpia.isBlank() || nombreLimpio.isBlank() || edadLimpio.isBlank()) {
            mensajeError = "Por favor, completa todos los campos"
            return
        }

        viewModelScope.launch {
            estaCargando = true
            mensajeError = null
            val edadEntero = edadLimpio.toIntOrNull() ?: 0
            val resultado = repository.registrarEstudiante(emailLimpio, passwordLimpia, nombreLimpio, edadEntero)
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
}