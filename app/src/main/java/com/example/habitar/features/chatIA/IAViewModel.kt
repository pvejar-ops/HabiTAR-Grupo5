package com.example.habitar.features.chatIA

import androidx.lifecycle.ViewModel
import com.google.ai.client.generativeai.GenerativeModel
import com.example.habitar.BuildConfig
import com.example.habitar.core.SupabaseRepository

class IAViewModel(private val repository: SupabaseRepository) : ViewModel() {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-3.1-flash-lite",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    suspend fun obtenerConsejoIA(inputUsuario: String): String {
        return try {
            val preferencias = repository.getPreferenciasUsuario()

            val prompt = """
                Eres el asistente de autorregulación de la App HabiTAR, una aplicación
                mHealth para estudiantes universitarios con TEA. Responde de forma
                empática, breve y con estrategias concretas de autorregulación.
                
                Contexto del usuario:
                - Le ayudan estos sonidos: ${preferencias.preferenciasSonidos}
                - Prefiere estos recursos visuales: ${preferencias.preferenciasVisuales}
                - Sus debilidades son: ${preferencias.debilidadesIdentificadas}
                
                Situación actual del usuario: "$inputUsuario"
                
                Responde dando un consejo breve, empático y que aproveche 
                sus preferencias de autorregulación.
            """.trimIndent()

            val response = generativeModel.generateContent(prompt)
            response.text ?: "Lo siento, hubo un problema al generar el consejo."

        } catch (e: Exception) {
            "Error al obtener consejo: ${e.message}"
        }
    }
}