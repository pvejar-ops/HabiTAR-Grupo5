package com.example.habitar.features.autorregulacion.emociones

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

/** Pantalla de Emociones del proyecto HabiTAR.
 Gestiona la selección de estados anímicos y el envío de datos a la IA */
@Composable
fun EmocionesScreen(
    onNavigateToMenu: () -> Unit,
    viewModel: EmocionesViewModel = viewModel()
) {
    val etiquetasDisponibles = listOf("Feliz", "Eufórico", "Confundido", "Triste", "Enojado", "Frustrado")

    // CONTENEDOR PRINCIPAL: Ahora maneja el padding de forma limpia
    Column(
        modifier = Modifier
            .fillMaxSize() // 1. Ocupa toda la pantalla
            .verticalScroll(rememberScrollState()) // 2. El scroll va primero para manejar el contenido dinámico
            .padding(all = 24.dp) // 3. El margen de la pantalla
            .imePadding(), // 4. 💡 EL IME VA AL FINAL. Así empuja el contenedor real desde la base del teclado
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --- COMPONENTE: BOTÓN VOLVER ---
        IconButton(
            onClick = onNavigateToMenu,
            modifier = Modifier
                .background(color = Color(0xFFF3EDF7), shape = CircleShape)
                .size(48.dp)
                .align(Alignment.Start)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver",
                tint = Color(0xFFD671F5)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- COMPONENTE: TÍTULO PRINCIPAL ---
        Text(
            text = "Emociones",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFFD671F5), shape = CircleShape)
                .padding(vertical = 12.dp)
        )

        // Subtítulo descriptivo
        Text(
            text = "Chat con IA que te permite autorregularte",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )

        // Instrucción
        Text(
            text = "Selecciona las emociones que te identifiquen en este momento",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // --- COMPONENTE: GRID DE EMOCIONES (Optimizado sin la columna extra) ---
        // Quitamos el Modifier.fillMaxWidth() conflictivo de este bloque
        val chunks = etiquetasDisponibles.chunked(3)
        chunks.forEach { fila ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                fila.forEach { etiqueta ->
                    val estaSeleccionada = viewModel.emocionesSeleccionadas.contains(etiqueta)
                    EmotionChip(
                        text = etiqueta,
                        isSelected = estaSeleccionada,
                        modifier = Modifier.weight(1f),
                        onCheckedChange = {
                            viewModel.alternarEmocion(etiqueta)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- COMPONENTE: CAMPO DE TEXTO (USER INPUT) ---
        Text(
            text = "Escribe lo que sientes...",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFFE0E0E0), shape = CircleShape)
                .padding(vertical = 8.dp), // Padding interno para centrar el texto en la barra gris.
            textAlign = TextAlign.Center
        )

        OutlinedTextField(
            value = viewModel.comentarioUsuario,
            onValueChange = { viewModel.comentarioUsuario = it },
            placeholder = { Text("Escribe aquí...", textAlign = TextAlign.Center) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp), // Margen externo para no pegarse a los elementos de arriba/abajo.
            shape = RoundedCornerShape(16.dp), // Esquinas redondeadas para el cuadro de texto.
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE0E0E0), // Color del borde cuando no se está escribiendo.
                focusedBorderColor = Color(0xFFD671F5)    // Color del borde al hacer click para escribir.
            )
        )

        // --- COMPONENTE: BOTÓN ACCIÓN IA ---
        Button(
            onClick = { viewModel.enviarFormularioEmociones() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD671F5), // Fondo rosa sólido.
                contentColor = Color.White        // Texto del botón en blanco.
            ),
            // Se bloquea si está cargando o si no ha marcado nada
            enabled = !viewModel.estaProcesando &&
                    (viewModel.comentarioUsuario.isNotBlank() || viewModel.emocionesSeleccionadas.isNotEmpty())
        ) {
            Text(
                text = if (viewModel.estaProcesando) "Procesando..." else "Pedir consejo a HabiTAR",
                fontWeight = FontWeight.Bold
            )
        }

        // Línea estética decorativa de 4dp de grosor.
        Box(
            modifier = Modifier
                .width(200.dp)
                .height(4.dp)
                .background(color = Color(0xFFD671F5), shape = CircleShape)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- SECCIÓN: RESPUESTA IA ---
        Text(
            text = "Respuesta IA",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFFF3EDF7), shape = CircleShape)
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)), // Fondo gris muy claro.
            elevation = CardDefaults.cardElevation(2.dp) // Sombra suave para dar profundidad.
        ) {
            Column(modifier = Modifier.padding(16.dp)) { // Padding interno de la tarjeta para el texto.
                Text(
                    text = viewModel.respuestaIA_Visualizar.ifBlank {
                        "Aquí aparecerá el consejo de la IA basado en tus emociones."
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

/**
 * Función que dibuja cada cápsula de emoción.
 */
@Composable
fun EmotionChip(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: () -> Unit
) {
    Surface(
        shape = CircleShape,
        color = if (isSelected) Color(0xFFF3EDF7) else Color(0xFFD671F5), //0xFFF3EDF7
        modifier = modifier.clickable { onCheckedChange() } // Detecta el toque en toda la superficie de la cápsula.
    ) {
        Text(
            text = text,
            // Ajuste de contraste: blanco en fondo oscuro, morado oscuro en fondo claro.
            color = if (isSelected) Color(0xFFD671F5) else Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp), // Espaciado para que el texto no toque los bordes del chip.
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}