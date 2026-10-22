package com.example.habitar.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PerfilUsuario(
    @SerialName("id_usuario") // <-- Nombre exacto en tu BDD
    val idUsuario: String,

    @SerialName("nombre_usuario") // <-- Nombre exacto en tu BDD
    val nombreUsuario: String,   // En Kotlin se acostumbra usar camello (camelCase)

    @SerialName("correo_electronico") // <-- Nombre exacto en tu BDD
    val correoElectronico: String,

    @SerialName("edad") // <-- Nombre exacto en tu BDD
    val edad: Int,

    @SerialName("contrasenia") // <-- Nombre exacto en tu BDD
    val contrasenia: String,

    @SerialName("carrera")
    val carrera: String,

    @SerialName("telefono")
    val telefono: String
)