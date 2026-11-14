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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
private val TextoSecundario = Color(0xFF49454F)
private val GrisApoyo = Color(0xFF6B7280)

@Composable
fun ProximosPasosScreen(
    onVolverClick: () -> Unit,
    onVolverAlMenuClick: () -> Unit,
    onIrAHerramientasClick: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        containerColor = FondoPantalla
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Cabecera Cápsula "Próximos pasos"
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
                        text = "Próximos pasos",
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

            Spacer(modifier = Modifier.height(24.dp))

            // Título
            Text(
                text = "Si quieres profundizar",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextoNegro
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Subtítulo
            Text(
                text = "Esta orientación puede ser un buen punto de partida para conversar con alguien.",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = TextoSecundario,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tarjeta 1: Servicios de apoyo universidad
            TarjetaApoyo(
                icono = "✚",
                iconoFondo = Color(0xFFEDF0FA),
                iconoColor = MoradoHeader,
                titulo = "Servicios de apoyo de tu universidad",
                subtitulo = "Atención psicológica y orientación estudiantil"
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Tarjeta 2: Profesional de salud mental
            TarjetaApoyo(
                icono = "⚕",
                iconoFondo = Color(0xFFF0EEFA),
                iconoColor = MoradoPrimario,
                titulo = "Profesional de salud mental",
                subtitulo = "Lleva tu informe en PDF si te sirve"
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Tarjeta 3: Herramientas de HabiTAR
            TarjetaApoyo(
                icono = "❀",
                iconoFondo = Color(0xFFE0F2E9),
                iconoColor = Color(0xFF3B7A57),
                titulo = "Herramientas de HabiTAR",
                subtitulo = "Meditación, Sensaciones y Organízate",
                onClick = onIrAHerramientasClick
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Botón Píldora: DESCARGAR MI INFORME
            Button(
                onClick = {
                    Toast.makeText(context, "Generando informe PDF...", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MoradoPrimario),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Text(
                    text = "DESCARGAR MI INFORME",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón secundario: Volver al menú
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Volver al menú",
                    color = MoradoPrimario,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onVolverAlMenuClick() }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun TarjetaApoyo(
    icono: String,
    iconoFondo: Color,
    iconoColor: Color,
    titulo: String,
    subtitulo: String,
    onClick: (() -> Unit)? = null
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconoFondo),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icono,
                    fontSize = 20.sp,
                    color = iconoColor,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoNegro
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = subtitulo,
                    fontSize = 13.sp,
                    color = GrisApoyo,
                    lineHeight = 18.sp
                )
            }
        }
    }
}