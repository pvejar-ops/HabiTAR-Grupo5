package com.example.habitar.core

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.gotrue.Auth
import android.content.Context
import io.github.jan.supabase.SupabaseClient
import com.example.habitar.BuildConfig

object SupabaseConfig {
    const val SUPABASE_URL = BuildConfig.SUPABASE_URL
    const val SUPABASE_KEY = BuildConfig.SUPABASE_KEY

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