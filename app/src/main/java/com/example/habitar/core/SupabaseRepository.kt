package com.example.habitar.core

import com.example.habitar.data.PreferenciasUsuario
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest // Debes importar esto

class SupabaseRepository(private val cliente: SupabaseClient) {

    suspend fun getPreferenciasUsuario(): PreferenciasUsuario {
        // Accedemos a postgrest directamente desde el cliente
        return cliente.postgrest.from("preferencias_usuario")
            .select()
            .decodeSingle<PreferenciasUsuario>()
    }
}