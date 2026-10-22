package com.example.habitar.features.autorregulacion.meditacion

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log
import com.example.habitar.core.SupabaseConfig
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from

class MeditacionRepository {
    private val client = SupabaseConfig.client

    // 1. Obtener todos los recursos del catálogo
    // Asegúrate de que la firma de la función devuelva una List de RecursoModulo
    suspend fun obtenerRecursos(): List<RecursoModulo> = withContext(Dispatchers.IO) {
        try {
            // 1. Hacemos la consulta a la tabla de Supabase
            val response = client.from("recursos_modulos")
                .select {
                    filter {
                        eq("modulo_recurso", "meditacion")
                    }
                }
                .decodeList<RecursoModulo>()

            // 2. Imprimimos en el log dentro del try (donde existe la variable)
            Log.d("HABITAR_MEDITACION", "Recursos obtenidos con éxito: ${response.size}")

            // 3. Retornamos la lista exitosa
            response
        } catch (e: Exception) {
            // Si algo falla (red, deserialización, etc.), atrapamos el error y devolvemos una lista vacía
            Log.e("HABITAR_MEDITACION", "Error al obtener recursos: ${e.localizedMessage}", e)
            emptyList()
        }
    }


    suspend fun registrarEntradaMeditacion(): Boolean = withContext(Dispatchers.IO) {
        try {
            val userId = client.auth.currentUserOrNull()?.id
            if (userId != null) {
                val registro = MeditacionRegistro(
                    id_usuario = userId,
                    id_recurso = null
                )
                client.from("historial_meditacion").insert(registro)
                Log.d("HABITAR_MEDITACION", "Entrada a meditación registrada.")
                true
            } else {
                Log.e("HABITAR_MEDITACION", "Usuario no autenticado.")
                false
            }
        } catch (e: Exception) {
            Log.e("HABITAR_MEDITACION", "Error al registrar entrada: ${e.message}")
            false
        }
    }
}