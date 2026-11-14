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

private data class ContenidoOrientativo(
    val badgeNivel: String,
    val badgeColorHex: Long,
    val introduccion: String,
    val estrategias: List<String>,
    val fuenteCitada: String,
    val tituloHerramienta: String,
    val subtituloHerramienta: String
)

@Composable
fun RespuestaOrientativaScreen(
    textoPregunta: String,
    perfil: PerfilOrientacion,
    onVolverClick: () -> Unit,
    onIrAMeditacionClick: () -> Unit,
    onVerOtrosTemasClick: () -> Unit
) {
    // Cruce RAG: Pregunta seleccionada + Perfil psicométrico real del usuario
    val orientacion = generarOrientacionPersonalizada(textoPregunta, perfil)

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

            // Burbuja de la Pregunta
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

            // Tarjeta de Respuesta Personalizada
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    // Badge con el nivel de tu perfil detectado para este tema
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Orientación según tus respuestas",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextoNegro
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(orientacion.badgeColorHex))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = orientacion.badgeNivel,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = orientacion.introduccion,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = TextoNegro,
                        lineHeight = 21.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    orientacion.estrategias.forEachIndexed { index, estrategia ->
                        Text(
                            text = estrategia,
                            fontSize = 13.sp,
                            color = TextoSecundario,
                            lineHeight = 20.sp
                        )
                        if (index < orientacion.estrategias.size - 1) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Píldora de fuentes citadas
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

            // Enlace a herramienta HabiTAR
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

            // Banner ético
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

// Generador de orientación que cruza pregunta + resultados psicométricos del usuario
private fun generarOrientacionPersonalizada(
    pregunta: String,
    perfil: PerfilOrientacion
): ContenidoOrientativo {
    val q = pregunta.lowercase()

    // 1. Preguntas sobre Rutinas y Cambios
    if (q.contains("semana") || q.contains("anticipar") || q.contains("rutinas") || q.contains("pruebas") || q.contains("organizarme")) {
        val nivelRutina = perfil.preferenciaPredecible.nivelTexto
        val colorHex = perfil.preferenciaPredecible.nivelColorHex

        return when (nivelRutina) {
            "Alta" -> ContenidoOrientativo(
                badgeNivel = "Previsión: Alta",
                badgeColorHex = colorHex,
                introduccion = "Dado que en tu perfil la 'Preferencia por lo predecible' resultó ALTA, los cambios inesperados te demandan un costo energético considerable. Es prioritario estructurar apoyos externos:",
                estrategias = listOf(
                    "• Diseña una rutina visual fija de lunes a viernes: bloquea horarios intocables para descanso y traslados.",
                    "• Elabora un 'Plan de Contingencia' explícito para imprevistos comunes (ej: si cambian de sala o suspenden una clase).",
                    "• Comunica con anticipación a tus grupos de trabajo que prefieres conocer las fechas y entregas con margen de tiempo."
                ),
                fuenteCitada = "Basado en: RBQ-2A (Barrett et al.) - Insistencia en rutinas e invarianza ambiental.",
                tituloHerramienta = "Ir a Meditación",
                subtituloHerramienta = "Ejercicio: Calma ante cambios imprevistos"
            )
            "Moderada" -> ContenidoOrientativo(
                badgeNivel = "Previsión: Moderada",
                badgeColorHex = colorHex,
                introduccion = "Tu perfil muestra una necesidad MODERADA de estructura. Te adaptas a ciertos cambios, pero las semanas muy desordenadas terminan por agotarte:",
                estrategias = listOf(
                    "• Mantén al menos 2 hábitos fijos innegociables en tu día (ej: tu desayuno o tu hora de dormir).",
                    "• Usa recordatorios o alarmas en el móvil para avisarte con 15 minutos de anticipación al cambiar de actividad.",
                    "• Evita sobrecargarte de compromisos espontáneos en días que ya tienen alta carga académica."
                ),
                fuenteCitada = "Basado en: RBQ-2A - Equilibrio entre flexibilidad y soporte de rutinas.",
                tituloHerramienta = "Ir a Meditación",
                subtituloHerramienta = "Ejercicio: Respiración para pausas activas"
            )
            else -> ContenidoOrientativo(
                badgeNivel = "Previsión: Baja",
                badgeColorHex = colorHex,
                introduccion = "En tu perfil la 'Preferencia por lo predecible' resultó BAJA. Tienes facilidad para adaptarte a cambios espontáneos sin experimentar angustia:",
                estrategias = listOf(
                    "• Aprovecha tu flexibilidad natural para apoyar la coordinación en grupos de estudio.",
                    "• Mantén un registro mínimo de fechas de pruebas para que la improvisación no descuide plazos clave.",
                    "• Si un compañero se bloquea ante un cambio de sala, ayúdale con calma a reubicarse."
                ),
                fuenteCitada = "Basado en: RBQ-2A - Flexibilidad adaptativa en entornos universitarios.",
                tituloHerramienta = "Ir a Meditación",
                subtituloHerramienta = "Ejercicio: Concentración y enfoque"
            )
        }
    }

    // 2. Preguntas sobre Social / Comunicación
    if (q.contains("social") || q.contains("comunicar") || q.contains("sociales")) {
        val nivelSocial = perfil.esfuerzoSocial.nivelTexto
        val colorHex = perfil.esfuerzoSocial.nivelColorHex

        return when (nivelSocial) {
            "Alta" -> ContenidoOrientativo(
                badgeNivel = "Esfuerzo Social: Alto",
                badgeColorHex = colorHex,
                introduccion = "Tu perfil indica un nivel ALTO de esfuerzo y camuflaje social (CAT-Q). Estar constantemente monitoreando tus gestos y palabras te produce un gran desgaste mental silencioso:",
                estrategias = listOf(
                    "• Planifica un 'tiempo límite' en actividades sociales universitarias y ten lista una retirada sin culpas.",
                    "• Permítete momentos a solas entre clases para desconectar la 'máscara' y no forzar expresiones faciales.",
                    "• Prefiere la comunicación por escrito (WhatsApp, correo) para tareas universitarias cuando te sientas cansado/a."
                ),
                fuenteCitada = "Basado en: CAT-Q (Hull et al., 2019) - Camuflaje y compensación en adultos.",
                tituloHerramienta = "Ir a Meditación",
                subtituloHerramienta = "Ejercicio: Descompresión social y relajación"
            )
            "Moderada" -> ContenidoOrientativo(
                badgeNivel = "Esfuerzo Social: Moderado",
                badgeColorHex = colorHex,
                introduccion = "Tu nivel de adaptación social es MODERADO. Te desenvuelves bien en entornos conocidos, pero grupos grandes o personas desconocidas aumentan tu esfuerzo:",
                estrategias = listOf(
                    "• Identifica qué tipo de interacciones te suman energía y cuáles te drenan para dosificarlas en la semana.",
                    "• Apóyate en amigos de confianza para compartir grupos de trabajo y sentirte en un entorno seguro.",
                    "• Expresa tus ideas a tu propio ritmo sin exigirte responder de inmediato."
                ),
                fuenteCitada = "Basado en: CAT-Q - Asimilación y modulación de energía social.",
                tituloHerramienta = "Ir a Meditación",
                subtituloHerramienta = "Ejercicio: Centramiento antes de interactuar"
            )
            else -> ContenidoOrientativo(
                badgeNivel = "Esfuerzo Social: Bajo",
                badgeColorHex = colorHex,
                introduccion = "En tus respuestas el esfuerzo por adaptarte socialmente resultó BAJO. Sueles actuar con bastante naturalidad y espontaneidad frente a otros:",
                estrategias = listOf(
                    "• Mantén tu autenticidad en los espacios universitarios, es una gran fortaleza personal.",
                    "• Recuerda que compañeros de equipo pueden requerir más tiempo o calma para participar.",
                    "• Canaliza tu tranquilidad social en liderar presentaciones o coordinaciones académicas."
                ),
                fuenteCitada = "Basado en: CAT-Q - Espontaneidad y comunicación directa.",
                tituloHerramienta = "Ir a Meditación",
                subtituloHerramienta = "Ejercicio: Respiración para mantener la calma"
            )
        }
    }

    // 3. Preguntas sobre Energía, Sobrecarga y Estímulos
    val nivelSensorial = perfil.movimientosRepetidos.nivelTexto
    val colorHexSensorial = perfil.movimientosRepetidos.nivelColorHex

    return when (nivelSensorial) {
        "Alta" -> ContenidoOrientativo(
            badgeNivel = "Reactividad / Descarga: Alta",
            badgeColorHex = colorHexSensorial,
            introduccion = "Tu perfil muestra ALTA presencia de conductas autorregulatorias y sensibilidad. Tu sistema nervioso registra los estímulos intensamente y necesita descargas periódicas:",
            estrategias = listOf(
                "• No reprimas tus movimientos repetitivos de autorregulación (stimming, mecerte, tronarte los dedos) cuando estés a solas o concentrado/a.",
                "• Utiliza protectores o auriculares con cancelación de ruido en patios, pasillos o el metro.",
                "• Ante sobrecarga inminente, sal de la sala 5 minutos a lavarte las manos con agua fría o buscar penumbra."
            ),
            fuenteCitada = "Basado en: RBQ-2A y Guía de Sobrecarga Sensorial HabiTAR (2026).",
            tituloHerramienta = "Ir a Meditación",
            subtituloHerramienta = "Ejercicio: Respiración 3 minutos ante sobrecarga"
        )
        "Moderada" -> ContenidoOrientativo(
            badgeNivel = "Reactividad / Descarga: Moderada",
            badgeColorHex = colorHexSensorial,
            introduccion = "Tu perfil presenta una reactividad MODERADA. En jornadas habituales logras regularte, pero días largos con ruido y luces intensas disparan tu fatiga:",
            estrategias = listOf(
                "• Programa descansos sensoriales cortos entre bloques de estudio (10 minutos con ojos cerrados).",
                "• Regula el brillo de las pantallas y busca asientos lejos de fuentes de ruido continuo (aires acondicionados, proyectores).",
                "• Realiza estiramientos físicos para liberar la tensión muscular acumulada."
            ),
            fuenteCitada = "Basado en: RBQ-2A - Regulación sensorial en adultos universitarios.",
            tituloHerramienta = "Ir a Meditación",
            subtituloHerramienta = "Ejercicio: Escaneo corporal relajante"
        )
        else -> ContenidoOrientativo(
            badgeNivel = "Reactividad / Descarga: Baja",
            badgeColorHex = colorHexSensorial,
            introduccion = "Tu perfil muestra BAJA reactividad sensorial o necesidad de movimientos repetitivos. Toleras bien el ajetreo típico del campus:",
            estrategias = listOf(
                "• Tu tolerancia ambiental te permite estudiar en diversos entornos de la universidad.",
                "• Mantén una buena hidratación y pausas de hidratación para cuidar tu energía física.",
                "• Si un día extraordinario te sientes fatigado/a, simplemente haz una pausa tranquila."
            ),
            fuenteCitada = "Basado en: RBQ-2A - Procesamiento sensorial neurotípico / basal.",
            tituloHerramienta = "Ir a Meditación",
            subtituloHerramienta = "Ejercicio: Pausa de recarga rápida"
        )
    }
}