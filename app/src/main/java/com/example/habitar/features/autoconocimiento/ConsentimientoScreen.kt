package com.example.habitar.features.autoconocimiento

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConsentimientoScreen(
    onNavigateBack: () -> Unit,
    onNavigateNext: () -> Unit,
    onCancelToMenu: () -> Unit
) {
    // Tokens de estilo
    val colorHeader = Color(0xFF5E72E4)
    val colorPrimario = Color(0xFF6C63FF)
    val colorFondo = Color(0xFFF8F9FE)
    val colorBordeCard = Color(0xFFE2E4F0)

    // Estados de las dos casillas
    var checkOrientacion by remember { mutableStateOf(false) }
    var checkAnonimo by remember { mutableStateOf(false) }

    // El botón se habilita únicamente si ambas están seleccionadas
    val botonHabilitado = checkOrientacion && checkAnonimo

    Scaffold(
        containerColor = colorFondo,
        topBar = {
            // Cabecera Cápsula flotante
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = colorHeader, shape = CircleShape)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { onNavigateBack() },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "←",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "Antes de empezar",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f).padding(end = 36.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // --- 1. TÍTULO Y DESCRIPCIÓN ---
            Text(
                text = "Tus datos, en tus manos",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1B26),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Para darte una orientación, guardamos tus respuestas de forma anónima.",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF4A4A58),
                lineHeight = 22.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(18.dp))

            // --- 2. TARJETA DE GARANTÍAS DE PRIVACIDAD (Ley 21.719) ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = colorBordeCard, shape = RoundedCornerShape(18.dp)),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "• Tus respuestas se asocian a un identificador anónimo, no a tu nombre.",
                        fontSize = 13.sp,
                        color = Color(0xFF4A4A58),
                        lineHeight = 18.sp
                    )
                    Text(
                        text = "• Nadie de tu universidad ve tus resultados, salvo que tú decidas compartirlos.",
                        fontSize = 13.sp,
                        color = Color(0xFF4A4A58),
                        lineHeight = 18.sp
                    )
                    Text(
                        text = "• Puedes borrar tus respuestas desde Perfil cuando quieras.",
                        fontSize = 13.sp,
                        color = Color(0xFF4A4A58),
                        lineHeight = 18.sp
                    )
                    Text(
                        text = "• La app no toma decisiones sobre ti.",
                        fontSize = 13.sp,
                        color = Color(0xFF4A4A58),
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- 3. CHECKBOX 1: ORIENTACIÓN Y NO DIAGNÓSTICO ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { checkOrientacion = !checkOrientacion },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = checkOrientacion,
                    onCheckedChange = { checkOrientacion = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = colorPrimario,
                        checkmarkColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Entiendo que esto es una orientación y no un diagnóstico.",
                    fontSize = 14.sp,
                    color = Color(0xFF1E1B26),
                    lineHeight = 19.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // --- 4. CHECKBOX 2: GUARDADO ANÓNIMO ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { checkAnonimo = !checkAnonimo },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = checkAnonimo,
                    onCheckedChange = { checkAnonimo = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = colorPrimario,
                        checkmarkColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Acepto que se guarden mis respuestas de forma anónima para generar mi orientación.",
                    fontSize = 14.sp,
                    color = Color(0xFF1E1B26),
                    lineHeight = 19.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // --- 5. ENLACE LEER MÁS SOBRE PRIVACIDAD ---
            Text(
                text = "Leer más sobre privacidad",
                color = colorPrimario,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .clickable {
                        // Informativo
                    }
            )

            Spacer(modifier = Modifier.weight(1f, fill = false))
            Spacer(modifier = Modifier.height(30.dp))

            // --- 6. BOTÓN ACEPTO Y CONTINÚO ---
            Button(
                onClick = onNavigateNext,
                enabled = botonHabilitado,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorPrimario,
                    contentColor = Color.White,
                    disabledContainerColor = colorPrimario.copy(alpha = 0.45f),
                    disabledContentColor = Color.White.copy(alpha = 0.85f)
                )
            ) {
                Text(
                    text = "ACEPTO Y CONTINÚO",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- 7. BOTÓN NO POR AHORA (Vuelve al Menú) ---
            TextButton(
                onClick = onCancelToMenu,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
            ) {
                Text(
                    text = "No por ahora",
                    color = colorPrimario,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}