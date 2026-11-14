package com.example.habitar.features.autoconocimiento

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val MoradoHeader = Color(0xFF5E72E4)
private val MoradoPrimario = Color(0xFF6C63FF)
private val FondoPantalla = Color(0xFFF8F9FE)
private val TextoNegro = Color(0xFF1C1B1F)
private val TextoSecundario = Color(0xFF49454F)
private val GrisLavandaCirculo = Color(0xFFECECF4)
private val GrisNube = Color(0xFF9E9EB2)

@Composable
fun SinConexionScreen(
    onReintentarClick: () -> Unit,
    onVolverAlMenuClick: () -> Unit
) {
    Scaffold(
        containerColor = FondoPantalla
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Cabecera Cápsula "Autoconocimiento"
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MoradoHeader,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Autoconocimiento",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Círculo Gris Lavanda con Nube (Cero rojo de alarma)
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(GrisLavandaCirculo),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "☁",
                    fontSize = 48.sp,
                    color = GrisNube
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Título
            Text(
                text = "No pudimos conectarnos",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextoNegro,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subtítulo con alivio: "Tus respuestas están guardadas"
            Text(
                text = "Tus respuestas están guardadas en tu teléfono. Prepararemos tu orientación cuando vuelva la conexión.",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = TextoSecundario,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.weight(1.2f))

            // Botón Píldora: REINTENTAR
            Button(
                onClick = onReintentarClick,
                colors = ButtonDefaults.buttonColors(containerColor = MoradoPrimario),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Text(
                    text = "REINTENTAR",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Enlace: Volver al menú
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

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}