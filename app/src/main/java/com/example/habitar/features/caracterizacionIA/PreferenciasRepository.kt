package com.example.habitar.features.caracterizacionIA

import com.example.habitar.core.SupabaseConfig
import com.example.habitar.data.PreferenciasUsuario
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PreferenciasRepository {
    private val client = SupabaseConfig.client

    suspend fun guardarPreferenciasIniciales(
        preferenciasSonidos: String,
        preferenciasVisuales: String,
        debilidadesIdentificadas: String
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            // Obtener el ID del usuario logueado actualmente en Supabase Auth
            val userId = client.auth.currentUserOrNull()?.id

            if (userId != null) {
                // 2. Creamos el objeto incluyendo el idUsuario para respetar la clave foránea
                val nuevasPreferencias = PreferenciasUsuario(
                    idUsuario = userId, // Relación directa con perfil_usuario
                    preferenciasSonidos = preferenciasSonidos,
                    preferenciasVisuales = preferenciasVisuales,
                    debilidadesIdentificadas = debilidadesIdentificadas
                )

                // 3. Insertamos en tu tabla destino (asumiendo que se llama "preferencias_usuario")
                client.from("preferencias_usuario").insert(nuevasPreferencias)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}