package com.example.habitar

import com.example.habitar.data.RespuestasCatq
import com.example.habitar.data.ScreeningApiService
import com.example.habitar.data.ScreeningRequest
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*

class ScreeningBackendTest {

    @Test
    fun probar_conexion_con_fastapi() = runBlocking {
        println("--> ENVIANDO EVALUACIÓN REAL A RENDER...")

        // Creamos un CAT-Q de prueba con 25 respuestas
        val respuestasPrueba = (1..25).associate { it.toString() to 4 }

        val request = ScreeningRequest(
            user_id = "USR-APP-001",
            catq = RespuestasCatq(answers = respuestasPrueba)
        )

        try {
            val respuesta = ScreeningApiService.procesarScreening(request)

            println("--> RESPUESTA EXITOSA DE RENDER!")
            println("Usuario: ${respuesta.user_id}")
            println("CAT-Q Total: ${respuesta.catq_result?.total_score}")
            println("Compensación: ${respuesta.catq_result?.compensation_score}")
            println("Disclaimer: ${respuesta.disclaimer}")

            assertNotNull(respuesta.catq_result)
            assertEquals(100, respuesta.catq_result?.total_score)
        } catch (e: Exception) {
            println("ERROR AL CONECTAR: ${e.message}")
            e.printStackTrace()
            fail("Falló la conexión: ${e.message}")
        }
    }
}