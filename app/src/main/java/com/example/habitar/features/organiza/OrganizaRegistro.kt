package com.example.habitar.features.organiza

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class OrganizaRegistro(
    @SerialName("id_tarea")
    val idTarea: Long? = null,          // int8 (autogenerado por la BDD)

    @SerialName("descripcion_tarea")
    val descripcionTarea: String,       // text

    @SerialName("completada")
    val completada: Boolean? = false,    // bool

    @SerialName("fecha_tarea")
    val fechaTarea: String,             // date (YYYY-MM-DD)

    @SerialName("id_usuario")
    val idUsuario: String? = null       // uuid
)