package com.example.habitar.data

import kotlinx.serialization.Serializable

// --- MODELOS DE ENTRADA (REQUEST) ---
@Serializable
data class RespuestasCatq(
    val answers: Map<String, Int>
)

@Serializable
data class RespuestasRbq2a(
    val answers: Map<String, Int>
)

@Serializable
data class ScreeningRequest(
    val user_id: String,
    val catq: RespuestasCatq? = null,
    val rbq2a: RespuestasRbq2a? = null
)

// --- MODELOS DE SALIDA (RESPONSE) ---
@Serializable
data class CatqResult(
    val total_score: Int = 0,
    val compensation_score: Int = 0,
    val masking_score: Int = 0,
    val assimilation_score: Int = 0,
    val interpretation_note: String = ""
)

@Serializable
data class Rbq2aResult(
    val total_mean: Double = 0.0,
    val repetitive_motor_mean: Double = 0.0,
    val insistence_on_sameness_mean: Double = 0.0,
    val interpretation_note: String = ""
)

@Serializable
data class ScreeningResponse(
    val user_id: String = "",
    val catq_result: CatqResult? = null,
    val rbq2a_result: Rbq2aResult? = null,
    val disclaimer: String = ""
)

// --- MODELO DE PERSISTENCIA EN SUPABASE ---
@Serializable
data class ScreeningResultadoSupabase(
    val id_usuario: String,
    val catq_total: Int,
    val catq_compensacion: Int,
    val catq_enmascaramiento: Int,
    val catq_asimilacion: Int,
    val rbq2a_media: Double,
    val rbq2a_motor: Double,
    val rbq2a_rutinas: Double,
    val resumen_orientacion: String
)