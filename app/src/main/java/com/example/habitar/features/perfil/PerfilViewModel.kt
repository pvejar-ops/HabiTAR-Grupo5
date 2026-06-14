package com.example.habitar.features.perfil

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PerfilViewModel(
    private val repository: PerfilRepository = PerfilRepository()
) : ViewModel() {

    // Estado principal del perfil
    var perfil by mutableStateOf<PerfilRegistro?>(null)

    // Estados de UI para manejar la carga y errores
    var estaCargando by mutableStateOf(false)
    var mensajeError by mutableStateOf<String?>(null)
    var operacionExitosa by mutableStateOf<Boolean?>(null)

    init {
        cargarPerfil()
    }

    fun cargarPerfil() {
        viewModelScope.launch {
            estaCargando = true
            mensajeError = null
            try {
                val datos = repository.obtenerPerfilUsuario()
                if (datos != null) {
                    perfil = datos
                } else {
                    mensajeError = "No se pudo encontrar tu perfil."
                }
            } catch (e: Exception) {
                mensajeError = "Error al conectar con la base de datos: ${e.localizedMessage}"
                Log.e("PERFIL_VM", "Error en cargarPerfil: ${e.localizedMessage}")
            } finally {
                estaCargando = false
            }
        }
    }

    fun guardarCambios(nombre: String, edad: Int, carrera: String) {
        viewModelScope.launch {
            operacionExitosa = null
            mensajeError = null

            // Creamos un objeto con los datos nuevos
            val perfilActualizado = PerfilRegistro(
                nombre_usuario = nombre,
                correo_electronico = perfil?.correo_electronico ?: "", // Mantenemos el correo actual
                edad = edad,
                carrera = carrera
            )

            val exito = repository.actualizarPerfil(perfilActualizado)

            if (exito) {
                perfil = perfilActualizado
                operacionExitosa = true
                Log.d("PERFIL_VM", "Cambios guardados correctamente.")
            } else {
                mensajeError = "No se pudieron guardar los cambios."
            }
        }
    }

    fun cerrarSesion(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            val exito = repository.cerrarSesion()
            if (exito) {
                onLogoutSuccess() // Aquí navegarás de vuelta a la pantalla de Login
            }
        }
    }

    fun limpiarPerfil() {
        perfil = null
        mensajeError = null
        operacionExitosa = null
    }

}