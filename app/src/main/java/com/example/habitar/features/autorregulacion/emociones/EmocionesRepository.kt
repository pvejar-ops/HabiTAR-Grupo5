package com.example.habitar.features.autorregulacion.emociones

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log
import com.example.habitar.core.SupabaseConfig
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from

class EmocionesRepository {
    private val client = SupabaseConfig.client

    suspend fun guardarRegistroEmocion(
        emociones: List<String>,
        comentario: String,
        respuestaIA: String
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            // 1. Conseguimos de forma segura el UID de la sesión actual
            val userId = client.auth.currentUserOrNull()?.id

            if (userId != null) {
                // 2. Creamos el objeto con los nombres exactos de tus columnas
                val nuevoRegistro = RegistroEmocion(
                    id_usuario = userId,
                    caract_seleccionada = emociones,
                    comentario_usuario = comentario,
                    respuesta_ia = respuestaIA
                )

                // 3. Insertamos en tu nueva tabla individual "historial_emociones"
                client.from("historial_emociones").insert(nuevoRegistro)

                Log.d("HABITAR_EMOCIONES", "Historial de emoción guardado con éxito para: $userId")
                true
            } else {
                Log.e("HABITAR_EMOCIONES", "Error: No se encontró un usuario autenticado.")
                false
            }
        } catch (e: Exception) {
            Log.e("HABITAR_EMOCIONES", "Error al insertar en la tabla: ${e.message}")
            e.printStackTrace()
            false
        }
    }
}