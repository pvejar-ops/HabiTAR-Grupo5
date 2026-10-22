package com.example.habitar.features.autorregulacion.meditacion

import kotlinx.serialization.Serializable

// Representa un recurso disponible en la bdd (para leer de recursos_modulos)
@Serializable
data class RecursoModulo(
    val id_recurso: Int? = null, // Generado automáticamente por Supabase
    val nombre_recurso: String,
    val formato_recurso: String,    // "visual" o "auditivo"
    val url_contenido: String,
    val imagen_miniatura: String? = null
)

// Representa la fila que guardaremos en historial_meditacion
@Serializable
data class MeditacionRegistro(
    val id_usuario: String,
    val id_recurso: Int? = null
)