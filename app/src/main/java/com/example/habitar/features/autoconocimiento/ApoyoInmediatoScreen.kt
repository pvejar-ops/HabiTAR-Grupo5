package com.example.habitar.features.autoconocimiento

import android.content.Intent
import android.net.Uri
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
fun ApoyoInmediatoScreen(
    onVolverClick: () -> Unit,
    onIrAEmergenciaClick: () -> Unit
) {
    val context = LocalContext.current

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
                    OutlinedButton(
                        onClick = onVolverClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(28.dp),
                        border = BorderStroke(1.5.dp, MoradoPrimario)
                    ) {
                        Text(
                            text = "VOLVER A LO QUE ESTABA HACIENDO",
                            color = MoradoPrimario,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Cabecera Cápsula "Apoyo inmediato"
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
                        text = "Apoyo inmediato",
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
                text = "Lo que sientes es importante",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextoNegro
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Subtítulo
            Text(
                text = "Si estás pasando por un momento muy difícil, mereces apoyo ahora. No tienes que resolverlo solo/a.",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = TextoSecundario,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tarjeta 1: Ir a Emergencia (Destacada con borde morado)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onIrAEmergenciaClick() },
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = BorderStroke(1.5.dp, MoradoPrimario)
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
                            .background(Color(0xFF5E72E4)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "☎",
                            fontSize = 22.sp,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Ir a Emergencia",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextoNegro
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "Tus contactos y pasos para calmarte",
                            fontSize = 13.sp,
                            color = GrisApoyo
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tarjeta 2: Hablar con alguien de confianza
            Surface(
                modifier = Modifier.fillMaxWidth(),
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
                            .background(Color(0xFFE0F2E9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "♡",
                            fontSize = 22.sp,
                            color = Color(0xFF3B7A57)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Hablar con alguien de confianza",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextoNegro
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "Una persona cercana puede acompañarte",
                            fontSize = 13.sp,
                            color = GrisApoyo
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tarjeta 3: Servicios de salud o emergencia (Marca al 131 o 4141)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        // Enlace directo al fono salud 4141
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:4141"))
                        context.startActivity(intent)
                    },
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
                            .background(Color(0xFFEDF0FA)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✚",
                            fontSize = 20.sp,
                            color = MoradoHeader,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Servicios de salud o emergencia",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextoNegro
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "Si estás en peligro inmediato",
                            fontSize = 13.sp,
                            color = GrisApoyo
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}