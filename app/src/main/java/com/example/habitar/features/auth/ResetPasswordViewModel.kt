package com.example.habitar.features.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitar.core.SupabaseConfig
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.OTP
import kotlinx.coroutines.launch
import io.github.jan.supabase.gotrue.user.UserSession

class ResetPasswordViewModel : ViewModel() {

    var nuevaPassword by mutableStateOf("")
    var confirmarPassword by mutableStateOf("")
    var estaCargando by mutableStateOf(false)
    var mensajeError by mutableStateOf<String?>(null)
    var resetExitoso by mutableStateOf(false)

    fun cambiarPassword(token: String) {
        mensajeError = null

        if (nuevaPassword.isBlank() || confirmarPassword.isBlank()) {
            mensajeError = "Por favor completa ambos campos."
            return
        }

        if (nuevaPassword != confirmarPassword) {
            mensajeError = "Las contraseñas no coinciden."
            return
        }

        if (nuevaPassword.length < 6) {
            mensajeError = "La contraseña debe tener al menos 6 caracteres."
            return
        }

        viewModelScope.launch {
            estaCargando = true
            try {
                // Primero establecer la sesión con el token
                SupabaseConfig.client.auth.importSession(
                    UserSession(
                        accessToken = token,
                        refreshToken = "",
                        expiresIn = 3600,
                        tokenType = "bearer",
                        user = null
                    )
                )
                // Luego actualizar la contraseña
                SupabaseConfig.client.auth.updateUser {
                    password = nuevaPassword
                }
                resetExitoso = true
            } catch (e: Exception) {
                mensajeError = "Error al actualizar: ${e.localizedMessage}"
            } finally {
                estaCargando = false
            }
        }
    }
}