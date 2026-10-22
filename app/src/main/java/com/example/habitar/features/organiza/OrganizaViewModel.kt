package com.example.habitar.features.organiza

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class OrganizaViewModel : ViewModel() {
    private val repository = OrganizaRepository()

    var listaTareas by mutableStateOf<List<OrganizaRegistro>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun cargarTareas(fecha: String) {
        viewModelScope.launch {
            isLoading = true
            listaTareas = repository.obtenerTareasPorFecha(fecha)
            isLoading = false
        }
    }

    fun agregarTarea(descripcion: String, fecha: String) {
        if (descripcion.isBlank()) return
        viewModelScope.launch {
            val exito = repository.guardarTarea(descripcion, fecha)
            if (exito) {
                cargarTareas(fecha) // Refresca la lista
            }
        }
    }

    fun cambiarEstadoTarea(tarea: OrganizaRegistro, completada: Boolean) {
        val id = tarea.idTarea ?: return
        viewModelScope.launch {
            // Cambio local veloz para el usuario
            listaTareas = listaTareas.map {
                if (it.idTarea == id) it.copy(completada = completada) else it
            }
            repository.cambiarEstadoCheck(id, completada)
        }
    }

    fun eliminarTarea(tarea: OrganizaRegistro, fecha: String) {
        val id = tarea.idTarea ?: return
        viewModelScope.launch {
            val exito = repository.eliminarTareaFisicamente(id)
            if (exito) {
                cargarTareas(fecha)
            }
        }
    }
}