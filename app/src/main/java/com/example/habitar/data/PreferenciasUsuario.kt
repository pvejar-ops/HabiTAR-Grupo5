package com.example.habitar.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PreferenciasUsuario(
    @SerialName("id_usuario")
    val idUsuario: String, // Aquí se guarda el UUID del usuario asociado

    @SerialName("preferencias_sonidos")
    val preferenciasSonidos: String,

    @SerialName("preferencias_visuales")
    val preferenciasVisuales: String,

    @SerialName("debilidades_identificadas")
    val debilidadesIdentificadas: String
)