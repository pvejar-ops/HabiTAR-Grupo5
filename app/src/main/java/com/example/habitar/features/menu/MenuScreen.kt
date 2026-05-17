package com.example.habitar.features.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.habitar.core.ui.componentes.AppButton

@Composable
fun MenuScreen(
    // Funciones Lambda: Permiten que el NavHost controle hacia dónde va el usuario al hacer clic.
    onNavigateToEmociones: () -> Unit,
    onNavigateToMeditacion: () -> Unit,
    onNavigateToEmergencia: () -> Unit,
    onNavigateToSensaciones: () -> Unit
) {
    // Column: Organiza los elementos de forma vertical uno debajo del otro.
    Column(
        modifier = Modifier
            .fillMaxSize() // Indica que la columna debe ocupar todo el alto y ancho de la pantalla.
            .verticalScroll(rememberScrollState()) // Habilita el desplazamiento si el contenido es más alto que la pantalla.
            .padding(24.dp), // Agrega un margen de seguridad de 24dp en todos los bordes.
        horizontalAlignment = Alignment.CenterHorizontally // Centra todos los componentes hijos horizontalmente.
    ) {
        // --- TÍTULO TIPO CÁPSULA ---
        Text(
            text = "Módulos de ayuda",
            style = MaterialTheme.typography.titleMedium, // Aplica el estilo de texto predefinido de Material3.
            color = Color.White, // Define el color del texto como blanco.
            fontWeight = FontWeight.Bold, // Aplica negrita para resaltar el título.
            modifier = Modifier
                .padding(vertical = 8.dp) // Margen externo para separar el título de otros elementos.
                .background(
                    color = Color(0xFF6750A4), // Fondo de color morado sólido (Hexadecimal).
                    shape = CircleShape // Corta el fondo en forma circular/cápsula.
                )
                .padding(horizontal = 24.dp, vertical = 12.dp) // Padding interno: espacio entre el texto y el borde morado.
        )

        // Subtítulo explicativo debajo del título principal.
        Text(
            text = "Selecciona un módulo de autoregulación",
            style = MaterialTheme.typography.bodyLarge, // Estilo de texto un poco más pequeño que el título.
            fontWeight = FontWeight.Medium, // Grosor de fuente intermedio.
            textAlign = TextAlign.Center, // Alinea el contenido del texto al centro.
            modifier = Modifier
                .fillMaxWidth() // Expande el contenedor para que el centrado sea efectivo.
                .padding(top = 16.dp, bottom = 24.dp) // Espaciado específico para separar del título y de los botones.
        )

        // --- BLOQUES DE NAVEGACIÓN (Basados en image_71c9c7.png) ---

        // AppButton (Emociones): Bloque de color rosado vibrante.
        AppButton(
            text = "Emociones",
            onClick = onNavigateToEmociones, // Ejecuta la navegación hacia la pantalla de emociones.
            modifier = Modifier
                .fillMaxWidth() // Hace que el botón use todo el ancho disponible.
                .height(90.dp), // Altura fija de 90dp para darle aspecto de bloque/tarjeta.
            shape = RoundedCornerShape(16.dp), // Bordes suavizados con un radio de 16dp.
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD671F5) // Color de fondo específico para este bloque.
            )
        )

        // Spacer: Crea un espacio vacío de 16dp entre los botones para que no se vean pegados.
        Spacer(modifier = Modifier.height(16.dp))

        // AppButton (Sensaciones): Bloque de color lila suave.
        AppButton(
            text = "Sensaciones Físicas",
            onClick = onNavigateToSensaciones,
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF9291F3)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // AppButton (Meditación): Bloque de color verde menta/turquesa.
        AppButton(
            text = "Meditación",
            onClick = onNavigateToMeditacion,
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF5ED9B9)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // AppButton (Emergencia): Bloque de color naranja pastel.
        AppButton(
            text = "Emergencia",
            onClick = onNavigateToEmergencia,
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFA085)
            )
        )
    }
}