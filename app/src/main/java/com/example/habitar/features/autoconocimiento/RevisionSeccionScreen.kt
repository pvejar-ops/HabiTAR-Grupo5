package com.example.habitar.features.autoconocimiento

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val MoradoPrimario = Color(0xFF6C63FF)
private val MoradoHeader = Color(0xFF5E72E4)
private val FondoPantalla = Color(0xFFF8F9FE)
private val TextoNegro = Color(0xFF1C1B1F)
private val TextoSecundario = Color(0xFF49454F)
private val GrisApoyo = Color(0xFF798E9C)
private val SeparadorColor = Color(0xFFF0F0F5)

@Composable
fun RevisionSeccionScreen(
    idSeccion: Int,
    preguntas: List<String>,
    respuestas: Map<Int, Int>,
    onVolverClick: () -> Unit,
    onEditarPreguntaClick: (indicePregunta: Int) -> Unit,
    onConfirmarClick: () -> Unit
) {
    val nombreSeccion = when (idSeccion) {
        1 -> "Cómo me relaciono"
        2 -> "Rutinas e intereses"
        3 -> "Sensibilidad sensorial"
        else -> "Comunicación"
    }

    fun obtenerTextoOpcion(valor: Int): String {
        return if (idSeccion == 2) {
            when (valor) {
                1 -> "Nunca"
                2 -> "A veces"
                3 -> "Con frecuencia"
                else -> "Sin responder"
            }
        } else {
            when (valor) {
                1 -> "Totalmente en desacuerdo"
                2 -> "En desacuerdo"
                3 -> "Algo en desacuerdo"
                4 -> "Neutro"
                5 -> "Algo de acuerdo"
                6 -> "De acuerdo"
                7 -> "Totalmente de acuerdo"
                else -> "Sin responder"
            }
        }
    }

    Scaffold(
        containerColor = FondoPantalla,
        bottomBar = {
            Surface(
                color = FondoPantalla,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                ) {
                    Button(
                        onClick = onConfirmarClick,
                        colors = ButtonDefaults.buttonColors(containerColor = MoradoPrimario),
                        shape = RoundedCornerShape(28.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                    ) {
                        Text(
                            text = "CONFIRMAR SECCIÓN",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Cabecera Cápsula "Revisa tus respuestas"
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MoradoHeader,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onVolverClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "Revisa tus respuestas",
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

            Spacer(modifier = Modifier.height(20.dp))

            // Texto explicativo
            Text(
                text = "Antes de terminar “$nombreSeccion”, puedes revisar y cambiar lo que quieras.",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = TextoSecundario,
                lineHeight = 22.sp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lista deslizable de preguntas con sus respuestas
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
            ) {
                itemsIndexed(preguntas) { index, textoPregunta ->
                    val idPregunta = index + 1
                    val valor = respuestas[idPregunta] ?: 0
                    val textoRespuesta = obtenerTextoOpcion(valor)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onEditarPreguntaClick(index) }
                            .padding(vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "$idPregunta. $textoPregunta",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = TextoNegro,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 12.dp)
                            )

                            Text(
                                text = textoRespuesta,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MoradoPrimario,
                                textAlign = TextAlign.End
                            )
                        }
                    }

                    HorizontalDivider(
                        color = SeparadorColor,
                        thickness = 1.dp
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Toca una respuesta para editarla · ${preguntas.size} en total",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = GrisApoyo,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}