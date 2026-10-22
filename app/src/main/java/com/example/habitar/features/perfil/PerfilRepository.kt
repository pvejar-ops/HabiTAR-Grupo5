package com.example.habitar.features.perfil

import android.util.Log
import com.example.habitar.core.SupabaseConfig
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PerfilRepository {
    private val client = SupabaseConfig.client

    // 1. Obtener los datos del perfil del usuario actual
    suspend fun obtenerPerfilUsuario(): PerfilRegistro? = withContext(Dispatchers.IO) {
        try {
            val userId = client.auth.currentUserOrNull()?.id

            if (userId != null) {
                val response = client.from("perfil_usuario")
                    .select {
                        filter {
                            eq("id_usuario", userId) // Filtramos por el ID único del usuario
                        }
                    }
                    .decodeSingle<PerfilRegistro>()

                Log.d("PERFIL_REPO", "Perfil obtenido correctamente para usuario: $userId")
                response
            } else {
                Log.e("PERFIL_REPO", "Usuario no autenticado para obtener perfil.")
                null
            }
        } catch (e: Exception) {
            Log.e("PERFIL_REPO", "Error al cargar perfil: ${e.localizedMessage}", e)
            null
        }
    }

    // 2. Actualizar datos del perfil
    suspend fun actualizarPerfil(perfilActualizado: PerfilRegistro): Boolean = withContext(Dispatchers.IO) {
        try {
            val userId = client.auth.currentUserOrNull()?.id

            if (userId != null) {
                client.from("perfil_usuario")
                    .update({
                        // Definimos los campos que cambian
                        set("nombre_usuario", perfilActualizado.nombre_usuario)
                        set("edad", perfilActualizado.edad)
                        set("carrera", perfilActualizado.carrera)
                    }) {
                        // Filtramos usando el 'eq' que ya importaste
                        filter { eq("id_usuario", userId) }
                    }
                Log.d("PERFIL_REPO", "Perfil actualizado con éxito.")
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e("PERFIL_REPO", "Error al actualizar perfil: ${e.localizedMessage}", e)
            false
        }
    }

    suspend fun cerrarSesion(): Boolean {
        return try {
            client.auth.signOut()
            true
        } catch (e: Exception) {
            Log.e("PERFIL_REPO", "Error al cerrar sesión: ${e.message}")
            false
        }
    }

}