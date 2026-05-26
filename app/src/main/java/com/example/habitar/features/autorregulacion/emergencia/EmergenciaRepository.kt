package com.example.habitar.features.autorregulacion.emergencia

import android.util.Log
import com.example.habitar.core.SupabaseConfig
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EmergenciaRepository {
    private val client = SupabaseConfig.client

    // 1. Obtener recursos auditivos exclusivos de tipo "emergencia"
    suspend fun obtenerRecursosEmergencia(): List<RecursoModulo> = withContext(Dispatchers.IO) {
        try {
            val response = client.from("recursos_modulos")
                .select {
                    filter {
                        eq("modulo_recurso", "emergencia")
                    }
                }
                .decodeList<RecursoModulo>()

            // Log opcional para verificar en el Logcat cuántos audios de emergencia se bajaron
            Log.d("EMERGENCIA_REPO", "Audios de emergencia obtenidos: ${response.size}")

            // Retornamos la respuesta exitosa
            response
        } catch (e: Exception) {
            Log.e("EMERGENCIA_REPO", "Error al cargar audios de emergencia: ${e.localizedMessage}", e)
            emptyList()
        }
    }

    // 2. Guardar el registro en la tabla historial_emergencia
    suspend fun guardarRegistroEmergencia(
        lugar: String,
        comentario: String,
        respuestaIa: String,
        idRecurso: Int?
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            // Obtenemos el UUID del usuario autenticado desde Supabase Auth
            val userId = client.auth.currentUserOrNull()?.id

            if (userId != null) {
                // 🌟 Usamos tu modelo tal cual lo creaste para que coincida con tu esquema
                val registro = RegistroEmergencia(
                    id_usuario = userId,
                    lugar_seleccionado = lugar,
                    comentario_usuario = comentario,
                    respuestaIa = respuestaIa,
                    id_recurso = idRecurso
                )

                // Insertamos apuntando a la tabla real de tu BDD: "historial_emergencia"
                client.from("historial_emergencia").insert(registro)
                Log.d("EMERGENCIA_REPO", "Registro de emergencia guardado con éxito en el historial.")
                true
            } else {
                Log.e("EMERGENCIA_REPO", "Usuario no autenticado.")
                false
            }
        } catch (e: Exception) {
            Log.e("EMERGENCIA_REPO", "Error al guardar en historial_emergencia: ${e.localizedMessage}", e)
            false
        }
    }

    // 3. Generador de respuestas rápidas de IA (Simulado o expandible a API real)
    fun generarConsejoIA(lugar: String, descripcion: String): String {
        return when (lugar) {
            "Universidad" -> "Estás en la Universidad. Busca un lugar tranquilo como la biblioteca o un patio despejado. Cierra los ojos, escucha el audio de respiración y recuerda que este momento va a pasar. Estás a salvo."
            "Vía pública" -> "Estás en la calle. Si es seguro, camina hacia la pared o siéntate en un lugar firme. Concéntrate en sentir tus pies en el suelo. Sigue el ritmo del audio de emergencia. Todo está bien."
            "Casa" -> "Estás en casa, un lugar seguro. Recuéstate o siéntate cómodamente. Pon el audio de contención a volumen medio, inhala en 4 tiempos y exhala lento. No estás solo/a."
            else -> "Respira profundo. Concéntrate en los sonidos que te rodean. Escucha el audio seleccionado para ayudarte a estabilizar tu ritmo cardíaco."
        }
    }
}