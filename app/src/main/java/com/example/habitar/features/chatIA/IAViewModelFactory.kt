package com.example.habitar.features.chatIA

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habitar.core.SupabaseRepository

class IAViewModelFactory(private val repository: SupabaseRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return IAViewModel(repository) as T
    }
}