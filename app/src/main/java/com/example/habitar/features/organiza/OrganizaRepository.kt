package com.example.habitar.features.organiza

import android.util.Log
import com.example.habitar.core.SupabaseConfig
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OrganizaRepository {
    private val client = SupabaseConfig.client
    private val TABLA_TAREAS = "tareas"

    // 1. Obtener tareas del usuario logueado para una fecha específica
    suspend fun obtenerTareasPorFecha(fecha: String): List<OrganizaRegistro> = withContext(Dispatchers.IO) {
        try {
            val userId = client.auth.currentUserOrNull()?.id
            if (userId != null) {
                val response = client.from(TABLA_TAREAS)
                    .select {
                        filter {
                            eq("id_usuario", userId)
                            eq("fecha_tarea", fecha)
                        }
                    }
                    .decodeList<OrganizaRegistro>()

                Log.d("ORGANIZA_REPO", "Tareas obtenidas para el día $fecha: ${response.size}")
                response
            } else {
                Log.e("ORGANIZA_REPO", "Usuario no autenticado al intentar leer tareas.")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("ORGANIZA_REPO", "Error al cargar tareas de la BDD: ${e.localizedMessage}", e)
            emptyList()
        }
    }

    // 2. Insertar una nueva tarea en la tabla
    suspend fun guardarTarea(descripcion: String, fecha: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val userId = client.auth.currentUserOrNull()?.id
            if (userId != null) {
                val nuevaTarea = OrganizaRegistro(
                    descripcionTarea = descripcion,
                    fechaTarea = fecha,
                    idUsuario = userId,
                    completada = false
                )

                client.from(TABLA_TAREAS).insert(nuevaTarea)
                Log.d("ORGANIZA_REPO", "Nueva tarea guardada con éxito.")
                true
            } else {
                Log.e("ORGANIZA_REPO", "Usuario no autenticado al intentar guardar tarea.")
                false
            }
        } catch (e: Exception) {
            Log.e("ORGANIZA_REPO", "Error al insertar tarea en la BDD: ${e.localizedMessage}", e)
            false
        }
    }

    // 3. Actualizar el estado de completada (Check) en Supabase
    suspend fun cambiarEstadoCheck(idTarea: Long, nuevoEstado: Boolean): Boolean = withContext(Dispatchers.IO) {
        try {
            client.from(TABLA_TAREAS).update({
                set("completada", nuevoEstado)
            }) {
                filter {
                    eq("id_tarea", idTarea)
                }
            }
            Log.d("ORGANIZA_REPO", "Estado del check actualizado a $nuevoEstado para idTarea: $idTarea")
            true
        } catch (e: Exception) {
            Log.e("ORGANIZA_REPO", "Error al actualizar check en la BDD: ${e.localizedMessage}", e)
            false
        }
    }

    // 4. Eliminar físicamente una tarea por su ID primario
    suspend fun eliminarTareaFisicamente(idTarea: Long): Boolean = withContext(Dispatchers.IO) {
        try {
            client.from(TABLA_TAREAS).delete {
                filter {
                    eq("id_tarea", idTarea)
                }
            }
            Log.d("ORGANIZA_REPO", "Tarea eliminada exitosamente. ID: $idTarea")
            true
        } catch (e: Exception) {
            Log.e("ORGANIZA_REPO", "Error al eliminar tarea de la BDD: ${e.localizedMessage}", e)
            false
        }
    }
}