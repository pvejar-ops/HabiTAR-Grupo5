package com.example.habitar.features.autoconocimiento

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val MoradoPrimario = Color(0xFF6C63FF)
private val MoradoHeader = Color(0xFF5E72E4)
private val FondoPantalla = Color(0xFFF8F9FE)
private val TextoNegro = Color(0xFF1C1B1F)
private val GrisBorde = Color(0xFFE0E0E0)
private val MoradoSeleccionFondo = Color(0xFFF0EEFF)
private val GrisDotInactivo = Color(0xFFDCDCE5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreguntaScreen(
    idSeccion: Int,
    viewModel: AutoconocimientoViewModel,
    indiceInicial: Int = 0,
    esModoEdicion: Boolean = false,
    onVolverClick: () -> Unit,
    onPausarClick: () -> Unit,
    onFinishSeccion: () -> Unit
) {
    val preguntas = if (idSeccion == 1) viewModel.preguntasCatq else viewModel.preguntasRbq2a
    val totalPreguntas = preguntas.size

    var indiceActual by rememberSaveable(indiceInicial) { mutableIntStateOf(indiceInicial) }
    val idPreguntaActual = indiceActual + 1

    val respuestaGuardada = viewModel.obtenerRespuestaActual(idSeccion, idPreguntaActual)
    var opcionSeleccionada by remember(indiceActual, respuestaGuardada) {
        mutableStateOf(respuestaGuardada)
    }

    val textoPregunta = preguntas.getOrElse(indiceActual) { "" }
    val tituloSeccion = if (idSeccion == 1) "Cómo me relaciono" else "Rutinas e intereses"

    val opciones = if (idSeccion == 1) {
        listOf(
            1 to "Totalmente en desacuerdo",
            2 to "En desacuerdo",
            3 to "Algo en desacuerdo",
            4 to "Neutro",
            5 to "Algo de acuerdo",
            6 to "De acuerdo",
            7 to "Totalmente de acuerdo"
        )
    } else {
        listOf(
            1 to "Nunca",
            2 to "A veces",
            3 to "Con frecuencia"
        )
    }

    val alturaOpcion = if (idSeccion == 2) 56.dp else 46.dp

    Scaffold(
        containerColor = FondoPantalla
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Cabecera Cápsula
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MoradoHeader,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        if (esModoEdicion) {
                            onVolverClick()
                        } else if (indiceActual > 0) {
                            indiceActual--
                        } else {
                            onVolverClick()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retroceder",
                            tint = Color.White
                        )
                    }

                    Text(
                        text = tituloSeccion,
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 48.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Indicador de Progreso con Bolitas (AHORA CLICKEABLES)
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                itemsIndexed(preguntas) { index, _ ->
                    val yaRespondida = viewModel.obtenerRespuestaActual(idSeccion, index + 1) != null
                    val esActual = index == indiceActual

                    val colorPunto = when {
                        esActual -> MoradoPrimario
                        yaRespondida -> MoradoHeader
                        else -> GrisDotInactivo
                    }

                    val tamano = if (esActual) 9.dp else 7.dp

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .size(tamano)
                            .clip(CircleShape)
                            .background(colorPunto)
                            .clickable { indiceActual = index }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Enunciado de la Pregunta con Botón (?)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "${indiceActual + 1}. $textoPregunta",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextoNegro,
                    lineHeight = 24.sp,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MoradoPrimario)
                        .clickable { viewModel.setAyudaVisible(true) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "?",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Lista de Opciones
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                opciones.forEach { (valor, texto) ->
                    val seleccionada = opcionSeleccionada == valor

                    val colorFondo = if (seleccionada) MoradoSeleccionFondo else Color.White
                    val colorBorde = if (seleccionada) MoradoPrimario else GrisBorde
                    val grosorBorde = if (seleccionada) 2.dp else 1.dp
                    val pesoTexto = if (seleccionada) FontWeight.Bold else FontWeight.Normal

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(alturaOpcion)
                            .clickable {
                                opcionSeleccionada = valor
                                viewModel.guardarRespuesta(idSeccion, idPreguntaActual, valor)
                            },
                        shape = RoundedCornerShape(12.dp),
                        color = colorFondo,
                        border = BorderStroke(grosorBorde, colorBorde)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = texto,
                                fontSize = 15.sp,
                                fontWeight = pesoTexto,
                                color = if (seleccionada) MoradoPrimario else TextoNegro
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botones Inferiores Inteligentes
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        if (esModoEdicion) onVolverClick() else onPausarClick()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(26.dp),
                    border = BorderStroke(1.5.dp, MoradoPrimario)
                ) {
                    Text(
                        text = if (esModoEdicion) "CANCELAR" else "PAUSAR",
                        color = MoradoPrimario,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                val esUltimaPregunta = indiceActual == totalPreguntas - 1

                val textoBoton = when {
                    esModoEdicion -> "GUARDAR Y REVISAR"
                    esUltimaPregunta -> "REVISAR"
                    else -> "SIGUIENTE"
                }

                Button(
                    onClick = {
                        if (esModoEdicion || esUltimaPregunta) {
                            onFinishSeccion()
                        } else {
                            indiceActual++
                        }
                    },
                    enabled = opcionSeleccionada != null,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MoradoPrimario,
                        disabledContainerColor = GrisBorde
                    )
                ) {
                    Text(
                        text = textoBoton,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // BottomSheet S07: Ayuda
        if (viewModel.mostrarAyudaBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.setAyudaVisible(false) },
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 36.dp)
                ) {
                    Text(
                        text = "¿A qué se refiere esta pregunta?",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoNegro
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val textoExplicacion = if (idSeccion == 1) {
                        "Describe el esfuerzo consciente por adaptar tu conducta social. Por ejemplo: ensayar antes qué decir al saludar a un profesor, o prestar atención activa a mirar a los ojos para que la otra persona no se sienta incómoda."
                    } else {
                        "Se refiere a rutinas, hábitos o intereses absorbentes en tu vida diaria. Por ejemplo: preferir sentarte siempre en el mismo banco de la sala de clases, o investigar a fondo un tema hasta saber cada detalle."
                    }

                    Text(
                        text = textoExplicacion,
                        fontSize = 15.sp,
                        color = Color(0xFF49454F),
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.setAyudaVisible(false) },
                        colors = ButtonDefaults.buttonColors(containerColor = MoradoPrimario),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(
                            text = "Entendido",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}