package com.example.habitar.features.autorregulacion.meditacion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MeditacionViewModel(
    private val repository: MeditacionRepository = MeditacionRepository()
) : ViewModel() {

    // Listas separadas para la UI
    var recursosVisuales by mutableStateOf<List<RecursoModulo>>(emptyList())
    var recursosAuditivos by mutableStateOf<List<RecursoModulo>>(emptyList())

    // Recursos seleccionados actualmente por el usuario (guarda el objeto completo)
    var recursoVisualSeleccionado by mutableStateOf<RecursoModulo?>(null)
    var recursoAuditivoSeleccionado by mutableStateOf<RecursoModulo?>(null)

    // Estados de control
    var estaCargandoRecursos by mutableStateOf(false)
    var estaProcesandoMeditation by mutableStateOf(false)
    var respuestaIA_Visualizar by mutableStateOf("")
    var mensajeError by mutableStateOf<String?>(null)

        init {
            cargarCatalogoRecursos()
            viewModelScope.launch {
                repository.registrarEntradaMeditacion()
            }
        }

        private fun cargarCatalogoRecursos() {
            viewModelScope.launch {
                estaCargandoRecursos = true
                val todos = repository.obtenerRecursos()
                recursosVisuales = todos.filter { it.formato_recurso == "visual" }
                recursosAuditivos = todos.filter { it.formato_recurso == "auditivo" }
                estaCargandoRecursos = false
            }
        }

        // Ya no guarda al seleccionar — solo cambia el estado visual
        fun seleccionarAudio(recurso: RecursoModulo) {
            val esElMismo = recursoAuditivoSeleccionado?.id_recurso == recurso.id_recurso
            recursoAuditivoSeleccionado = if (esElMismo) null else recurso
        }

        fun seleccionarVisual(recurso: RecursoModulo) {
            val esElMismo = recursoVisualSeleccionado?.id_recurso == recurso.id_recurso
            recursoVisualSeleccionado = if (esElMismo) null else recurso
        }

        fun iniciarMeditacion() {
            mensajeError = null
            if (recursoVisualSeleccionado == null && recursoAuditivoSeleccionado == null) {
                mensajeError = "Por favor selecciona al menos un recurso para meditar."
                return
            }

            viewModelScope.launch {
                estaProcesandoMeditation = true
                try {
                    val visualNom = recursoVisualSeleccionado?.nombre_recurso ?: "Ninguno"
                    val auditivoNom = recursoAuditivoSeleccionado?.nombre_recurso ?: "Ninguno"
                    respuestaIA_Visualizar = "HabiTAR ha configurado tu entorno de meditación. " +
                            "Estás usando el recurso visual '$visualNom' y el recurso auditivo '$auditivoNom'. " +
                            "Mantén tu espalda recta, enfoca tu mirada sutilmente y sigue el ritmo durante 5 minutos."
                } catch (e: Exception) {
                    mensajeError = "Error al procesar la meditación: ${e.localizedMessage}"
                } finally {
                    estaProcesandoMeditation = false
                }
            }
        }
    }