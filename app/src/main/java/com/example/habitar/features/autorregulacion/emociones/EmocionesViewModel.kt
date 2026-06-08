package com.example.habitar.features.autorregulacion.emociones

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitar.core.SupabaseConfig
import com.example.habitar.core.SupabaseRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.example.habitar.BuildConfig
import kotlinx.coroutines.launch
import android.util.Log

class EmocionesViewModel(
    private val repository: EmocionesRepository = EmocionesRepository()
) : ViewModel() {

    private val iaRepository = SupabaseRepository(SupabaseConfig.client)

    private val generativeModel = GenerativeModel(
        modelName = "gemini-3.1-flash-lite",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    var emocionesSeleccionadas by mutableStateOf<List<String>>(emptyList())
    var comentarioUsuario by mutableStateOf("")
    var estaProcesando by mutableStateOf(false)
        private set
    var respuestaIA_Visualizar by mutableStateOf("")
        private set
    var mensajeError by mutableStateOf<String?>(null)
        private set
    var procesoExitoso by mutableStateOf(false)
        private set

    fun alternarEmocion(emocion: String) {
        emocionesSeleccionadas = if (emocionesSeleccionadas.contains(emocion)) {
            emocionesSeleccionadas - emocion
        } else {
            emocionesSeleccionadas + emocion
        }
    }

    private val TAG = "IA_Debug"

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
                Log.d(TAG, "Iniciando proceso de consulta a IA...")

                val prefs = iaRepository.getPreferenciasUsuario()
                Log.d(TAG, "Preferencias obtenidas: $prefs")

                val prompt = """
                    Eres el asistente de autorregulación de la App HabiTAR, una aplicación
                    mHealth para estudiantes universitarios con TEA en Chile. 

                    Marco de Actuación: Prioriza la inclusión educativa mediante la gestión socioemocional y 
                    protocolos preventivos ante la desregulación emocional y conductual. Implementa un modelo de 
                    cuidados escalonados que fomente ambientes seguros, identifique gatilladores y active 
                    la calma frente al estrés, fundamentando la gestión de conflictos en la empatía y la 
                    comunicación asertiva.

                    Objetivo: Proporcionar estrategias de autorregulación para emociones basadas en evidencia científica para neurodiversidad.

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
                    (Lo más importante)
                    - Lo que siente: ${emocionesSeleccionadas.joinToString()}.
                    - Comentario usuario (importante): $comentarioUsuario
                """.trimIndent()

                Log.d(TAG, "Enviando prompt a Gemini...")
                val respuestaGenerada = generativeModel.generateContent(prompt).text
                    ?: "No pude generar un consejo."
                Log.d(TAG, "Respuesta recibida: $respuestaGenerada")

                respuestaIA_Visualizar = respuestaGenerada

                val exitoBDD = repository.guardarRegistroEmocion(
                    emociones = emocionesSeleccionadas,
                    comentario = comentarioUsuario.trim(),
                    respuestaIA = respuestaGenerada
                )
                Log.d(TAG, "Guardado en BDD: ${if (exitoBDD) "Éxito" else "Fallo"}")

                procesoExitoso = exitoBDD
                if (exitoBDD) comentarioUsuario = ""

            } catch (e: Exception) {
                Log.e(TAG, "Error durante el flujo de IA: ${e.localizedMessage}", e)
                mensajeError = "Error: ${e.localizedMessage}"
            } finally {
                estaProcesando = false
                Log.d(TAG, "Proceso finalizado.")
            }
        }
    }
}