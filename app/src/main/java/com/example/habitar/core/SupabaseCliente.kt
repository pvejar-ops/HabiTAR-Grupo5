package com.example.habitar.core

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.SettingsSessionManager
import android.content.Context
import io.github.jan.supabase.SupabaseClient

object SupabaseConfig {
    const val SUPABASE_URL = "https://knocdehqpunqiotfxtuf.supabase.co"
    const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imtub2NkZWhxcHVucWlvdGZ4dHVmIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Nzc5MzU2MDEsImV4cCI6MjA5MzUxMTYwMX0.EpJdIeS3IPY9RW7iFhMAx9eKuT-Zdp59tcKSvEFUFJI"

    lateinit var client: SupabaseClient
        private set

    fun init(context: Context) {
        client = createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_KEY
        ) {
            install(Postgrest)
            install(Auth) {
                scheme = "habitar"
                host = "reset-password"
            }
        }
    }
}