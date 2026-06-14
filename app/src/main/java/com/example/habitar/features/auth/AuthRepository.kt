package com.example.habitar.features.auth

import com.example.habitar.core.SupabaseConfig
import com.example.habitar.data.PerfilUsuario
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import android.util.Log

class AuthRepository {
    private val client = SupabaseConfig.client

    // 1. REGISTRO + CREACIÓN DE PERFIL INICIAL
    suspend fun registrarEstudiante(email: String, contrasenia: String, nombre: String, edad: Int, carrera: String, telefono: String): Boolean = withContext(Dispatchers.IO) {
        try {
            // Registrar credenciales en Supabase Auth
            val authResult = client.auth.signUpWith(Email) {
                this.email = email
                this.password = contrasenia
             }

            // Obtenemos el UUID generado de forma segura
            //val userId = authResult?.id
            val userId = client.auth.currentUserOrNull()?.id // Obtenemos el UUID generado de forma segura directamente del estado de la sesión

            if (userId != null) {
                // Creamos el objeto perfil con tus variables exactas corregidas
                val nuevoPerfil = PerfilUsuario(
                    idUsuario = userId,
                    nombreUsuario = nombre,
                    correoElectronico = email,
                    edad = edad,
                    contrasenia = contrasenia,
                    carrera = carrera, // Añadir propiedad
                    telefono = telefono // Añadir propiedad
                )

                // Insertar en tu tabla real 'perfil_usuario'
                client.postgrest.from("perfil_usuario").insert(nuevoPerfil)

                Log.d("DEBUG_HABITAR_AUTH", "Perfil guardado con éxito en la tabla para el UID: $userId")
                true
            } else {
                Log.e("DEBUG_HABITAR_AUTH", "No se pudo obtener el UID del usuario registrado.")
                false
            }
        } catch (e: Exception) {
            Log.e("DEBUG_HABITAR_AUTH", "Error en registro: ${e.message}")
            e.printStackTrace()
            false
        }
    }

    // 2. INICIO DE SESIÓN
    suspend fun iniciarSesion(email: String, contrasenia: String): Boolean = withContext(Dispatchers.IO) {
        // Definimos una etiqueta para filtrar en el Logcat
        val TAG = "DEBUG_HABITAR_AUTH"

        Log.d(TAG, "==> INICIANDO INTENTO DE LOGIN <==")
        Log.d(TAG, "Intentando conectar con Email: $email")

        try {
            Log.d(TAG, "Llamando a client.auth.signInWith...")
            client.auth.signInWith(Email) {
                this.email = email
                this.password = contrasenia
            }

            Log.d(TAG, "✅ ¡LOGIN EXITOSO EN SUPABASE!")
            true
        } catch (e: Exception) {
            Log.e(TAG, "❌ ¡LA APLICACIÓN CAPTURÓ UN ERROR!")
            Log.e(TAG, "Mensaje del error: ${e.message}")
            Log.e(TAG, "Causa del error: ${e.cause}")

            // Esto imprime toda la ruta del choque en el Logcat sin cerrar la app
            e.printStackTrace()
            false
        }
    }
}