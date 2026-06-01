package com.example.habitar.core

import com.example.habitar.core.navegacion.Screen
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.gotrue.Auth

object SupabaseConfig {
    const val SUPABASE_URL = "https://knocdehqpunqiotfxtuf.supabase.co"
    const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imtub2NkZWhxcHVucWlvdGZ4dHVmIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Nzc5MzU2MDEsImV4cCI6MjA5MzUxMTYwMX0.EpJdIeS3IPY9RW7iFhMAx9eKuT-Zdp59tcKSvEFUFJI"

    // 3. Inicialización del cliente
    val client = createSupabaseClient(SUPABASE_URL, SUPABASE_KEY) {
        install(Postgrest) // Para leer/escribir en las tablas de la base de datos
        install(Auth) // Para manejar los inicios de sesión y registros
    }
}
