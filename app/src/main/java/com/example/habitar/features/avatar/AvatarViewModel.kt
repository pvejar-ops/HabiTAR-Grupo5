package com.example.habitar.features.avatar

//Código para generar avatar con ia, viewmodel
/*import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitar.BuildConfig
import com.example.habitar.core.SupabaseConfig
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.util.UUID

@Serializable
data class OpcionesAvatar(
    val tipo: String,
    val genero: String? = null,
    val colorPiel: String? = null,
    val colorCabello: String? = null,
    val colorOjos: String,
    val ropa: String? = null,
    val colorRopa: String? = null,
    val tipoAnimal: String? = null,
    val colorPelo: String? = null,
    val accesorio: String? = null
)

@Serializable
data class AvatarUsuario(
    val id_usuario: String,
    val tipo: String,
    val opciones: String,
    val imagen_url: String
)

class AvatarViewModel : ViewModel() {

    var tipoAvatar by mutableStateOf("Persona")

    // Opciones persona
    var genero by mutableStateOf("Femenino")
    var colorPiel by mutableStateOf("Claro")
    var colorCabello by mutableStateOf("Negro")
    var colorOjosPersona by mutableStateOf("Café")
    var ropa by mutableStateOf("Casual")
    var colorRopa by mutableStateOf("Azul")

    // Opciones animal
    var tipoAnimal by mutableStateOf("Gato")
    var colorPelo by mutableStateOf("Negro")
    var colorOjosAnimal by mutableStateOf("Verde")
    var accesorio by mutableStateOf("Sin accesorio")

    // Estados UI
    var estaCargando by mutableStateOf(false)
    var mensajeError by mutableStateOf<String?>(null)
    var avatarGenerado by mutableStateOf<Bitmap?>(null)
    var guardadoExitoso by mutableStateOf(false)

    private val json = Json { ignoreUnknownKeys = true }

    fun generarAvatar() {
        viewModelScope.launch {
            estaCargando = true
            mensajeError = null
            avatarGenerado = null
            try {
                val prompt = construirPrompt()
                val client = HttpClient(Android)

                val response = client.post(
                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.1-flash-image-preview:generateContent?key=${BuildConfig.GEMINI_API_KEY}"
                ) {
                    contentType(ContentType.Application.Json)
                    setBody("""
        {
          "contents": [{"parts": [{"text": "${prompt.replace("\"", "\\\"").replace("\n", "\\n")}"}]}],
          "generationConfig": {"responseModalities": ["IMAGE", "TEXT"]}
        }
    """.trimIndent())
                }

                val responseText = response.bodyAsText()
                Log.d("AVATAR_VM", "Respuesta: $responseText")

                val jsonResponse = json.parseToJsonElement(responseText)
                val parts = jsonResponse
                    .jsonObject["candidates"]!!
                    .jsonArray[0]
                    .jsonObject["content"]!!
                    .jsonObject["parts"]!!
                    .jsonArray

                val base64 = parts.firstOrNull {
                    it.jsonObject.containsKey("inlineData")
                }?.jsonObject?.get("inlineData")
                    ?.jsonObject?.get("data")
                    ?.jsonPrimitive?.content

                if (base64 != null) {
                    val bytes = Base64.decode(base64, Base64.DEFAULT)
                    avatarGenerado = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                } else {
                    mensajeError = "No se pudo generar la imagen."
                }

                client.close()

            } catch (e: Exception) {
                mensajeError = "Error al generar avatar: ${e.localizedMessage}"
                Log.e("AVATAR_VM", "Error: ${e.localizedMessage}", e)
            } finally {
                estaCargando = false
            }
        }
    }

    fun guardarAvatar() {
        val bitmap = avatarGenerado ?: return
        viewModelScope.launch {
            estaCargando = true
            mensajeError = null
            try {
                val userId = SupabaseConfig.client.auth.currentUserOrNull()?.id ?: return@launch

                val stream = java.io.ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val bytes = stream.toByteArray()

                val fileName = "avatar_${userId}_${UUID.randomUUID()}.png"
                SupabaseConfig.client.storage.from("avatares").upload(fileName, bytes)
                val imageUrl = SupabaseConfig.client.storage.from("avatares").publicUrl(fileName)

                val opciones = OpcionesAvatar(
                    tipo = tipoAvatar,
                    genero = if (tipoAvatar == "Persona") genero else null,
                    colorPiel = if (tipoAvatar == "Persona") colorPiel else null,
                    colorCabello = if (tipoAvatar == "Persona") colorCabello else null,
                    colorOjos = if (tipoAvatar == "Persona") colorOjosPersona else colorOjosAnimal,
                    ropa = if (tipoAvatar == "Persona") ropa else null,
                    colorRopa = if (tipoAvatar == "Persona") colorRopa else null,
                    tipoAnimal = if (tipoAvatar == "Animal") tipoAnimal else null,
                    colorPelo = if (tipoAvatar == "Animal") colorPelo else null,
                    accesorio = if (tipoAvatar == "Animal") accesorio else null
                )

                SupabaseConfig.client.from("avatar_usuario").upsert(
                    AvatarUsuario(
                        id_usuario = userId,
                        tipo = tipoAvatar,
                        opciones = Json.encodeToString(OpcionesAvatar.serializer(), opciones),
                        imagen_url = imageUrl
                    )
                )
                guardadoExitoso = true

            } catch (e: Exception) {
                mensajeError = "Error al guardar: ${e.localizedMessage}"
                Log.e("AVATAR_VM", "Error guardando: ${e.localizedMessage}", e)
            } finally {
                estaCargando = false
            }
        }
    }

    private fun construirPrompt(): String {
        return if (tipoAvatar == "Persona") {
            """
            Crea un avatar de persona animado, estilo caricatura amigable, apropiado para una app universitaria institucional.
            Características:
            - Género: $genero
            - Color de piel: $colorPiel
            - Color de cabello: $colorCabello
            - Color de ojos: $colorOjosPersona
            - Ropa estilo: $ropa
            - Color de ropa: $colorRopa
            El avatar debe ser de busto, fondo blanco o neutro, expresión amigable y neutral.
            No incluyas texto, marcas de agua ni contenido inapropiado.
            """.trimIndent()
        } else {
            """
            Crea un avatar de animal animado, estilo caricatura tierna, apropiado para una app universitaria institucional.
            Características:
            - Tipo de animal: $tipoAnimal
            - Color de pelo: $colorPelo
            - Color de ojos: $colorOjosAnimal
            - Accesorio: $accesorio
            El avatar debe verse de frente, fondo blanco o neutro, expresión amigable.
            No incluyas texto, marcas de agua ni contenido inapropiado.
            """.trimIndent()
        }
    }
}*/