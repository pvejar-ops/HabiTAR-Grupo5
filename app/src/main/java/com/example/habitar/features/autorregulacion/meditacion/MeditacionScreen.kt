package com.example.habitar.features.autorregulacion.meditacion

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.border

@Composable
fun MeditacionScreen(onNavigateToMenu: () -> Unit) {
    // ESTADO para la selección del momento del día
    var momentoSeleccionado by remember { mutableStateOf("") }

    // --- COLORES DE LA PALETA  ---
    val mentaPrincipal = Color(0xFF63D3B5)
    val grisFondo = Color(0xFFE0E0E0)
    val moradoIcono = Color(0xFF6750A4)

    val targetColor = when (momentoSeleccionado) {
        "Mañana" -> Color(0xFFFFF9C4) // Un amarillo muy claro
        "Tarde" -> Color(0xFFFFE0B2)  // Un naranja suave
        "Noche" -> Color(0xFFC5CAE9)  // Un azul oscuro/lavanda
        else -> Color.White           // Blanco si no hay nada seleccionado

    }

    val animatedBgColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 500) // 0.5 segundos de transición
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(animatedBgColor) // Usamos el color animado
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- BOTÓN VOLVER ---
        IconButton(
            onClick = onNavigateToMenu,
            modifier = Modifier
                .background(color = Color(0xFFF3EDF7), shape = CircleShape) // Fondo lila claro circular.
                .size(48.dp) // Define un tamaño fijo de 48dp para el área del botón.
                .align(Alignment.Start) // Fuerza a este botón específico a alinearse a la izquierda.
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver",
                tint = Color(0xFF63D3B5) // Color rosa distintivo del módulo.
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- TÍTULO PRINCIPAL ---
        Text(
            text = "Meditación",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(mentaPrincipal, CircleShape)
                .padding(vertical = 12.dp),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Recursos auditivos y visuales que te permiten relajarte y meditar",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
        )

        // --- SECCIÓN: MOMENTO DEL DÍA ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val opciones = listOf("Mañana", "Tarde", "Noche")
            opciones.forEach { opcion ->
                // Si 'momentoSeleccionado' es "", esto será falso para todos al inicio
                val esEste = momentoSeleccionado == opcion

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(CircleShape) // Importante para que el ripple del clic sea circular
                        .background(if (esEste) mentaPrincipal else Color(0xFFF0F0F0), CircleShape)
                        .clickable { momentoSeleccionado = opcion }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = opcion,
                        // Si no está seleccionado, el texto es gris oscuro. Si sí, es blanco.
                        color = if (esEste) Color.White else Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- SECCIÓN: RECURSOS AUDITIVOS ---
        RecursoHeader(titulo = "Recursos auditivos", color = mentaPrincipal)

        // Aquí conectas Supabase. Por ahora, pasamos datos manuales.
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // Ejemplo de 3 recursos
            ItemRecurso("Podcast", "res_podcast") // Reemplaza por tu ID de recurso
            ItemRecurso("Olas", "res_olas")
            ItemRecurso("Sonido blanco", "res_blanco")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- SECCIÓN: RECURSOS VISUALES ---
        RecursoHeader(titulo = "Recursos visuales", color = mentaPrincipal)

        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ItemRecurso("Respiración", "res_respiracion")
            ItemRecurso("Ambiente", "res_ambiente")
            ItemRecurso("Visualización", "res_visualizacion")
        }
    }
}

/**
 * Encabezado de sección tipo cápsula (Recursos auditivos/visuales)
 */
@Composable
fun RecursoHeader(titulo: String, color: Color) {
    Text(
        text = titulo,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .background(color.copy(alpha = 1f), CircleShape)
            .padding(vertical = 8.dp),
        textAlign = TextAlign.Center
    )
}

/**
 * Elemento individual (Círculo con imagen y texto inferior)
 * AQUÍ es donde recibirás la URL de la imagen desde Supabase.
 */
@Composable
fun ItemRecurso(nombre: String, idRecurso: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(100.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(20.dp)) // Bordes muy redondeados como la imagen
                .background(Color.LightGray)
                .clickable { /* Lógica para abrir desde Supabase usando el idRecurso */ }
        ) {
            // Cuando tengas Supabase, usa Coil o Glide para cargar la URL aquí
            Text(
                text = "IMG",
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        }
        Text(
            text = nombre,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp),
            lineHeight = 14.sp
        )
    }
}