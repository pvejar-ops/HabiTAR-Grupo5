package com.example.habitar.features.CaracterizaciónDeIA

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.habitar.core.ui.componentes.AppButton

@Composable
fun CaracterizacionIAScreen(
    // A esto se le llama "Callback". Es la única vía de salida de esta pantalla.
    onNavigateToMenu: () -> Unit,
) {
    // Column con scroll: Vital para que si agregas muchas preguntas, el usuario pueda bajar.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var sonidos by remember { mutableStateOf("") }
        var recursosvisuales by remember { mutableStateOf("") }
        var debilidades by remember { mutableStateOf("") }
        // Título con estilo de cápsula (mismo que usaste en el Menú de HabiTAR)
        Text(
            text = "Cuéntanos de ti",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center, // Centra el texto dentro de la cápsula
            modifier = Modifier
                .fillMaxWidth() // <--- Esto hace que se expanda hacia los lados
                .padding(vertical = 8.dp)
                .background(
                    color = Color(0xFF6750A4),
                    shape = CircleShape
                )
                .padding(vertical = 12.dp) // Padding arriba y abajo dentro de la morado
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "¿Qué te ayuda a autorregularte?",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))


        Text(
            text = "Sonidos",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth() // <--- También se expande
                .padding(horizontal = 16.dp, vertical = 8.dp) // Espacio extra a los lados si quieres que sea un pelín más corta que la de arriba
                .background(
                    color = Color(0xFF9291F3), // El color lila claro que usamos en los bloques
                    shape = CircleShape
                )
                .padding(vertical = 12.dp)
        )

        OutlinedTextField(
            value = sonidos,
            onValueChange = { sonidos = it }, // Actualiza el estado 'email' con cada letra escrita
            label = { Text("Escribe cómo te ayudan los sonidos al momento de autorregularte...") },
            modifier = Modifier.fillMaxWidth(), // El campo ocupa todo el ancho disponible
            shape = RoundedCornerShape(12.dp),  // Bordes redondeados según el diseño de la app
            singleLine = true                   // Evita que el usuario salte de línea dentro del campo
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Recursos visuales",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth() // <--- También se expande
                .padding(horizontal = 16.dp, vertical = 8.dp) // Espacio extra a los lados si quieres que sea un pelín más corta que la de arriba
                .background(
                    color = Color(0xFF9291F3), // El color lila claro que usamos en los bloques
                    shape = CircleShape
                )
                .padding(vertical = 12.dp)
        )

        OutlinedTextField(
            value = recursosvisuales,
            onValueChange = { recursosvisuales = it }, // Actualiza el estado 'email' con cada letra escrita
            label = { Text("Escribe cómo y qué recursos visuales te ayudan al momento de autorregularte...") },
            modifier = Modifier.fillMaxWidth(), // El campo ocupa todo el ancho disponible
            shape = RoundedCornerShape(12.dp),  // Bordes redondeados según el diseño de la app
            singleLine = true                   // Evita que el usuario salte de línea dentro del campo
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Debilidades",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth() // <--- También se expande
                .padding(horizontal = 16.dp, vertical = 8.dp) // Espacio extra a los lados si quieres que sea un pelín más corta que la de arriba
                .background(
                    color = Color(0xFF9291F3), // El color lila claro que usamos en los bloques
                    shape = CircleShape
                )
                .padding(vertical = 12.dp)
        )

        OutlinedTextField(
            value = debilidades,
            onValueChange = { debilidades = it }, // Actualiza el estado 'email' con cada letra escrita
            label = { Text("Cuéntanos cuáles consideras tus debilidades al autorregularte...") },
            modifier = Modifier.fillMaxWidth(), // El campo ocupa todo el ancho disponible
            shape = RoundedCornerShape(12.dp),  // Bordes redondeados según el diseño de la app
            singleLine = true                   // Evita que el usuario salte de línea dentro del campo
        )

        Spacer(modifier = Modifier.height(24.dp))

        AppButton(
            text = "TODO LISTO",
            onClick = onNavigateToMenu, // Llama a la función de navegación definida en el NavHost
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF5E5CE6), // Color principal personalizado (Morado/Azul)
                contentColor = Color.White         // Color del texto blanco para legibilidad
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}