package com.example.habitar.features.caracterizacionIA

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PreferenciasViewModel(
    private val repository: PreferenciasRepository = PreferenciasRepository()
) : ViewModel() {

    // Estados para los tres TextFields de "Cuéntanos de ti"
    var preferenciasSonidos by mutableStateOf("")
    var preferenciasVisuales by mutableStateOf("")
    var debilidadesIdentificadas by mutableStateOf("")

    // Estados de carga y flujo
    var estaGuardando by mutableStateOf(false)
        private set
    var guardadoExitoso by mutableStateOf(false)
        private set
    var mensajeError by mutableStateOf<String?>(null)
        private set

    // Función para enviar los datos a Supabase
    fun guardarFormulario() {
        mensajeError = null // Para limpiar alertas previas

        if (preferenciasSonidos.isBlank() || preferenciasVisuales.isBlank() || debilidadesIdentificadas.isBlank()) {
            mensajeError = "Por favor, responde todas las preguntas para un perfil más preciso."
            return
        }

        viewModelScope.launch {
            estaGuardando = true
            //mensajeError = null

            // Eliminamos espacios en blanco innecesarios en los extremos al enviar
            val exito = repository.guardarPreferenciasIniciales(
                preferenciasSonidos = preferenciasSonidos.trim(),
                preferenciasVisuales = preferenciasVisuales.trim(),
                debilidadesIdentificadas = debilidadesIdentificadas.trim()
            )

            estaGuardando = false
            if (exito) {
                guardadoExitoso = true
            } else {
                mensajeError = "Hubo un problema al guardar tus datos. Inténtalo de nuevo."
            }
        }
    }
}