package com.example.habitar.features.caracterizacionIA

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habitar.core.ui.componentes.AppButton

@Composable
fun CaracterizacionIAScreen(
    onTodoListoSuccess: () -> Unit, // Navega al menú principal al guardar con éxito
    viewModel: PreferenciasViewModel = viewModel(),
) {
    // Escuchar si Supabase guardó con exito para cambiar de pantalla
    LaunchedEffect(viewModel.guardadoExitoso) {
        if (viewModel.guardadoExitoso) {
            onTodoListoSuccess() // <-- Ejecuta la navegación al menú de forma segura
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEEEEF8))
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        // Título con estilo de cápsula
            Text(
                text = "Cuéntanos de ti",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth() // El fondo rosa ocupa todo el ancho disponible.
                    .background(
                        color = Color(0xFF6750A4),
                        shape = CircleShape
                    )
                    .padding(vertical = 12.dp) // Espaciado interno arriba y abajo del texto para darle aire.
            )


        Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "¿Qué te ayuda a autorregularte?",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Sonidos",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth() // <--- También se expande
                .padding(horizontal = 16.dp, vertical = 10.dp) // Espacio extra a los lados si quieres que sea un pelín más corta que la de arriba
                .background(
                    color = Color(0xFF9291F3), // El color lila claro que usamos en los bloques
                    shape = CircleShape
                )
                .padding(vertical = 12.dp)
        )

        OutlinedTextField(
            value = viewModel.preferenciasSonidos,
            onValueChange = { viewModel.preferenciasSonidos = it }, // Actualiza el estado 'email' con cada letra escrita
            label = { Text("Escribe cómo te ayudan los sonidos al momento de autorregularte...") },
            modifier = Modifier.fillMaxWidth(), // El campo ocupa todo el ancho disponible
            shape = RoundedCornerShape(12.dp),  // Bordes redondeados según el diseño de la app
            singleLine = false,  // Permite que usuario escriba párrafos
            maxLines = 4   // Limita el crecimiento visual antes de hacer scroll interno
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Recursos visuales",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth() // <--- También se expande
                .padding(horizontal = 16.dp, vertical = 10.dp) // Espacio extra a los lados si quieres que sea un pelín más corta que la de arriba
                .background(
                    color = Color(0xFF9291F3), // El color lila claro que usamos en los bloques
                    shape = CircleShape
                )
                .padding(vertical = 12.dp)
        )

        OutlinedTextField(
            value = viewModel.preferenciasVisuales,
            onValueChange = { viewModel.preferenciasVisuales = it }, // Actualiza el estado 'email' con cada letra escrita
            label = { Text("Escribe cómo y qué recursos visuales te ayudan al momento de autorregularte...") },
            modifier = Modifier.fillMaxWidth(), // El campo ocupa todo el ancho disponible
            shape = RoundedCornerShape(12.dp),  // Bordes redondeados según el diseño de la app
            singleLine = false,  // Permite que usuario escriba párrafos
            maxLines = 4   // Limita el crecimiento visual
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Debilidades",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth() // <--- También se expande
                .padding(horizontal = 16.dp, vertical = 10.dp) // Espacio extra a los lados si quieres que sea un pelín más corta que la de arriba
                .background(
                    color = Color(0xFF9291F3), // El color lila claro que usamos en los bloques
                    shape = CircleShape
                )
                .padding(vertical = 12.dp)
        )

        OutlinedTextField(
            value = viewModel.debilidadesIdentificadas,
            onValueChange = { viewModel.debilidadesIdentificadas = it }, // Actualiza el estado 'email' con cada letra escrita
            label = { Text("Cuéntanos cuáles consideras tus debilidades al autorregularte...") },
            modifier = Modifier.fillMaxWidth(), // El campo ocupa todo el ancho disponible
            shape = RoundedCornerShape(12.dp),  // Bordes redondeados según el diseño de la app
            singleLine = false,  // Permite que usuario escriba párrafos
            maxLines = 4   // Limita el crecimiento visual
        )

        Spacer(modifier = Modifier.height(24.dp))

        AppButton(
            text = if (viewModel.estaGuardando) "Guardando..." else "TODO LISTO",
            onClick = { viewModel.guardarFormulario() },// <-- CAMBIADO: Llama al ViewModel en lugar de navegar directo
            enabled = !viewModel.estaGuardando,      // <-- NUEVO: Bloquea el botón mientras consulta a Supabase
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF5E5CE6), // Color principal personalizado (Morado/Azul)
                contentColor = Color.White         // Color del texto blanco para legibilidad
            ),
            modifier = Modifier
                .fillMaxWidth(0.6f) // El botón usará el 60% del ancho de la pantalla
                .align(Alignment.CenterHorizontally) // Y se centrará en su contenedor Column
        )

        // --- FEEDBACK DE ERROR EN INTERFAZ ---
        if (viewModel.mensajeError != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = viewModel.mensajeError ?: "Error desconocido",
                color = Color.Red, // Rojo para alertar problemas
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
        }
    }
}

