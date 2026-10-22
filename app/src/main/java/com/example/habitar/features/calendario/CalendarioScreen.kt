package com.example.habitar.features.calendario

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarioScreen(
    onNavigateToMenu: () -> Unit,
    onNavigateToTodo: (String) -> Unit
) {
    // --- LÓGICA DE NAVEGACIÓN DEL MES ---
    var mesActual by remember { mutableStateOf(YearMonth.now()) }
    val hoyReal = LocalDate.now()

    // Colores de tu paleta
    val moradoPrincipal = Color(0xFF6750A4)
    val lilaSeleccion = Color(0xFFD671F5)
    val grisBorde = Color(0xFFE0E0E0)

    // 🌟 Ajuste: Cálculo de días memorizado para optimizar recomposiciones
    val (diasEnMes, espaciosVacios) = remember(mesActual) {
        Pair(mesActual.lengthOfMonth(), mesActual.atDay(1).dayOfWeek.value - 1)
    }

    Scaffold(
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            // --- BOTÓN VOLVER (Idéntico a Organiza) ---
            IconButton(
                onClick = onNavigateToMenu,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .background(color = Color(0xFFF3EDF7), shape = CircleShape)
                    .size(40.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = moradoPrincipal)
            }

            Spacer(modifier = Modifier.height(16.dp))
            // --- TÍTULO ---
            Text(
                text = "Calendario",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = moradoPrincipal, shape = CircleShape)
                    .padding(vertical = 12.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Control mensual de tareas",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            // --- TARJETA DEL CALENDARIO ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    // --- CABECERA DINÁMICA ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Muestra el nombre del mes y año según el estado 'mesActual'
                        Text(
                            text = "${mesActual.month.getDisplayName(TextStyle.FULL, Locale("es", "CL")).replaceFirstChar { it.uppercase() }} ${mesActual.year}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp
                        )
                        Row {
                            // Al hacer clic, restamos un mes al estado
                            IconButton(onClick = { mesActual = mesActual.minusMonths(1) }) {
                                Icon(Icons.Default.KeyboardArrowLeft, "Anterior", tint = Color.Gray)
                            }
                            // Al hacer clic, sumamos un mes al estado
                            IconButton(onClick = { mesActual = mesActual.plusMonths(1) }) {
                                Icon(Icons.Default.KeyboardArrowRight, "Siguiente")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Días de la semana (Cabecera)
                    val diasSemana = listOf("Lu", "Ma", "Mi", "Ju", "Vi", "Sa", "Su")
                    Row(modifier = Modifier.fillMaxWidth()) {
                        diasSemana.forEach { dia ->
                            Text(dia, Modifier.weight(1f), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // --- CÁLCULO DE LA GRILLA ---
                    val diasEnMes = mesActual.lengthOfMonth()
                    // Calculamos en qué día de la semana empieza el día 1 (1 = Lunes, 7 = Domingo)
                    val primerDiaSemana = mesActual.atDay(1).dayOfWeek.value
                    val espaciosVacios = primerDiaSemana - 1

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(7),
                        modifier = Modifier.height(260.dp),
                        userScrollEnabled = false
                    ) {
                        // 1. Celdas vacías del mes anterior
                        items(espaciosVacios) {
                            Box(Modifier.aspectRatio(1f).border(0.5.dp, grisBorde))
                        }

                        // 2. Días del mes actual
                        items(diasEnMes) { diaIdx ->
                            val dia = diaIdx + 1
                            val fechaSeleccionada = mesActual.atDay(dia) // Esto es un objeto LocalDate
                            val esHoy = fechaSeleccionada == hoyReal

                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .border(0.5.dp, grisBorde)
                                    .background(if (esHoy) lilaSeleccion else Color.Transparent)
                                    .clickable { onNavigateToTodo(fechaSeleccionada.toString()) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = dia.toString(),
                                    color = if (esHoy) Color.White else Color.Black,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}