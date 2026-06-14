package com.example.habitar

import android.app.Application
import com.example.habitar.core.SupabaseConfig

class HabitarApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SupabaseConfig.init(this)
    }
}