package com.example.habitar.features.autoconocimiento

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
private val GrisApoyo = Color(0xFF6B7280)
private val VerdeDisclaimerFondo = Color(0xFFEDF6F2)
private val VerdeDisclaimerBarra = Color(0xFF6C9A8B)
private val VerdeDisclaimerTexto = Color(0xFF2E5A44)
private val VerdeIconoFondo = Color(0xFFE0F2E9)
private val VerdeIconoSimbolo = Color(0xFF3B7A57)

// Modelo para la respuesta dinámica de orientación
private data class ContenidoOrientativo(
    val introduccion: String,
    val estrategias: List<String>,
    val fuenteCitada: String,
    val tituloHerramienta: String,
    val subtituloHerramienta: String
)

@Composable
fun RespuestaOrientativaScreen(
    textoPregunta: String,
    onVolverClick: () -> Unit,
    onIrAMeditacionClick: () -> Unit,
    onVerOtrosTemasClick: () -> Unit
) {
    // Genera la orientación adaptada según la pregunta tocada en S13
    val orientacion = obtenerOrientacionPorPregunta(textoPregunta)

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
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    OutlinedButton(
                        onClick = onVerOtrosTemasClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(26.dp),
                        border = BorderStroke(1.5.dp, MoradoPrimario)
                    ) {
                        Text(
                            text = "VER OTROS TEMAS",
                            color = MoradoPrimario,
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

            // Cabecera Cápsula "Explora tu perfil"
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
                        text = "Explora tu perfil",
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

            // Burbuja de la Pregunta elegida por el estudiante
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.88f)
                        .clip(RoundedCornerShape(18.dp))
                        .background(MoradoHeader)
                        .padding(horizontal = 18.dp, vertical = 14.dp)
                ) {
                    Text(
                        text = textoPregunta,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 21.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Tarjeta de Respuesta del Agente RAG Dinámica
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = orientacion.introduccion,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        color = TextoNegro,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    orientacion.estrategias.forEachIndexed { index, estrategia ->
                        Text(
                            text = estrategia,
                            fontSize = 14.sp,
                            color = TextoSecundario,
                            lineHeight = 21.sp
                        )
                        if (index < orientacion.estrategias.size - 1) {
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Píldora de fuentes citadas del Corpus
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF3F4F8))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = orientacion.fuenteCitada,
                    fontSize = 12.sp,
                    color = GrisApoyo,
                    lineHeight = 17.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta de Enlace a Herramienta de Apoyo HabiTAR
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onIrAMeditacionClick() },
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
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(VerdeIconoFondo),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "❀",
                            fontSize = 20.sp,
                            color = VerdeIconoSimbolo
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = orientacion.tituloHerramienta,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextoNegro
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = orientacion.subtituloHerramienta,
                            fontSize = 13.sp,
                            color = GrisApoyo
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Ir a herramienta",
                        tint = Color(0xFF9CA3AF)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                        .height(36.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(VerdeDisclaimerBarra)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Es una orientación general, no reemplaza la opinión de un profesional de la salud.",
                    fontSize = 13.sp,
                    color = VerdeDisclaimerTexto,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// Lógica de mapeo dinámico para las 7 preguntas de S13
private fun obtenerOrientacionPorPregunta(pregunta: String): ContenidoOrientativo {
    val normalizada = pregunta.lowercase()

    return when {
        normalizada.contains("sociales") -> ContenidoOrientativo(
            introduccion = "En situaciones sociales o presentaciones universitarias, preparar el contexto reduce el desgaste por camuflaje social (CAT-Q):",
            estrategias = listOf(
                "• Define con anticipación tu tiempo de permanencia y ten una 'ruta de salida' discreta sin sentir culpa.",
                "• Acuerda con una persona de confianza una señal si necesitas tomar un descanso de la conversación.",
                "• Programa 10 a 15 minutos de silencio sensorial antes y después del evento para recargar energía."
            ),
            fuenteCitada = "Basado en: Guía de Camuflaje Social (Hull et al., CAT-Q) y dosificación de energía social en educación superior.",
            tituloHerramienta = "Ir a Meditación",
            subtituloHerramienta = "Ejercicio sugerido: Respiración consciente previa"
        )

        normalizada.contains("comunicar") -> ContenidoOrientativo(
            introduccion = "Expresar tus necesidades de autorregulación a docentes o compañeros previene la sobrecarga sin forzarte a actuar:",
            estrategias = listOf(
                "• Usa frases simples y asertivas: 'Para concentrarme mejor en la clase suelo usar auriculares con cancelación'.",
                "• Si la comunicación oral te genera tensión ante imprevistos, apóyate en notas breves o correos electrónicos.",
                "• Establece roles claros y acuerdos por escrito al inicio de los trabajos en equipo."
            ),
            fuenteCitada = "Basado en: Estrategias de comunicación asertiva y asimilación social en educación superior (Corpus HabiTAR).",
            tituloHerramienta = "Ir a Meditación",
            subtituloHerramienta = "Ejercicio sugerido: Regulación y centramiento"
        )

        normalizada.contains("semana") || normalizada.contains("anticipar") -> ContenidoOrientativo(
            introduccion = "Tener predictibilidad en tu semana disminuye la ansiedad y el impacto de los cambios imprevistos (RBQ-2A):",
            estrategias = listOf(
                "• Construye un horario visual por bloques (time-blocking), fijando descansos y traslados además de las clases.",
                "• Diseña un 'Plan B' sencillo para imprevistos comunes (como cambio de sala o clase suspendida).",
                "• Revisa tu cronograma el domingo o la noche anterior para evitar la incertidumbre matutina."
            ),
            fuenteCitada = "Basado en: Insistencia en rutinas e invariancia ambiental (Barrett et al., RBQ-2A).",
            tituloHerramienta = "Ir a Meditación",
            subtituloHerramienta = "Ejercicio sugerido: Calma ante cambios imprevistos"
        )

        normalizada.contains("pruebas") || normalizada.contains("organizarme") -> ContenidoOrientativo(
            introduccion = "En períodos de exámenes, la sobrecarga cognitiva puede dificultar el inicio de tareas. La clave es la estructura externa:",
            estrategias = listOf(
                "• Divide cada materia en micro-tareas de 25 minutos con pausas sensoriales obligatorias intermedias.",
                "• Protege tus horas de sueño y comidas como prioridades biológicas innegociables de tu rendimiento.",
                "• Utiliza listas de verificación visuales paso a paso para evitar el bloqueo por exceso de pendientes."
            ),
            fuenteCitada = "Basado en: Soporte para funciones ejecutivas y estructuración de rutinas en estudiantes universitarios.",
            tituloHerramienta = "Ir a Meditación",
            subtituloHerramienta = "Ejercicio sugerido: Enfoque y pausa anti-bloqueo"
        )

        normalizada.contains("sobrecargado") || normalizada.contains("sobrecarga") -> ContenidoOrientativo(
            introduccion = "Cuando hay sobrecarga sensorial o emocional, lo más urgente es disminuir la entrada de estímulos:",
            estrategias = listOf(
                "• Retírate a un lugar de baja estimulación: un baño poco concurrido, biblioteca o una zona abierta al aire libre.",
                "• Aplica anclaje sensorial rápido (técnica 5-4-3-2-1): identifica cosas neutras que puedas ver o tocar.",
                "• Reduce la luz, colócate tapones/auriculares o bebe agua fría con sorbos lentos y profundos."
            ),
            fuenteCitada = "Basado en: Protocolo clínico de descompresión sensorial y manejo de crisis leves (Corpus HabiTAR).",
            tituloHerramienta = "Ir a Meditación",
            subtituloHerramienta = "Ejercicio sugerido: Respiración 3 minutos"
        )

        normalizada.contains("energía") || normalizada.contains("recuperar") -> ContenidoOrientativo(
            introduccion = "El cansancio post-jornada ('social hangover') ocurre cuando has pasado horas monitoreando tu conducta:",
            estrategias = listOf(
                "• Permítete un tiempo de desconexión sin culpa: luz tenue, ropa holgada y cero exigencia de hablar.",
                "• Conéctate con tus temas de interés profundo (hiperfoco) o pasatiempos que realmente disfrutes.",
                "• Realiza movimientos repetitivos autorregulatorios (stimming) o estiramientos si tu cuerpo lo necesita."
            ),
            fuenteCitada = "Basado en: Costo cognitivo del camuflaje compensatorio (CAT-Q) y descanso neuroafirmativo.",
            tituloHerramienta = "Ir a Meditación",
            subtituloHerramienta = "Ejercicio sugerido: Soltar la tensión muscular"
        )

        else -> ContenidoOrientativo(
            introduccion = "A menudo la fatiga universitaria no es por el estudio, sino por estímulos del entorno que pasan desapercibidos (RBQ-2A):",
            estrategias = listOf(
                "• Estímulos auditivos: el murmullo de pasillos, ventiladores o eco de las aulas drena energía acumulativa.",
                "• Estímulos visuales: las luces fluorescentes o pantallas de alto brillo provocan cansancio ocular y cefaleas.",
                "• Monitorea si ciertas telas, olores de laboratorios o la temperatura ambiental están aumentando tu tensión."
            ),
            fuenteCitada = "Basado en: Procesamiento sensorial atípico e hiperreactividad en adultos (RBQ-2A).",
            tituloHerramienta = "Ir a Meditación",
            subtituloHerramienta = "Ejercicio sugerido: Escaneo corporal consciente"
        )
    }
}