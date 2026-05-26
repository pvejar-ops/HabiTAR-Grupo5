package com.example.habitar.features.autorregulacion.emociones

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EmocionesViewModel(
    private val repository: EmocionesRepository = EmocionesRepository()
) : ViewModel() {

    // Estados de lo que ingresa el usuario en la interfaz
    var emocionesSeleccionadas by mutableStateOf<List<String>>(emptyList())
    var comentarioUsuario by mutableStateOf("")

    // Estados para controlar el flujo de la pantalla
    var estaProcesando by mutableStateOf(false)
        private set
    var respuestaIA_Visualizar by mutableStateOf("")
        private set
    var mensajeError by mutableStateOf<String?>(null)
        private set
    var procesoExitoso by mutableStateOf(false)
        private set

    // Agrega o remueve la emoción de la lista según el usuario clickee los bloques
    fun alternarEmocion(emocion: String) {
        emocionesSeleccionadas = if (emocionesSeleccionadas.contains(emocion)) {
            emocionesSeleccionadas - emocion
        } else {
            emocionesSeleccionadas + emocion
        }
    }

    fun enviarFormularioEmociones() {
        mensajeError = null
        procesoExitoso = false

        if (emocionesSeleccionadas.isEmpty()) {
            mensajeError = "Por favor, selecciona al menos una emoción para poder ayudarte."
            return
        }

        viewModelScope.launch {
            estaProcesando = true

            try {
                // 1. Llamamos a la función que procesa la IA (Gemini, Edge Functions, etc.)
                val respuestaGenerada = generarRespuestaDeIA(emocionesSeleccionadas, comentarioUsuario)

                // Mostramos la respuesta de inmediato en el cuadro de texto de la app
                respuestaIA_Visualizar = respuestaGenerada

                // 2. Guardamos todo el bloque (Entradas + Salida de la IA) en Supabase
                val exitoBDD = repository.guardarRegistroEmocion(
                    emociones = emocionesSeleccionadas,
                    comentario = comentarioUsuario.trim(),
                    respuestaIA = respuestaGenerada
                )

                if (exitoBDD) {
                    procesoExitoso = true
                    // Opcional: Limpiar el campo de texto tras guardar con éxito
                    comentarioUsuario = ""
                } else {
                    mensajeError = "Se generó el consejo, pero no se pudo respaldar en tu historial."
                }

            } catch (e: Exception) {
                mensajeError = "Ocurrió un error en la comunicación: ${e.localizedMessage}"
            } finally {
                estaProcesando = false
            }
        }
    }

    // Simulación de la IA de HabiTAR (aquí conectarás el SDK oficial más adelante)
    private suspend fun generarRespuestaDeIA(emociones: List<String>, comentario: String): String {
        delay(1800) // Simula el tiempo de carga de la red
        val listaEmociones = emociones.joinToString(", ")
        return "Entiendo perfectamente que te sientas de la siguiente manera: $listaEmociones. Respecto a lo que describes ('$comentario'), te recomiendo reducir estímulos sonoros en tu trayecto actual para equilibrar tu entorno."
    }
}