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
import androidx.activity.compose.LocalActivity


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
    val activity = LocalActivity.current as androidx.activity.ComponentActivity

    DisposableEffect(Unit) {
        val originalMode = activity.window.attributes.softInputMode
        activity.window.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        onDispose {
            activity.window.setSoftInputMode(originalMode) //
        }
    }

    // 🌟 Cada vez que cambie la fecha (desde el calendario o el día actual), gatilla la carga desde la BDD
    LaunchedEffect(fechaFiltro) {
        viewModel.cargarTareas(fechaFiltro)
    }
    var nuevaTareaTexto by remember { mutableStateOf("") }
    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding() // Protege la flecha de la barra de notificaciones arriba
    ) {
        // 💡 UN SOLO CONTENEDOR SCROLLEABLE
        // Al meter la barra de agregar dentro de la misma LazyColumn como el último item.
        // logramos que todo pertenezca a la misma pestaña deslizable.
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(
                top = 0.dp,
                bottom = 120.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            // 1. Botón Volver
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 16.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    IconButton(
                        onClick = onNavigateToMenu,
                        modifier = Modifier
                            .background(color = Color(0xFFF3EDF7), shape = CircleShape)
                            .size(40.dp)
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // 2. Título Principal
            item {
                Text(
                    text = "Organiza tu día",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
                        .padding(vertical = 12.dp),
                    textAlign = TextAlign.Center
                )
            }

            // 3. Subtítulo
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

            // 4. Fecha actual formateada
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

            // 5. Loading de Supabase
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

            // 6. Listado reactivo de Tareas
            items(viewModel.listaTareas) { tarea ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp) // 💡 Las tarjetas ahora aprovechan mejor el ancho del fono
                )
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

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // 💡 7. LA BARRA DE ENTRADA (Ahora es un ítem más dentro del scroll)
            // Al estar aquí, el teclado la levanta y puedes scrollear hacia arriba o abajo
            // para ver los títulos o las tareas previas sin que nada quede bloqueado ni recortado.
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp), // MISMO margen que las cards
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White),
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
                                focusedContainerColor = Color(0xFFF1F1F1),
                                unfocusedContainerColor = Color(0xFFF1F1F1),
                                disabledContainerColor = Color(0xFFF1F1F1),
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
            }
            item {
                Spacer(modifier = Modifier.height(80.dp))
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isChecked)
                MaterialTheme.colorScheme.surfaceVariant
            else
                Color(0xFFFAF8FC)
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