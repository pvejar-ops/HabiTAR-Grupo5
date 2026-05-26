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
    }

    private fun cargarCatalogoRecursos() {
        viewModelScope.launch {
            estaCargandoRecursos = true
            val todos = repository.obtenerRecursos()
            // Filtramos dinámicamente por la columna tipo_recurso
            recursosVisuales = todos.filter { it.formato_recurso == "visual" }
            recursosAuditivos = todos.filter { it.formato_recurso == "auditivo" }
            estaCargandoRecursos = false
        }
    }

    // GUARDADO AUTOMÁTICO AL SELECCIONAR UN AUDIO
    fun seleccionarAudio(recurso: RecursoModulo) {
        val esElMismo = recursoAuditivoSeleccionado?.id_recurso == recurso.id_recurso
        recursoAuditivoSeleccionado = if (esElMismo) null else recurso

        // Si lo acaba de seleccionar (y no desmarcar), lo guardamos en la BDD inmediatamente
        if (!esElMismo) {
            recurso.id_recurso?.let { id ->
                registrarInteraccionEnSegundoPlano(id)
            }
        }
    }

    // GUARDADO AUTOMÁTICO AL SELECCIONAR UN VISUAL
    fun seleccionarVisual(recurso: RecursoModulo) {
        val esElMismo = recursoVisualSeleccionado?.id_recurso == recurso.id_recurso
        recursoVisualSeleccionado = if (esElMismo) null else recurso

        if (!esElMismo) {
            recurso.id_recurso?.let { id ->
                registrarInteraccionEnSegundoPlano(id)
            }
        }
    }

    // Función silenciosa que hace el insert sin molestar al usuario con pantallas de carga
    private fun registrarInteraccionEnSegundoPlano(idRecurso: Int) {
        viewModelScope.launch {
            repository.guardarHistorialMeditacion(idRecurso)
        }
    }

    fun iniciarMeditacion() {
        mensajeError = null
        // Validar que haya seleccionado al menos un tipo de recurso
        if (recursoVisualSeleccionado == null && recursoAuditivoSeleccionado == null) {
            mensajeError = "Por favor selecciona al menos un recurso para meditar."
            return
        }

        viewModelScope.launch {
            estaProcesandoMeditation = true
            try {
                // Simulamos la respuesta de la IA indicando qué está consumiendo
                delay(1800)
                val visualNom = recursoVisualSeleccionado?.nombre_recurso ?: "Ninguno"
                val auditivoNom = recursoAuditivoSeleccionado?.nombre_recurso ?: "Ninguno"
                val consejoSimulado = "HabiTAR ha configurado tu entorno de meditación. Estás usando el recurso visual '$visualNom' y el recurso auditivo '$auditivoNom'. Mantén tu espalda recta, enfoca tu mirada sutilmente y sigue el ritmo durante 5 minutos."

                respuestaIA_Visualizar = consejoSimulado

                // Guardamos en historial de meditación (usamos el visual preferentemente o el auditivo)
                val idRecursoAGuardar = recursoVisualSeleccionado?.id_recurso
                    ?: recursoAuditivoSeleccionado?.id_recurso
                    ?: 0

                repository.guardarHistorialMeditacion(idRecursoAGuardar)

            } catch (e: Exception) {
                mensajeError = "Error al procesar la meditación: ${e.localizedMessage}"
            } finally {
                estaProcesandoMeditation = false
            }
        }
    }
}