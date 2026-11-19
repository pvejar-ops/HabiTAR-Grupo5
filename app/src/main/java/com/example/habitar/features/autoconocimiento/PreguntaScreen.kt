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

            // Indicador de Progreso con Bolitas (Clickeables)
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

        // BottomSheet S07: Ayuda Contextual Única por Pregunta
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

                    val textoExplicacion = obtenerExplicacionPregunta(idSeccion, idPreguntaActual)

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

/**
 * Mapeo clínico contextual de cada pregunta del screening
 * con ejemplos de la vida universitaria cotidiana.
 */
private fun obtenerExplicacionPregunta(idSeccion: Int, numeroPregunta: Int): String {
    if (idSeccion == 1) {
        // CAT-Q (Preguntas 1 a 25)
        return when (numeroPregunta) {
            1 -> "Monitoreo corporal: Por ejemplo, controlar conscientemente si estás sonriendo, dónde pones las manos o si tu postura se ve 'normal' frente a otros."
            2 -> "Contacto visual forzado: Mirar a los ojos porque sabes que es una norma social esperada, aunque te resulte incómodo, cansador o te cueste concentrarte."
            3 -> "Guiones mentales previos: Ensayar mentalmente frases exactas, bromas o preguntas antes de entrar a clases, hacer un trámite o saludar a alguien."
            4 -> "Imitación de conductas: Copiar los gestos, tono de voz o modismos de tus compañeros para encajar mejor en el grupo."
            5 -> "Sobreesfuerzo social: Sentir que relacionarte con otros requiere tanta concentración y energía como rendir un examen difícil."
            6 -> "Control de impulsos de movimiento: Esconder o reprimir movimientos como mover los pies, balancearte o tocar objetos para no llamar la atención."
            7 -> "Monitoreo del tono de voz: Regular conscientemente el volumen, velocidad o entonación al hablar para sonar empático o interesado."
            8 -> "Aprender reglas sociales como teoría: Sentir que las interacciones no salen naturales, sino que las has aprendido leyendo, observando o cometiendo errores."
            9 -> "Fatiga por interacción: Terminar el día con agotamiento extremo o dolor de cabeza tras haber estado en grupos, necesitando silencio absoluto."
            10 -> "Desconexión entre lo interno y lo externo: Sentirte ansioso, abrumado o molesto por dentro, pero mantener una cara neutral o sonriente por fuera."
            11 -> "Seguir conversaciones por cortesía: Prestar atención deliberada a cuándo te toca hablar o asentir para que no se note que te perdiste."
            12 -> "Preparar temas de conversación: Investigar sobre música, series o fútbol solo para tener de qué hablar en los recreos o reuniones."
            13 -> "Camuflaje de dificultades: Disimular que te molestan los ruidos o las luces de la sala de clases para no parecer 'exagerado/a'."
            14 -> "Necesidad de recuperación solitaria: Requerir horas a solas sin hablar con nadie después de un evento social, carrete o trabajo grupal."
            15 -> "Sensación de actuar un personaje: Sentir que te pones una 'máscara' adaptada a cada grupo de personas con las que estás."
            16 -> "Dificultad con la espontaneidad: Preferir que las interacciones tengan un objetivo claro (ej. trabajo académico) antes que una charla informal libre."
            17 -> "Autoobservación constante: Juzgarte mentalmente mientras hablas: '¿sonó raro lo que dije?', '¿estoy hablando muy rápido?'."
            18 -> "Imitación de expresiones: Practicar sonrisas, caras de sorpresa o asentimientos frente al espejo o mentalmente."
            19 -> "Incomodidad con charlas casuales ('small talk'): Costarte hablar del clima o de temas superficiales, prefiriendo temas profundos o directos."
            20 -> "Uso de intermediarios sociales: Preferir salir en grupo con un amigo de confianza que lleve la conversación para no tener que iniciarla tú."
            21 -> "Fingir interés por empatía o armonía: Asentir y sonreír ante lo que te cuentan, aunque por dentro no comprendas por qué les resulta importante, para no incomodar."
            22 -> "Ocultar intereses especiales: Evitar hablar de lo que verdaderamente te apasiona por miedo a que los demás piensen que eres intenso/a o aburrido/a."
            23 -> "Estrategias de salida social: Planificar excusas de antemano para poder retirarte de reuniones o fiestas cuando tu batería social se agote."
            24 -> "Sensación de aislamiento pese a integrarte: Sentir que la gente conoce la versión adaptada de ti, pero no tu forma de ser más auténtica."
            25 -> "Monitoreo de la distancia física: Pensar si estás parado demasiado cerca o lejos de la otra persona durante una conversación."
            else -> "Describe el esfuerzo consciente por adaptar tu conducta y lenguaje en contextos sociales cotidianos."
        }
    } else {
        // RBQ-2A (Preguntas 1 a 10)
        return when (numeroPregunta) {
            1 -> "Movimientos repetitivos corporales: Balanceo del tronco, aleteo de manos o caminar en puntillas cuando estás concentrado, ansioso o feliz."
            2 -> "Manipulación repetitiva de objetos: Girar tapas, hacer clic al lápiz, enrollar cables o manipular texturas de forma reiterada."
            3 -> "Interés sensorial específico: Fascinación por mirar luces que titilan, tocar telas suaves o escuchar el mismo sonido repetidas veces."
            4 -> "Adherencia estricta a rutinas: Sentir malestar notable si cambia el orden habitual en que te preparas por la mañana o el trayecto a la universidad."
            5 -> "Necesidad de previsibilidad: Preferir saber con anticipación exacta la hora, el lugar y quiénes asistirán a una actividad antes de ir."
            6 -> "Insistencia en el orden o simetría: Necesitar que tus apuntes, escritorio o cosas personales estén ordenadas bajo un criterio muy específico."
            7 -> "Intereses hiperfocalizados: Investigar un tema en profundidad durante horas seguidas, olvidando comer o dormir por la alta concentración."
            8 -> "Resistencia a cambios repentinos: Irritación o bloqueo si un profesor cancela una clase a última hora o si cambian las instrucciones de una entrega."
            9 -> "Repetición verbal o de sonidos (ecolalia): Repetir palabras, frases de películas o melodías en tu mente o en voz baja como mecanismo de calma."
            10 -> "Apego a objetos específicos: Llevar siempre el mismo objeto de apoyo (un llavero, auricular, prenda de ropa) que te brinde seguridad y calma."
            else -> "Se refiere a patrones de rutina, preferencias sensoriales o intereses profundos en tu vida diaria."
        }
    }
}