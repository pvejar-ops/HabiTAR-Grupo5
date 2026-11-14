package com.example.habitar.features.autoconocimiento

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
private val VerdeDisclaimerFondo = Color(0xFFEDF6F2)
private val VerdeDisclaimerBarra = Color(0xFF6C9A8B)
private val VerdeDisclaimerTexto = Color(0xFF2E5A44)

@Composable
fun ProcesandoScreen(
    viewModel: AutoconocimientoViewModel,
    onProcesamientoExitoso: () -> Unit
) {
    // Al entrar a la pantalla, llama al backend de Render y al terminar viaja a S12
    LaunchedEffect(Unit) {
        viewModel.procesarResultadosConBackend {
            onProcesamientoExitoso()
        }
    }

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

            // Cabecera Cápsula sin flecha atrás (evita cancelar el procesamiento)
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

            // Spinner suave de carga
            CircularProgressIndicator(
                modifier = Modifier.size(80.dp),
                color = MoradoPrimario,
                trackColor = Color(0xFFE8EDFB),
                strokeWidth = 6.dp
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Título
            Text(
                text = "Estamos preparando tu\norientación",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextoNegro,
                textAlign = TextAlign.Center,
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subtítulo calmado
            Text(
                text = "Puede tardar un momento. Puedes dejar la app abierta o volver más tarde: te avisaremos cuando esté lista.",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = TextoSecundario,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Banner ético protector
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
                        .height(32.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(VerdeDisclaimerBarra)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Recuerda: lo que verás es una orientación, no un diagnóstico.",
                    fontSize = 13.sp,
                    color = VerdeDisclaimerTexto,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.weight(1.2f))
        }
    }
}