package com.example.habitar.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object ScreeningApiService {
    private const val BASE_URL = "https://habitar-backend-odfe.onrender.com"

    val cliente = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun procesarScreening(request: ScreeningRequest): ScreeningResponse {
        return cliente.post("$BASE_URL/api/v1/screening/process") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}