package com.example.habitar.features.autoconocimiento

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val MoradoHeader = Color(0xFF5E72E4)
private val MoradoPrimario = Color(0xFF6C63FF)
private val FondoPantalla = Color(0xFFF8F9FE)
private val TextoNegro = Color(0xFF1C1B1F)
private val TextoSecundario = Color(0xFF6B7280)
private val VerdeDisclaimerFondo = Color(0xFFEDF6F2)
private val VerdeDisclaimerBarra = Color(0xFF6C9A8B)
private val VerdeDisclaimerTexto = Color(0xFF2E5A44)

@Composable
fun ResultadosScreen(
    viewModel: AutoconocimientoViewModel,
    onVolverClick: () -> Unit,
    onInformePdfClick: () -> Unit,
    onExplorarClick: () -> Unit,
    onReiniciarCuestionarioClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var mostrarDialogoReiniciar by remember { mutableStateOf(false) }

    val perfil = viewModel.perfilResultado ?: viewModel.calcularPerfilReal()

    Scaffold(
        containerColor = FondoPantalla,
        bottomBar = {
            Surface(
                color = FondoPantalla,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onInformePdfClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(26.dp),
                        border = BorderStroke(1.5.dp, MoradoPrimario)
                    ) {
                        Text(
                            text = "INFORME PDF",
                            color = MoradoPrimario,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Button(
                        onClick = onExplorarClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(26.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MoradoPrimario)
                    ) {
                        Text(
                            text = "EXPLORAR",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Cabecera Cápsula "Tu orientación"
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
                    IconButton(onClick = onVolverClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "Tu orientación",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    IconButton(onClick = { mostrarDialogoReiniciar = true }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reiniciar cuestionario",
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Banner ético superior
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(VerdeDisclaimerFondo)
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(48.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(VerdeDisclaimerBarra)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Esto no es un diagnóstico. Describe patrones en tus respuestas. Si algo te hace sentido, puedes conversarlo con un profesional.",
                    fontSize = 13.sp,
                    color = VerdeDisclaimerTexto,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Tarjeta 1: Cómo me relaciono (CAT-Q)
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Cómo me relaciono",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoNegro
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ItemSubescala(
                        titulo = perfil.esfuerzoSocial.titulo,
                        nivelTexto = perfil.esfuerzoSocial.nivelTexto,
                        nivelColor = Color(perfil.esfuerzoSocial.nivelColorHex),
                        progreso = perfil.esfuerzoSocial.progreso,
                        descripcion = perfil.esfuerzoSocial.descripcion
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    ItemSubescala(
                        titulo = perfil.cuidarMuestras.titulo,
                        nivelTexto = perfil.cuidarMuestras.nivelTexto,
                        nivelColor = Color(perfil.cuidarMuestras.nivelColorHex),
                        progreso = perfil.cuidarMuestras.progreso,
                        descripcion = perfil.cuidarMuestras.descripcion
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta 2: Rutinas e intereses (RBQ-2A)
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Rutinas e intereses",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoNegro
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ItemSubescala(
                        titulo = perfil.preferenciaPredecible.titulo,
                        nivelTexto = perfil.preferenciaPredecible.nivelTexto,
                        nivelColor = Color(perfil.preferenciaPredecible.nivelColorHex),
                        progreso = perfil.preferenciaPredecible.progreso,
                        descripcion = perfil.preferenciaPredecible.descripcion
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    ItemSubescala(
                        titulo = perfil.movimientosRepetidos.titulo,
                        nivelTexto = perfil.movimientosRepetidos.nivelTexto,
                        nivelColor = Color(perfil.movimientosRepetidos.nivelColorHex),
                        progreso = perfil.movimientosRepetidos.progreso,
                        descripcion = perfil.movimientosRepetidos.descripcion
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón destacado: Volver a hacer el cuestionario
            OutlinedButton(
                onClick = { mostrarDialogoReiniciar = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0xFFE53935).copy(alpha = 0.5f)),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFFE53935)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Volver a realizar el cuestionario",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        // Diálogo ético de confirmación
        if (mostrarDialogoReiniciar) {
            AlertDialog(
                onDismissRequest = { mostrarDialogoReiniciar = false },
                title = { Text(text = "¿Reiniciar y comenzar de cero?", fontWeight = FontWeight.Bold) },
                text = {
                    Text(
                        text = "Se borrarán tus respuestas anteriores de la memoria del teléfono para que puedas realizar nuevamente el cuestionario.",
                        fontSize = 14.sp,
                        color = TextoNegro
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.reiniciarProgreso()
                            mostrarDialogoReiniciar = false
                            Toast.makeText(context, "Respuestas eliminadas. Empezando de nuevo.", Toast.LENGTH_SHORT).show()
                            onReiniciarCuestionarioClick()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
                    ) {
                        Text(text = "Sí, reiniciar", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogoReiniciar = false }) {
                        Text(text = "Cancelar", color = Color(0xFF6B7280))
                    }
                }
            )
        }
    }
}

@Composable
private fun ItemSubescala(
    titulo: String,
    nivelTexto: String,
    nivelColor: Color,
    progreso: Float,
    descripcion: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = titulo,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextoNegro,
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(nivelColor)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = nivelTexto,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { progreso },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = nivelColor,
            trackColor = Color(0xFFE5E7EB)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = descripcion,
            fontSize = 13.sp,
            color = TextoSecundario,
            lineHeight = 17.sp
        )
    }
}