package com.example.habitar.features.autorregulacion.emergencia

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.util.Log
import com.example.habitar.BuildConfig
import com.example.habitar.core.SupabaseConfig
import com.example.habitar.core.SupabaseRepository
import com.google.ai.client.generativeai.GenerativeModel

class EmergenciaViewModel(
    private val repository: EmergenciaRepository = EmergenciaRepository()
) : ViewModel() {

    private val iaRepository = SupabaseRepository(SupabaseConfig.client)
    private val generativeModel = GenerativeModel(
        modelName = "gemini-3.1-flash-lite",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    var recursosVisuales by mutableStateOf<List<RecursoModulo>>(emptyList())
    var recursosAuditivos by mutableStateOf<List<RecursoModulo>>(emptyList())
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
        val esElMismo = recursoAuditivoSeleccionado?.id_recurso == recurso.id_recurso
        recursoAuditivoSeleccionado = if (esElMismo) null else recurso
    }

    fun seleccionarVisual(recurso: RecursoModulo) {
        val esElMismo = recursoVisualSeleccionado?.id_recurso == recurso.id_recurso
        recursoVisualSeleccionado = if (esElMismo) null else recurso
    }

    fun pedirConsejoYRegistrar() {
        mensajeError = null
        guardadoExitoso = null

        if (lugarSeleccionado.isBlank() || comentarioUsuario.isBlank()) {
            mensajeError = "Asegúrate de completar el lugar y tus sensaciones."
            Log.w("DEBUG_EMERGENCIA", "⚠️ Guardado cancelado: Falta completar el lugar o el comentario.")
            return
        }

        viewModelScope.launch {
            estaProcesandoIA = true
            try {
                val prefs = iaRepository.getPreferenciasUsuario()

                val prompt = """
                    Eres el asistente de autorregulación de la App HabiTAR, una aplicación
                    mHealth para estudiantes universitarios con TEA en Chile. 

                    Marco de Actuación: Prioriza la inclusión educativa mediante la gestión socioemocional y 
                    protocolos preventivos ante la desregulación emocional y conductual. Implementa un modelo de 
                    cuidados escalonados que fomente ambientes seguros, identifique gatilladores y active 
                    la calma frente al estrés, fundamentando la gestión de conflictos en la empatía y la 
                    comunicación asertiva.

                    Objetivo: Proporcionar estrategias de autorregulación para emergencias basadas en evidencia científica para neurodiversidad.
                    Considera el $lugarSeleccionado al momento de dar el consejo, este se debe adecuar al entorno actual del usuario.

                    Reglas de Interacción:
                    1. Lenguaje: Literal, calmo, directo y claro. Prohibido: sarcasmo, metáforas, lenguaje figurado o ambigüedades.

                    2. Protocolo:
                    * Validar: Identifica y valida la emoción/sensación del usuario antes de sugerir una solución.
                    * Personalizar: Adapta tus estrategias al perfil sensorial recibido en el JSON preferencias_autorregulacion.
                    * Evidencia: Aplica las estrategias de los documentos adjuntos. Ante síntomas no cubiertos, prioriza la baja carga cognitiva. Ante crisis por ruido, sugiere cancelación o refugios sensoriales.
                    * Naturalidad: No cites las fuentes; integra la información de forma fluida.

                    3. Seguridad: Ante palabras clave de autolesión o crisis severa, prioriza respuestas cortas y de acción inmediata para estabilización.

                    Flujo de trabajo:
                    1. Analizar problema y perfil sensorial.
                    2. Seleccionar estrategia de afrontamiento según evidencia.
                    3. Adaptar respuesta al perfil del usuario.
                    4. Entregar consejo directo.
                    
                    RESPONDE DE FORMA BREVE (MÁXIMO 200 PALABRAS), EMPÁTICA Y CON ESTRATEGIAS CONCRETAS DE AUTORREGULACIÓN.
                    
                    Contexto del usuario:
                    - Sonidos que le ayudan: ${prefs.preferenciasSonidos}
                    - Recursos visuales: ${prefs.preferenciasVisuales}
                    - Debilidades identificadas: ${prefs.debilidadesIdentificadas}
                    (Lo más importante y a considerar)
                    - Lugar actual: $lugarSeleccionado
                    - Lo que siente: $comentarioUsuario
                    
                    
                """.trimIndent()

                val consejoGenerado = generativeModel.generateContent(prompt).text
                    ?: "No pude generar un consejo."
                respuestaIA = consejoGenerado

                val idRecursoAsociado = recursoVisualSeleccionado?.id_recurso
                    ?: recursoAuditivoSeleccionado?.id_recurso

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
                    Log.d("HABITAR_EMERGENCIA", "¡ÉXITO! Datos guardados correctamente.")
                } else {
                    mensajeError = "Se generó el consejo, pero no se pudo respaldar en tu historial."
                    Log.e("HABITAR_EMERGENCIA", "ERROR: El repositorio devolvió 'false'.")
                }

            } catch (e: Exception) {
                mensajeError = "Ocurrió un error en la comunicación: ${e.localizedMessage}"
                Log.e("HABITAR_EMERGENCIA", "EXCEPCIÓN detectada: ${e.localizedMessage}", e)
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