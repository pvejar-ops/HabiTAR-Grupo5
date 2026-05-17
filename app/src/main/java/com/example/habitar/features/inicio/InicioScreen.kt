package com.example.habitar.features.inicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.habitar.R

@Composable
fun InicioScreen(
    onNavigateToNext: () -> Unit, // Función para movernos a la siguiente pantalla
    viewModel: InicioViewModel = viewModel() // El "cerebro" que maneja los datos de esta pantalla
) {
    // El Box funciona como un "marco" donde ponemos cosas una encima de otra (como capas)
    Box(
        modifier = Modifier.fillMaxSize() // Hace que el marco ocupe toda la pantalla del celular
    ) {
        // CAPA 1: La imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.fotoinicohabitar), // Busca tu foto en la carpeta drawable
            contentDescription = null, // No necesita descripción porque es decorativa
            modifier = Modifier.fillMaxSize(), // Estira la imagen a toda la pantalla
            contentScale = ContentScale.Crop // Corta los bordes para que la imagen no se vea deformada
        )

        // CAPA 2: Contenido visual (Textos y Botón)
        Column(
            modifier = Modifier
                .fillMaxSize() // La columna también ocupa todo el espacio para organizar lo de adentro
                .padding(24.dp), // Deja un margen en los bordes para que nada toque las orillas
            verticalArrangement = Arrangement.SpaceBetween, // Separa los textos (arriba) del botón (abajo)
            horizontalAlignment = Alignment.CenterHorizontally // Centra todo el contenido de izquierda a derecha
        ) {

            // SECCIÓN DE TÍTULOS
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 40.dp) // Baja un poco los textos del borde superior
            ) {
                Text(
                    text = "Bienvenid@",
                    style = MaterialTheme.typography.headlineSmall, // Tamaño de letra mediano
                    color = Color(0xFF5E5CE6) // Color azul/morado del diseño de Figma
                )

                Spacer(modifier = Modifier.height(8.dp)) // Un espacio pequeño entre palabras

                Text(
                    text = "HabiTAR",
                    style = MaterialTheme.typography.displayMedium, // Tamaño de letra grande y llamativo
                    fontWeight = FontWeight.Bold, // Pone la letra en negrita
                    color = Color(0xFF5E5CE6)
                )
            }

            // SECCIÓN DEL BOTÓN
            Button(
                onClick = onNavigateToNext, // Ejecuta la acción de navegar cuando lo presionas
                modifier = Modifier
                    .fillMaxWidth(0.8f) // El botón ocupa el 80% del ancho de la pantalla
                    .height(56.dp) // Altura del botón para que sea fácil de tocar
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(28.dp), // Hace que las puntas sean redondas (estilo píldora)
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5E5CE6) // Color del fondo del botón
                )
            ) {
                Text(
                    text = "COMENZAR",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White // Letras blancas para que resalten
                )
            }
        }
    }
}