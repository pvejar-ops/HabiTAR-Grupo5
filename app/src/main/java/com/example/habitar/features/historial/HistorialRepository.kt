package com.example.habitar.features.historial

import com.example.habitar.core.SupabaseConfig
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.Serializable
import io.github.jan.supabase.gotrue.auth

@Serializable
data class RegistroHistorial(
    val fecha_registro: String,
    val modulo: String
)

@Serializable
data class RegistroEmocion(
    val fecha_registro: String,
    val emociones: List<String>? = null,
    val comentario: String? = null,
    val respuesta_ia: String? = null
)

@Serializable
data class RegistroSensacion(
    val fecha_registro: String,
    val sensaciones: List<String>? = null,
    val comentario: String? = null,
    val respuesta_ia: String? = null
)

@Serializable
data class RegistroEmergencia(
    val fecha_registro: String,
    val lugar: String? = null,
    val comentario: String? = null,
    val respuesta_ia: String? = null
)

@Serializable
data class RegistroMeditacion(
    val fecha_registro: String
)

data class ResumenDia(
    val fecha: String,
    val emociones: List<RegistroEmocion> = emptyList(),
    val sensaciones: List<RegistroSensacion> = emptyList(),
    val emergencias: List<RegistroEmergencia> = emptyList(),
    val meditaciones: List<RegistroMeditacion> = emptyList()
)

class HistorialRepository {
    private val client = SupabaseConfig.client

    suspend fun obtenerResumenDias(): Map<String, ResumenDia> {
        val userId = SupabaseConfig.client.auth.currentUserOrNull()?.id
            ?: return emptyMap()

        val emociones = client.from("historial_emociones")
            .select { filter { eq("id_usuario", userId) } }
            .decodeList<RegistroEmocion>()

        val sensaciones = client.from("historial_sensaciones")
            .select { filter { eq("id_usuario", userId) } }
            .decodeList<RegistroSensacion>()

        val emergencias = client.from("historial_emergencia")
            .select { filter { eq("id_usuario", userId) } }
            .decodeList<RegistroEmergencia>()

        val meditaciones = client.from("historial_meditacion")
            .select { filter { eq("id_usuario", userId) } }
            .decodeList<RegistroMeditacion>()

        val resumenMap = mutableMapOf<String, ResumenDia>()

        emociones.forEach { reg ->
            val fecha = reg.fecha_registro.take(10)
            val actual = resumenMap[fecha] ?: ResumenDia(fecha)
            resumenMap[fecha] = actual.copy(emociones = actual.emociones + reg)
        }

        sensaciones.forEach { reg ->
            val fecha = reg.fecha_registro.take(10)
            val actual = resumenMap[fecha] ?: ResumenDia(fecha)
            resumenMap[fecha] = actual.copy(sensaciones = actual.sensaciones + reg)
        }

        emergencias.forEach { reg ->
            val fecha = reg.fecha_registro.take(10)
            val actual = resumenMap[fecha] ?: ResumenDia(fecha)
            resumenMap[fecha] = actual.copy(emergencias = actual.emergencias + reg)
        }

        meditaciones.forEach { reg ->
            val fecha = reg.fecha_registro.take(10)
            val actual = resumenMap[fecha] ?: ResumenDia(fecha)
            resumenMap[fecha] = actual.copy(meditaciones = actual.meditaciones + reg)
        }

        return resumenMap
    }
}