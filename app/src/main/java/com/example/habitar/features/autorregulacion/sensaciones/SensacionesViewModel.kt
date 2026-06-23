package com.example.habitar.features.autorregulacion.sensaciones

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.habitar.core.SupabaseConfig
import com.example.habitar.core.SupabaseRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.example.habitar.BuildConfig
import android.util.Log

class SensacionesViewModel(
    private val repository: SensacionesRepository = SensacionesRepository()
) : ViewModel() {

    private val iaRepository = SupabaseRepository(SupabaseConfig.client)

    private val generativeModel = GenerativeModel(
        modelName = "gemini-3.1-flash-lite",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

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
        sensacionesSeleccionadas = if (sensacionesSeleccionadas.contains(sensacion)) {
            sensacionesSeleccionadas - sensacion
        } else {
            sensacionesSeleccionadas + sensacion
        }
    }

    private val TAG = "IA_Debug"

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
                Log.d(TAG, "Iniciando consulta IA para sensaciones...")

                val prefs = iaRepository.getPreferenciasUsuario()

                val prompt = """
                    Eres el asistente de autorregulación de la App HabiTAR, una aplicación mHealth para estudiantes universitarios con TEA en Chile.

                    Marco de Actuación: Prioriza la inclusión educativa mediante la gestión socioemocional y protocolos preventivos ante la desregulación emocional y conductual. Implementa un modelo de cuidados escalonados que fomente ambientes seguros, identifique gatilladores y active la calma frente al estrés, fundamentando la gestión de conflictos en la empatía y la comunicación asertiva.
                    
                    Objetivo: Proporcionar estrategias de autorregulación para sensaciones físicas basadas en evidencia científica para neurodiversidad.
                    
                    Reglas de Interacción:
                    
                    1. Lenguaje: Literal, calmo, directo y claro. Prohibido: sarcasmo, metáforas, lenguaje figurado o ambigüedades.
                    
                    2. Protocolo:
                    * Validar: Identifica y valida la emoción/sensación del usuario antes de sugerir una solución.
                    * Personalizar: Adapta tus estrategias al perfil del usuario (preferencias sensoriales y debilidades), pero solo si tienen sentido en el contexto. No menciones literalmente los datos del perfil si no aportan al consejo. Si los datos parecen ingresados sin seriedad o no tienen coherencia (por ejemplo, preferencias sin sentido), ignóralos y entrega una respuesta general. No adaptes consejos basados en información que parezca incorrecta o ingresada como prueba.
                    * Evidencia: Aplica estrategias basadas en evidencia para neurodiversidad. Ante síntomas no cubiertos, prioriza la baja carga cognitiva. Ante crisis por ruido, sugiere cancelación o refugios sensoriales. Integra la información de forma fluida, sin citar fuentes.
                    * Naturalidad: No menciones que estás usando un perfil o datos del usuario. Simplemente adapta tu respuesta de forma natural.
                    
                    3. Seguridad:
                    * Ante palabras clave de autolesión, crisis severa, ideación suicida o situaciones de emergencia emocional, responde de forma corta, directa y con acción inmediata para estabilización.
                    * Incluye siempre los siguientes recursos de ayuda disponibles en Chile:
                      - Fono Salud Responde: 600 360 7777 (24 horas, gratuito)
                      - Línea Especializada de Prevención del Suicidio: *4141 (gratuito, disponible las 24 horas)
                      - Centro de Crisis más cercano: sugiere que contacte a su universidad o CESFAM más cercano.
                    * No entregues estos recursos ante consultas cotidianas o leves. Solo ante señales claras de riesgo o emergencia, tales como suicidas.
                    
                    4. Formato de respuesta:
                    * No hagas preguntas al usuario al final de tu respuesta.

                    5. Uso no serio de la app:
                    * Si el usuario envía mensajes sin sentido, provocadores, de prueba evidente, o que no tienen relación con autorregulación emocional o sensorial, responde de forma breve, neutra y redirige amablemente hacia el propósito de la app. No entres en el juego ni respondas fuera del marco de HabiTAR.
                                        
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
                    - El usuario experimenta estas sensaciones físicas: ${sensacionesSeleccionadas.joinToString()}.
                    - Comentario usuario (importante): $comentarioUsuario
                """.trimIndent()

                Log.d(TAG, "Enviando prompt a Gemini...")
                val respuestaGenerada = generativeModel.generateContent(prompt).text
                    ?: "No pude generar un consejo."
                Log.d(TAG, "Respuesta recibida: $respuestaGenerada")

                respuestaIA_Visualizar = respuestaGenerada

                val exito = repository.guardarRegistroSensacion(
                    sensaciones = sensacionesSeleccionadas,
                    comentario = comentarioUsuario.trim(),
                    respuestaIA = respuestaGenerada
                )
                Log.d(TAG, "Guardado en BDD: ${if (exito) "Éxito" else "Fallo"}")

                if (exito) {
                    procesoExitoso = true
                    comentarioUsuario = ""
                } else {
                    mensajeError = "Se generó el consejo, pero no se pudo conectar con tu historial."
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error en el flujo de sensaciones: ${e.localizedMessage}", e)
                mensajeError = "Error en el flujo: ${e.localizedMessage}"
            } finally {
                estaProcesando = false
                Log.d(TAG, "Proceso finalizado.")
            }
        }
    }
}