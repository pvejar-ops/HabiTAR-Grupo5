package com.example.habitar.features.autorregulacion.sensaciones

import androidx.activity.compose.LocalActivity
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
import androidx.compose.ui.viewinterop.AndroidView

/** Pantalla de Sensaciones Físicas para el proyecto HabiTAR.
 * Permite al usuario identificar síntomas corporales para recibir pautas de autorregulación.*/

@Composable
fun SensacionesScreen(
    onNavigateToMenu: () -> Unit,
    viewModel: SensacionesViewModel = viewModel()
) {
    // Listado de sensaciones para poblar la interfaz.
    val sensacionesDisponibles = listOf(
        "Taquicardia", "Sudoración", "Picazón",
        "Tensión Muscular", "Náuseas", "Dolor Físico"
    )

    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current
    val activity = LocalActivity.current as androidx.activity.ComponentActivity

    DisposableEffect(Unit) {
        val originalMode = activity.window.attributes.softInputMode
        activity.window.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        onDispose {
            activity.window.setSoftInputMode(originalMode) // 👈 restaura al salir
        }
    }

    // Contenedor principal con scroll vertical para evitar cortes en pantallas pequeñas.
    Column(
        modifier = Modifier
            .fillMaxWidth() // Expande el contenedor a todo el espacio disponible.
            .statusBarsPadding()
            .verticalScroll(rememberScrollState()) // Activa el desplazamiento si el contenido crece.
            .padding(24.dp)
           .imePadding(), // Margen de seguridad de 24dp en todos los bordes.
        horizontalAlignment = Alignment.CenterHorizontally // Centra los componentes hijos horizontalmente.
    ) {

        // --- FUNCIÓN: BOTÓN VOLVER ---
        IconButton(
            onClick = onNavigateToMenu,
            modifier = Modifier
                .background(
                    color = Color(0xFFF3EDF7),
                    shape = CircleShape
                ) // Fondo circular lila suave.
                .size(48.dp) // Tamaño fijo del área de click.
                .align(Alignment.Start) // Posiciona el botón al inicio (izquierda) de la columna.
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver",
                tint = Color(0xFF928AF0) // Color lila base del módulo Sensaciones.
            )
        }

        Spacer(modifier = Modifier.height(16.dp)) // Espaciado de 16dp entre el botón y el título.

        // --- FUNCIÓN: TÍTULO PRINCIPAL ---
        Text(
            text = "Sensaciones físicas",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth() // Asegura que el fondo lila cruce toda la pantalla.
                .background(
                    color = Color(0xFF9291F3),
                    shape = CircleShape
                ) // Cápsula de color sólido.
                .padding(vertical = 12.dp) // Espaciado interno para dar altura a la barra de título.
        )

        // Subtítulo explicativo del propósito de la pantalla.
        Text(
            text = "Chat con IA que te permite autorregularte",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 16.dp), // Separación vertical de 16dp.
            textAlign = TextAlign.Center
        )

        // Instrucción directa con margen inferior para separar de los chips.
        Text(
            text = "Selecciona las sensaciones que te identifiquen en este momento",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // --- FUNCIÓN: CUADRÍCULA DE SELECCIÓN (GRID) ---
        Column(modifier = Modifier.fillMaxWidth()) {
            // Se agrupan los elementos de 3 en 3 para formar las filas de la cuadrícula.
            val chunks = sensacionesDisponibles.chunked(3)
            chunks.forEach { fila ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp), // Espaciado entre filas.
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Espaciado horizontal de 8dp entre chips.
                ) {
                    fila.forEach { etiqueta ->
                        val estaSeleccionada = viewModel.sensacionesSeleccionadas.contains(etiqueta)
                        SensacionChip(
                            text = etiqueta,
                            isSelected = estaSeleccionada,
                            modifier = Modifier.weight(1f), // Distribuye el ancho equitativamente (33% cada uno).
                            onCheckedChange = {
                                viewModel.alternarSensacion(etiqueta) //función que refresca el estado en el ViewModel
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp)) // Espacio de 24dp antes de la entrada de texto.

        // --- FUNCIÓN: BARRA DE TÍTULO DE ESCRITURA ---
        Text(
            text = "Escribe lo que sientes...",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFFE0E0E0), shape = CircleShape) // Fondo gris neutro.
                .padding(vertical = 8.dp), // Padding interno para centrar el texto verticalmente.
            textAlign = TextAlign.Center
        )

        // --- FUNCIÓN: CAMPO DE TEXTO (USER INPUT) ---
        OutlinedTextField(
            value = viewModel.comentarioUsuario,
            onValueChange = { viewModel.comentarioUsuario = it },
            placeholder = { Text("Escribe aquí...", textAlign = TextAlign.Center) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp), // Margen vertical para no tocar el título ni el botón.
            shape = RoundedCornerShape(16.dp), // Bordes redondeados de 16dp.
            singleLine = false, // Permite múltiples líneas por si describe varios síntomas con calma
            maxLines = 4,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE0E0E0), // Gris cuando no hay foco.
                focusedBorderColor = Color(0xFF9291F3)    // Lila cuando el usuario está escribiendo.
            )
        )

        // --- FUNCIÓN: BOTÓN DE ACCIÓN PARA IA ---
        Button(
            onClick = { viewModel.enviarFormularioSensaciones()
                        focusManager.clearFocus()},
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = CircleShape, // Botón completamente redondeado.
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF928AF0), // Lila base.
                contentColor = Color.White        // Texto blanco.
            ),
            // Se inhabilita mientras procesa o si todo está vacío
            enabled = !viewModel.estaProcesando &&
                    (viewModel.comentarioUsuario.isNotBlank() || viewModel.sensacionesSeleccionadas.isNotEmpty())
        ) {
                if (viewModel.estaProcesando) {  // 👈 agrega esto
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                } else {
                    Text("Pedir consejo a HabiTAR", fontWeight = FontWeight.Bold)
                }
        }

        // Línea decorativa para separar visualmente la entrada de la respuesta.
        Box(
            modifier = Modifier
                .width(200.dp)
                .height(4.dp)
                .background(color = Color(0xFF9291F3), shape = CircleShape)
        )

        // Muestra errores en rojo en la UI si la red o Supabase fallan
        if (viewModel.mensajeError != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = viewModel.mensajeError ?: "",
                color = Color.Red,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- FUNCIÓN: ÁREA DE RESPUESTA ---
        Text(
            text = "Respuesta IA",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFFF3EDF7), shape = CircleShape) // Fondo lila muy claro.
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (viewModel.respuestaIA_Visualizar.isBlank()) {
                    Text(
                        text = "Aquí aparecerá el consejo de la IA basado en tus sensaciones.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    AndroidView(
                        factory = { context ->
                            android.widget.TextView(context).also { tv ->
                                io.noties.markwon.Markwon.create(context)
                                    .setMarkdown(tv, viewModel.respuestaIA_Visualizar)
                            }
                        },
                        update = { tv ->
                            io.noties.markwon.Markwon.create(tv.context)
                                .setMarkdown(tv, viewModel.respuestaIA_Visualizar)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}


/**
 * Componente personalizado para los Chips de Sensación.
 * @param text Etiqueta a mostrar.
 * @param isSelected Define el estado visual (invertido).
 */
@Composable
fun SensacionChip(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: () -> Unit
) {
    Surface(
        shape = CircleShape,
        color = if (isSelected) Color(0xFFF3EDF7) else Color(0xFF928AF0),
        modifier = modifier.clickable { onCheckedChange() } // Captura el evento de tacto.
    ) {
        Text(
            text = text,
            // Ajuste de contraste dinámico para el texto.
            color = if (isSelected) Color(0xFF6750A4) else Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp), // Espaciado interno del chip.
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}
