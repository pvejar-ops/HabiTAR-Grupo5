package com.example.habitar.features.QueSomos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val moradoPrincipal = Color(0xFF5E5CE6)
private val moradoOscuro    = Color(0xFF26215C)
private val moradoMedio     = Color(0xFF6061D1)
private val fondoPantalla   = Color(0xFFF3EDF7)
private val blanco          = Color(0xFFFFFFFF)
private val negro           = Color(0xFF000000)

@Composable
fun QueSomosScreen(onNavigateToAuth: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fondoPantalla)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "🏠 Bienvenido/a a HABITAR",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                color = moradoOscuro,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Aplicación para personas neurodivergentes (TEA/CEA).",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = moradoPrincipal,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tu espacio seguro para reconocer emociones, regularte y conocerte mejor.",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = moradoOscuro,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "¿Qué encontrarás?",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = moradoMedio
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FeatureCardQS("🧠", "Mis emociones", "Identifica y registra cómo te sientes.", Modifier.weight(1f))
                FeatureCardQS("📚", "Aprender", "Descubre conceptos sobre emociones y bienestar.", Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FeatureCardQS("📅", "Mi calendario", "Anota cómo te sientes cada día.", Modifier.weight(1f))
                FeatureCardQS("📊", "Mis informes", "Observa tus avances y patrones emocionales.", Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = blanco),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, negro)
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                    Text("🎨", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Mi espacio", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = moradoOscuro)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            "Elige tu avatar, personaliza sonidos y estímulos visuales que te ayuden a sentir calma.",
                            fontSize = 11.sp, color = moradoMedio, lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(modifier = Modifier.weight(1f), thickness = 5.dp, color = moradoMedio)
                Spacer(modifier = Modifier.width(75.dp))
                HorizontalDivider(modifier = Modifier.weight(1f), thickness = 5.dp, color = moradoMedio)
            }
            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = blanco),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, negro)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🌟 HABITAR te acompaña", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = moradoOscuro)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Lleva contigo una herramienta de apoyo emocional dondequiera que vayas, antes de clases, durante el día o cuando necesites un momento para ti.",
                        fontSize = 12.sp, color = moradoMedio, lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Tu bienestar importa. Este es tu espacio.",
                        fontSize = 12.sp, color = moradoOscuro,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onNavigateToAuth,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = moradoPrincipal)
            ) {
                Text("Entendido, ir al login", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun FeatureCardQS(emoji: String, titulo: String, desc: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = blanco),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, negro)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(emoji, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(titulo, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = moradoOscuro)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(desc, fontSize = 11.sp, color = moradoMedio, lineHeight = 16.sp)
        }
    }
}