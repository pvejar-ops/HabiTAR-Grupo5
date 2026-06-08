package com.example.habitar.features.autorregulacion.emergencia

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.util.Log

class EmergenciaViewModel(
    private val repository: EmergenciaRepository = EmergenciaRepository()
) : ViewModel() {

    // Listas separadas para alimentar las dos filas de la UI
    var recursosVisuales by mutableStateOf<List<RecursoModulo>>(emptyList())
    var recursosAuditivos by mutableStateOf<List<RecursoModulo>>(emptyList())

    // Estados independientes para el recurso visual y auditivo seleccionado
    var recursoVisualSeleccionado by mutableStateOf<RecursoModulo?>(null)
    var recursoAuditivoSeleccionado by mutableStateOf<RecursoModulo?>(null)

    var lugarSeleccionado by mutableStateOf("")
    var comentarioUsuario by mutableStateOf("")

    var respuestaIA by mutableStateOf("")
    var estaCargandoRecursos by mutableStateOf(false)
    var estaProcesandoIA by mutableStateOf(false)
    var mensajeError by mutableStateOf<String?>(null)
    var guardadoExitoso by mutableStateOf<Boolean?>(null)

    init {
        cargarCatalogoEmergencia()
    }

    private fun cargarCatalogoEmergencia() {
        viewModelScope.launch {
            estaCargandoRecursos = true
            mensajeError = null
            try {
                val todos = repository.obtenerRecursosEmergencia()
                // Filtramos reactivamente por formato
                recursosVisuales = todos.filter { it.formato_recurso == "visual" }
                recursosAuditivos = todos.filter { it.formato_recurso == "auditivo" }
            } catch (e: Exception) {
                mensajeError = "Error al clasificar recursos: ${e.localizedMessage}"
            } finally {
                estaCargandoRecursos = false
            }
        }
    }

    fun seleccionarAudio(recurso: RecursoModulo) {
        // Si ya estaba seleccionado, lo deseleccionamos; si no, lo asignamos
        if (recursoAuditivoSeleccionado?.id_recurso == recurso.id_recurso) {
            recursoAuditivoSeleccionado = null
        } else {
            recursoAuditivoSeleccionado = recurso
            recursoVisualSeleccionado = null // 🌟 CRUCIAL: Apaga el video si se selecciona un audio
        }
    }
    fun seleccionarVisual(recurso: RecursoModulo) {
        // Si ya estaba seleccionado, lo deseleccionamos; si no, lo asignamos
        if (recursoVisualSeleccionado?.id_recurso == recurso.id_recurso) {
            recursoVisualSeleccionado = null
        } else {
            recursoVisualSeleccionado = recurso
            recursoAuditivoSeleccionado = null // 🌟 CRUCIAL: Apaga el audio si se selecciona un video
        }
    }

    fun pedirConsejoYRegistrar() {
        mensajeError = null
        guardadoExitoso = null

        if (lugarSeleccionado.isBlank() || comentarioUsuario.isBlank()) {
            mensajeError = "Asegúrate de completar el lugar y tus sensaciones."
            // 🔍 DEBUG: Si falta llenar algún campo en la UI
            Log.w("DEBUG_EMERGENCIA", "⚠️ Guardado cancelado: Falta completar el lugar o el comentario.")
            return
        }

        viewModelScope.launch {
            estaProcesandoIA = true
            try {
                delay(1800)
                val consejoGenerado = repository.generarConsejoIA(lugarSeleccionado, comentarioUsuario)
                respuestaIA = consejoGenerado

                val idRecursoAsociado = recursoVisualSeleccionado?.id_recurso
                    ?: recursoAuditivoSeleccionado?.id_recurso
                    ?: null

                // 🔍 DEBUG: Ver los datos recolectados antes de enviarlos a Supabase
                Log.d("DEBUG_EMERGENCIA", """
                    🚀 Preparando envío de datos a Supabase...
                    • Lugar seleccionado: $lugarSeleccionado
                    • Comentario usuario: $comentarioUsuario
                    • Respuesta generada por IA: $consejoGenerado
                    • ID Recurso asociado: $idRecursoAsociado
                """.trimIndent())

                val exitoBDD = repository.guardarRegistroEmergencia(
                    lugar = lugarSeleccionado,
                    comentario = comentarioUsuario,
                    respuestaIa = consejoGenerado,
                    idRecurso = idRecursoAsociado
                )

                if (exitoBDD) {
                    guardadoExitoso = true
                    comentarioUsuario = ""
                    // 🔍 DEBUG: El repositorio confirmó una inserción exitosa (HTTP 201)
                    Log.d("HABITAR_EMERGENCIA", "¡ÉXITO! Los datos se guardaron correctamente en la tabla historial_emergencia.")
                } else {
                    mensajeError = "Se generó el consejo, pero no se pudo respaldar en tu historial."
                    // 🔍 DEBUG: Supabase o el repositorio retornaron false
                    Log.e("HABITAR_EMERGENCIA", "ERROR: El repositorio devolvió 'false'. Puede ser un problema de Auth o las políticas RLS.")
                }

            } catch (e: Exception) {
                mensajeError = "Ocurrió un error en la comunicación: ${e.localizedMessage}"
                // 🔍 DEBUG: Capturar caídas críticas de red, parseo de datos, etc.
                Log.e("HABITAR_EMERGENCIA", "EXCEPCIÓN detectada en el flujo: ${e.localizedMessage}", e)
            } finally {
                estaProcesandoIA = false
            }
        }
    }

    fun limpiarFormulario() {
        lugarSeleccionado = ""
        comentarioUsuario = ""
        respuestaIA = ""
        recursoVisualSeleccionado = null
        recursoAuditivoSeleccionado = null
        mensajeError = null
        guardadoExitoso = null
    }
}