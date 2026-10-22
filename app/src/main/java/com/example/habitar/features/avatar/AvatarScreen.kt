package com.example.habitar.features.avatar

import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp


private val morado = Color(0xFF6750A4)
private val moradoSuave = Color(0xFFEEEDFE)

@Composable
fun AvatarScreen(
    onNavigateToIA: () -> Unit, // <--- DEBES AGREGAR ESTO
    onAuthSuccess: () -> Unit   // También este si lo vas a usar
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Personaliza tu avatar",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Placeholder para el visualizador 3D
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text("Aquí se mostrará el modelo 3D del avatar")
            // Más adelante usaremos SceneView aquí
        }

        Button(
            // Aquí iría la lógica de guardado en Supabase más adelante
            onClick = onNavigateToIA,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp), // Espacio extra del borde inferior
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Finalizar y Crear Avatar")
        }
    }
}

//Código para generar avatar con ia, diseño de pestaña
/*@Composable
fun AvatarScreen(
    onNavigateToIA: () -> Unit,
    onAuthSuccess: () -> Unit,
    viewModel: AvatarViewModel = viewModel()
) {
    LaunchedEffect(viewModel.guardadoExitoso) {
        if (viewModel.guardadoExitoso) {
            onNavigateToIA()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3EDF7))
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Crea tu Avatar",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(morado, CircleShape)
                .padding(vertical = 12.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Personaliza tu avatar eligiendo las opciones de cada sección.",
            fontSize = 13.sp,
            color = Color(0xFF6061D1),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tipo de avatar y Género en la misma fila
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                SeccionDesplegable(titulo = "Tipo de avatar") {
                    SelectorOpcion(
                        opciones = listOf("Persona", "Animal"),
                        seleccionado = viewModel.tipoAvatar,
                        onSeleccionar = { viewModel.tipoAvatar = it }
                    )
                }
            }
            if (viewModel.tipoAvatar == "Persona") {
                Box(modifier = Modifier.weight(1f)) {
                    SeccionDesplegable(titulo = "Género") {
                        SelectorOpcion(listOf("Femenino", "Masculino"), viewModel.genero) { viewModel.genero = it }
                    }
                }
            } else {
                Box(modifier = Modifier.weight(1f)) {
                    SeccionDesplegable(titulo = "Tipo de animal") {
                        SelectorOpcion(listOf("Gato", "Perro", "Conejo", "Zorro", "Oso"), viewModel.tipoAnimal) { viewModel.tipoAnimal = it }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (viewModel.tipoAvatar == "Persona") {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    SeccionDesplegable(titulo = "Color de piel") {
                        SelectorOpcion(listOf("Claro", "Medio", "Oscuro"), viewModel.colorPiel) { viewModel.colorPiel = it }
                    }
                }
                Box(modifier = Modifier.weight(1f)) {
                    SeccionDesplegable(titulo = "Color de cabello") {
                        SelectorOpcion(listOf("Negro", "Castaño", "Rubio", "Pelirrojo", "Gris", "Blanco"), viewModel.colorCabello) { viewModel.colorCabello = it }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    SeccionDesplegable(titulo = "Color de ojos") {
                        SelectorOpcion(listOf("Café", "Verde", "Azul", "Gris", "Negro"), viewModel.colorOjosPersona) { viewModel.colorOjosPersona = it }
                    }
                }
                Box(modifier = Modifier.weight(1f)) {
                    SeccionDesplegable(titulo = "Estilo de ropa") {
                        SelectorOpcion(listOf("Casual", "Formal", "Deportiva", "Universitaria"), viewModel.ropa) { viewModel.ropa = it }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            SeccionDesplegable(titulo = "Color de ropa") {
                SelectorOpcion(listOf("Azul", "Verde", "Rojo", "Amarillo", "Morado", "Negro", "Blanco", "Rosa"), viewModel.colorRopa) { viewModel.colorRopa = it }
            }
        } else {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    SeccionDesplegable(titulo = "Color de pelo") {
                        SelectorOpcion(listOf("Negro", "Blanco", "Café", "Naranja", "Gris", "Dorado"), viewModel.colorPelo) { viewModel.colorPelo = it }
                    }
                }
                Box(modifier = Modifier.weight(1f)) {
                    SeccionDesplegable(titulo = "Color de ojos") {
                        SelectorOpcion(listOf("Café", "Verde", "Azul", "Amarillo", "Negro"), viewModel.colorOjosAnimal) { viewModel.colorOjosAnimal = it }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            SeccionDesplegable(titulo = "Accesorio") {
                SelectorOpcion(listOf("Sin accesorio", "Bufanda", "Sombrero", "Corbatín"), viewModel.accesorio) { viewModel.accesorio = it }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Vista previa
        viewModel.avatarGenerado?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Avatar generado",
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        viewModel.mensajeError?.let {
            Text(text = it, color = Color.Red, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = { viewModel.generarAvatar() },
            modifier = Modifier.fillMaxWidth(),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = morado),
            enabled = !viewModel.estaCargando
        ) {
            Text(
                text = if (viewModel.estaCargando) "Generando..." else "Generar Avatar",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        if (viewModel.avatarGenerado != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { viewModel.guardarAvatar() },
                modifier = Modifier.fillMaxWidth(),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                enabled = !viewModel.estaCargando
            ) {
                Text(
                    text = if (viewModel.estaCargando) "Guardando..." else "Guardar y Continuar",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun SeccionDesplegable(
    titulo: String,
    expandidoInicial: Boolean = false,
    contenido: @Composable () -> Unit
) {
    var expandido by remember { mutableStateOf(expandidoInicial) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header clickeable
            Surface(
                onClick = { expandido = !expandido },
                color = Color.Transparent,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = titulo,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF26215C)
                    )
                    Icon(
                        imageVector = if (expandido) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = morado
                    )
                }
            }

            AnimatedVisibility(visible = expandido) {
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).padding(bottom = 12.dp)) {
                    contenido()
                }
            }
        }
    }
}

@Composable
fun SelectorOpcion(
    opciones: List<String>,
    seleccionado: String,
    onSeleccionar: (String) -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        opciones.forEach { opcion ->
            FilterChip(
                selected = seleccionado == opcion,
                onClick = { onSeleccionar(opcion) },
                label = { Text(opcion, fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = morado,
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}*/