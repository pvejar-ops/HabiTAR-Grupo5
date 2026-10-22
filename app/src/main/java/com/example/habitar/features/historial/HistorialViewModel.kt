package com.example.habitar.features.historial

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class HistorialViewModel : ViewModel() {

    private val repository = HistorialRepository()

    var resumenDias by mutableStateOf<Map<String, ResumenDia>>(emptyMap())
        private set
    var estaCargando by mutableStateOf(false)
        private set
    var diaSeleccionado by mutableStateOf<ResumenDia?>(null)
        private set

    init {
        cargarHistorial()
    }

    private fun cargarHistorial() {
        viewModelScope.launch {
            estaCargando = true
            try {
                resumenDias = repository.obtenerResumenDias()
            } catch (e: Exception) {
                // manejar error
            } finally {
                estaCargando = false
            }
        }
    }

    fun seleccionarDia(fecha: String) {
        diaSeleccionado = resumenDias[fecha]
    }

    fun limpiarSeleccion() {
        diaSeleccionado = null
    }
}