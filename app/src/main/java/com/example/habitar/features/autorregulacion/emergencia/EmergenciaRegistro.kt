package com.example.habitar.features.autorregulacion.emergencia

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

// Representa un recurso disponible en la bdd (para leer de recursos_modulos)
@Serializable
data class RecursoModulo(
    val id_recurso: Int? = null, // Generado automáticamente por Supabase
    val nombre_recurso: String,
    val formato_recurso: String,    // "visual" o "auditivo"
    val url_contenido: String,
    val imagen_miniatura: String? = null
)

@Serializable
data class RegistroEmergencia(
    val id_emergencia: Int? = null, // Nulo al insertar para que Supabase lo genere
    val id_usuario: String, // Cambia a Int si tu ID de usuario es numérico
    val lugar_seleccionado: String,
    val comentario_usuario: String,

    @SerialName("respuesta_ia")
    val respuestaIa: String,

    val id_recurso: Int? = null
)