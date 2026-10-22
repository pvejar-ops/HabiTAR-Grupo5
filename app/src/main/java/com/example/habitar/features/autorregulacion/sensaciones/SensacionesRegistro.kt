package com.example.habitar.features.autorregulacion.sensaciones

import kotlinx.serialization.Serializable

@Serializable
data class SensacionRegistro(
    val id_usuario: String,
    val caract_seleccionada: List<String>, // Mapea directo al text[] de tu bdd
    val comentario_usuario: String,
    val respuesta_ia: String
)