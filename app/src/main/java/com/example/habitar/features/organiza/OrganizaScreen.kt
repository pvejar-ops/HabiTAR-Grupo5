package com.example.habitar.features.organiza

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habitar.features.organiza.OrganizaViewModel
import com.example.habitar.features.organiza.OrganizaRegistro
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn


data class TareaModel(
    val idTarea: Long? = null,          // int8 (autogenerado por la BDD)
    val descripcionTarea: String,       // text
    var completada: Boolean = false,    // bool
    val fechaTarea: String,             // date (YYYY-MM-DD)
    val idUsuario: String? = null       // uuid
)

@Composable
fun OrganizaDiaScreen(
    fechaFiltro: String,
    onNavigateToMenu: () -> Unit,
    viewModel: OrganizaViewModel = viewModel()
) {
    // 🌟 Cada vez que cambie la fecha (desde el calendario o el día actual), gatilla la carga desde la BDD
    LaunchedEffect(fechaFiltro) {
        viewModel.cargarTareas(fechaFiltro)
    }
    var nuevaTareaTexto by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .navigationBarsPadding(), // Respeta los botones nativos de Android
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = nuevaTareaTexto,
                    onValueChange = { nuevaTareaTexto = it },
                    placeholder = { Text("Escribe una tarea...", color = Color.Gray) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 12.dp),
                    colors = TextFieldDefaults.colors(
                        // En Material 3 se separan los estados del contenedor
                        focusedContainerColor = Color(0xFFF1F1F1),
                        unfocusedContainerColor = Color(0xFFF1F1F1),
                        disabledContainerColor = Color(0xFFF1F1F1),

                        // Los indicadores de la línea de abajo se quedan transparentes
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Button(
                    onClick = {
                        if (nuevaTareaTexto.isNotBlank()) {
                            // 🌟 Inserta en Supabase y refresca de forma automática
                            viewModel.agregarTarea(nuevaTareaTexto, fechaFiltro)
                            nuevaTareaTexto = ""
                        }
                    },
                    modifier = Modifier.size(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar Tarea", tint = Color.White)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            // --- BOTÓN VOLVER ---
            IconButton(
                onClick = onNavigateToMenu,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .background(color = Color(0xFFF3EDF7), shape = CircleShape)
                    .size(40.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item {
                    Text(
                        text = "Organiza tu día",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
                            .padding(vertical = 12.dp),
                        textAlign = TextAlign.Center
                    )
                }
                item {
                    Text(
                        text = "Control de tareas diarias",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    val fechaFormateada = remember(fechaFiltro) {
                        try {
                            val fechaParseada = java.time.LocalDate.parse(fechaFiltro)
                            val formateador = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")
                            fechaParseada.format(formateador)
                        } catch (e: Exception) {
                            fechaFiltro
                        }
                    }

                    Text(
                        text = buildAnnotatedString {
                            append("Actividades para el: ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(fechaFormateada)
                            }
                        },
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // 🌟 Feedback visual mientras conecta con el servidor de Supabase
                if (viewModel.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }

                // 🌟 Pintamos la lista reactiva oficial de OrganizaRegistro
                items(viewModel.listaTareas) { tarea ->
                    ItemTarea(
                        tarea = tarea,
                        onCheckedChange = { nuevoEstado ->
                            viewModel.cambiarEstadoTarea(tarea, nuevoEstado)
                        },
                        onDeleteClick = {
                            viewModel.eliminarTarea(tarea, fechaFiltro)
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun ItemTarea(
    tarea: OrganizaRegistro,
    onCheckedChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit
) {
    // Escucha el estado del check directamente de las mutaciones del ViewModel
    val isChecked = tarea.completada ?: false

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isChecked) MaterialTheme.colorScheme.surfaceVariant else Color(0xFFFAF8FC)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { onCheckedChange(it) }
            )
            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = tarea.descripcionTarea,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isChecked) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f) // 🌟 Esto empuja al basurero por completo hacia la derecha
            )

            // Botón de basurero alineado a la derecha
            IconButton(
                onClick = onDeleteClick
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar Tarea",
                    tint = Color.Gray.copy(alpha = 0.7f) // Un gris sutil para que combine con el look de HabiTAR
                )
            }
        }
    }
}