package com.example.habitar.features.autorregulacion.emergencia

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.ui.unit.sp

@Composable
fun EmergenciaScreen(onNavigateToMenu: () -> Unit) {
    // --- ESTADOS ---
    var ubicacionSeleccionada by remember { mutableStateOf("") }
    var textoEmergencia by remember { mutableStateOf("") }

    // --- PALETA DE COLORES (image_694295.png) ---
    val coralPrincipal = Color(0xFFF4B3A3) // El color salmón de los botones
    val coralSuave = Color(0xFFFDE8E3)    // Para el estado seleccionado (invertido)
    val grisEncabezado = Color(0xFFE0E0E0)
    val fondoTarjeta = Color(0xFFF8F8F8)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- BOTÓN VOLVER ---
        IconButton(
            onClick = onNavigateToMenu,
            modifier = Modifier
                .background(color = Color(0xFFF3EDF7), shape = CircleShape)
                .size(40.dp)
                .align(Alignment.Start)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color(0xFFF4B3A3))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- TÍTULO PRINCIPAL ---
        Text(
            text = "Sensaciones físicas",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(coralPrincipal, CircleShape)
                .padding(vertical = 12.dp),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Autorregúlate en una situación compleja",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )

        // --- SECCIÓN: ¿DÓNDE ESTÁS? ---
        Text(
            text = "¿Dónde estás?",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val opciones = listOf("Universidad", "Vía pública", "Casa")
            opciones.forEach { opcion ->
                val esEste = ubicacionSeleccionada == opcion
                // Lógica de color invertida: Fuerte por defecto, suave al seleccionar
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(if (esEste) coralSuave else coralPrincipal, CircleShape)
                        .clickable { ubicacionSeleccionada = opcion }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = opcion,
                        color = if (esEste) Color(0xFF8B4A3E) else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // --- ENTRADA DE TEXTO ---
        Text(
            text = "Escribe lo que sientes...",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier
                .fillMaxWidth()
                .background(grisEncabezado, CircleShape)
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )

        OutlinedTextField(
            value = textoEmergencia,
            onValueChange = { textoEmergencia = it },
            placeholder = { Text("Escribe aquí para recibir una respuesta...", textAlign = TextAlign.Center) },
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            shape = RoundedCornerShape(16.dp)
        )

        // --- BOTÓN MANDAR A IA (DINÁMICO) ---
        Button(
            onClick = { /* Aquí conectas la API de Gemini */ },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = coralPrincipal),
            enabled = textoEmergencia.isNotBlank() || ubicacionSeleccionada.isNotEmpty()
        ) {
            Text("Pedir consejo al Avatar", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- RESPUESTA IA (CARD) ---
        Text(
            text = "Respuesta IA",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier
                .fillMaxWidth()
                .background(grisEncabezado, CircleShape)
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )

        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = fondoTarjeta)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "En este caso, podrías...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }

        // --- SECCIÓN: RECURSOS AUDITIVOS (SUPABASE READY) ---
        Text(
            text = "Recursos auditivos",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .background(coralPrincipal, CircleShape)
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // Ejemplo de Items que vendrán de Supabase
            ItemEmergenciaRecurso("Podcast de ayuda")
            ItemEmergenciaRecurso("Guía para emergencias")
        }
    }
}

@Composable
fun ItemEmergenciaRecurso(nombre: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(140.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.LightGray, RoundedCornerShape(24.dp))
                .clickable { /* Cargar recurso de Supabase */ }
        ) {
            Text("IMG", Modifier.align(Alignment.Center), color = Color.White)
        }
        Text(
            text = nombre,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}