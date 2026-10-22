package com.example.habitar.features.autorregulacion.sensaciones

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log
import com.example.habitar.core.SupabaseConfig
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from

class SensacionesRepository {
    private val client = SupabaseConfig.client

    suspend fun guardarRegistroSensacion(
        sensaciones: List<String>,
        comentario: String,
        respuestaIA: String
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val userId = client.auth.currentUserOrNull()?.id

            if (userId != null) {
                val nuevoRegistro = SensacionRegistro(
                    id_usuario = userId,
                    caract_seleccionada = sensaciones,
                    comentario_usuario = comentario,
                    respuesta_ia = respuestaIA
                )

                // Insertamos en tu tabla específica de sensaciones
                client.from("historial_sensaciones").insert(nuevoRegistro)

                Log.d("HABITAR_SENSACIONES", "Historial de sensaciones físicas guardado con éxito.")
                true
            } else {
                Log.e("HABITAR_SENSACIONES", "Error: Usuario no autenticado.")
                false
            }
        } catch (e: Exception) {
            Log.e("HABITAR_SENSACIONES", "Error al insertar en historial_sensaciones: ${e.message}")
            e.printStackTrace()
            false
        }
    }
}