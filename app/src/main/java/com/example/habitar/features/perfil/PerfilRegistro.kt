package com.example.habitar.features.perfil

import kotlinx.serialization.Serializable

@Serializable
data class PerfilRegistro(
    // ID opcional (nullable) para que Supabase lo maneje al insertar/actualizar
    val id_usuario: String? = null,
    val nombre_usuario: String,
    val correo_electronico: String,
    val edad: Int,
    val carrera: String? = null // Lo marcamos como nullable porque en tu BD algunos valores son NULL
)