package com.example.habitar.features.autorregulacion.sensaciones

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SensacionesViewModel(
    private val repository: SensacionesRepository = SensacionesRepository()
) : ViewModel() {

    // Estados de entrada del usuario
    var sensacionesSeleccionadas by mutableStateOf<List<String>>(emptyList())
    var comentarioUsuario by mutableStateOf("")

    // Estados de control de la pantalla
    var estaProcesando by mutableStateOf(false)
        private set
    var respuestaIA_Visualizar by mutableStateOf("")
        private set
    var mensajeError by mutableStateOf<String?>(null)
        private set
    var procesoExitoso by mutableStateOf(false)
        private set

    // Control dinámico de selección de síntomas
    fun alternarSensacion(sensacion: String) {
        val listaActual = sensacionesSeleccionadas.toMutableList()
        if (listaActual.contains(sensacion)) {
            listaActual.remove(sensacion)
        } else {
            listaActual.add(sensacion)
        }
        sensacionesSeleccionadas = listaActual
    }

    fun enviarFormularioSensaciones() {
        mensajeError = null
        procesoExitoso = false

        if (sensacionesSeleccionadas.isEmpty()) {
            mensajeError = "Por favor, selecciona al menos una sensación física para poder orientarte."
            return
        }

        viewModelScope.launch {
            estaProcesando = true
            try {
                // 1. Simulación o llamado futuro a la IA pasando las sensaciones y el nivel
                val respuestaGenerada = generarRespuestaDeIA(sensacionesSeleccionadas, comentarioUsuario)

                respuestaIA_Visualizar = respuestaGenerada

                // 2. Guardar en Supabase
                val exito = repository.guardarRegistroSensacion(
                    sensaciones = sensacionesSeleccionadas,
                    comentario = comentarioUsuario.trim(),
                    respuestaIA = respuestaGenerada
                )

                if (exito) {
                    procesoExitoso = true
                } else {
                    mensajeError = "Se generó el consejo, pero no se pudo conectar con tu historial."
                }
            } catch (e: Exception) {
                mensajeError = "Error en el flujo: ${e.localizedMessage}"
            } finally {
                estaProcesando = false
            }
        }
    }

    private suspend fun generarRespuestaDeIA(sensaciones: List<String>, comentarioUsuario: String): String {
        delay(1800)
        val sintomas = sensaciones.joinToString(", ")
        return "HabiTAR detecta que estás experimentando $sintomas y $comentarioUsuario. Te sugiero buscar un punto de apoyo cercano, regular tu respiración inhalando en 4 tiempos."
    }
}