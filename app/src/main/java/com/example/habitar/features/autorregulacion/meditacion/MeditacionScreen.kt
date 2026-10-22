package com.example.habitar.features.autorregulacion.meditacion

import android.R.attr.resizeMode
import android.media.MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.border
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import coil.compose.AsyncImage

@OptIn(UnstableApi::class)
@Composable
fun MeditacionScreen(
    onNavigateToMenu: () -> Unit,
    viewModel: MeditacionViewModel = viewModel()
) {
    val context = LocalContext.current
    var momentoSeleccionado by remember { mutableStateOf("") }
    val mentaPrincipal = Color(0xFF63D3B5)

    // --- REPRODUCTOR DE AUDIO NATIVO (MediaPlayer) ---
    val mediaPlayer = remember { MediaPlayer() }

    // Controlamos la reproducción del audio según lo que cambie en el ViewModel
    LaunchedEffect(viewModel.recursoAuditivoSeleccionado) {
        val audio = viewModel.recursoAuditivoSeleccionado
        try {
            mediaPlayer.reset()
            audio?.url_contenido?.let { urlSegura ->
                if (urlSegura.isNotBlank()) {
                    mediaPlayer.setDataSource(context, Uri.parse(urlSegura))
                    mediaPlayer.prepareAsync()
                    mediaPlayer.setOnPreparedListener { mp ->
                        mp.isLooping = true
                        mp.start()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Limpieza: Si el usuario sale de la pantalla, detenemos el audio por completo
    DisposableEffect(Unit) {
        onDispose {
            try {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                }
                mediaPlayer.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val targetColor = when (momentoSeleccionado) {
        "Mañana" -> Color(0xFFFFF9C4) // Un amarillo muy claro
        "Tarde" -> Color(0xFFFFE0B2)  // Un naranja suave
        "Noche" -> Color(0xFFC5CAE9)  // Un azul oscuro/lavanda
        else -> Color.White           // Blanco si no hay nada seleccionado
    }

    val animatedBgColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 500) // 0.5 segundos de transición
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(animatedBgColor) // Usamos el color animado
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- BOTÓN VOLVER ---
        IconButton(
            onClick = onNavigateToMenu,
            modifier = Modifier
                .background(color = Color(0xFFF3EDF7), shape = CircleShape)
                .size(48.dp)
                .align(Alignment.Start)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver",
                tint = Color(0xFF63D3B5)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- TÍTULO PRINCIPAL ---
        Text(
            text = "Meditación",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(mentaPrincipal, CircleShape)
                .padding(vertical = 12.dp),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Recursos auditivos y visuales que te permiten relajarte y meditar",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
        )

        // --- SECCIÓN: MOMENTO DEL DÍA ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val opciones = listOf("Mañana", "Tarde", "Noche")
            opciones.forEach { opcion ->
                val esEste = momentoSeleccionado == opcion

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(CircleShape)
                        .background(if (esEste) mentaPrincipal else Color(0xFFF0F0F0), CircleShape)
                        .clickable { momentoSeleccionado = opcion }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = opcion,
                        color = if (esEste) Color.White else Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- SECCIÓN: RECURSOS AUDITIVOS ---
        RecursoHeader(titulo = "Recursos auditivos", color = mentaPrincipal)

        if (viewModel.estaCargandoRecursos) {
            CircularProgressIndicator(color = mentaPrincipal, modifier = Modifier.padding(16.dp))
        } else {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                viewModel.recursosAuditivos.forEach { recurso ->
                    val esEste =
                        viewModel.recursoAuditivoSeleccionado?.id_recurso == recurso.id_recurso
                    ItemRecurso(
                        nombre = recurso.nombre_recurso,
                        isSelected = esEste,
                        urlMiniatura = recurso.imagen_miniatura,
                        onClick = { viewModel.seleccionarAudio(recurso) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- SECCIÓN: RECURSOS VISUALES ---
        RecursoHeader(titulo = "Recursos visuales", color = mentaPrincipal)

        if (viewModel.estaCargandoRecursos) {
            CircularProgressIndicator(color = mentaPrincipal, modifier = Modifier.padding(16.dp))
        } else {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                viewModel.recursosVisuales.forEach { recurso ->
                    val esEste =
                        viewModel.recursoVisualSeleccionado?.id_recurso == recurso.id_recurso
                    ItemRecurso(
                        nombre = recurso.nombre_recurso,
                        isSelected = esEste,
                        urlMiniatura = recurso.imagen_miniatura,
                        onClick = { viewModel.seleccionarVisual(recurso) }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // --- 🌟 VISUALIZACIÓN DINÁMICA DE VIDEO (EXOPLAYER) 🌟 ---
        AnimatedVisibility(visible = viewModel.recursoVisualSeleccionado != null) {
            viewModel.recursoVisualSeleccionado?.let { recurso ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Reproduciendo: ${recurso.nombre_recurso}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Inicializamos ExoPlayer de manera segura para Compose
                    val videoPlayer = remember {
                        androidx.media3.exoplayer.ExoPlayer.Builder(context).build().apply {
                            repeatMode =
                                androidx.media3.common.Player.REPEAT_MODE_ALL // Bucle infinito de relajación
                        }
                    }

                    // Cada vez que cambie el video seleccionado, actualizamos la fuente del reproductor
                    LaunchedEffect(recurso.url_contenido) {
                        recurso.url_contenido?.let { urlSegura ->
                            if (urlSegura.isNotBlank()) {
                                val mediaItem =
                                    androidx.media3.common.MediaItem.fromUri(Uri.parse(urlSegura))
                                videoPlayer.setMediaItem(mediaItem)
                                videoPlayer.prepare()
                                videoPlayer.playWhenReady = true // Inicia automáticamente
                            }
                        }
                    }

                    // Liberamos el reproductor de video si el usuario desmarca o sale de la pantalla
                    DisposableEffect(Unit) {
                        onDispose {
                            videoPlayer.release()
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        // Insertamos la vista nativa de Android que dibuja el video dentro de Jetpack Compose
                        androidx.compose.ui.viewinterop.AndroidView(
                            factory = { ctx ->
                                androidx.media3.ui.PlayerView(ctx).apply {
                                    player = videoPlayer
                                    useController =
                                        false // Oculta los botones de pausa/play para mantener el diseño limpio
                                    resizeMode=androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}
/**
 * Encabezado de sección tipo cápsula (Recursos auditivos/visuales)
 */
@Composable
fun RecursoHeader(titulo: String, color: Color) {
    Text(
        text = titulo,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .background(color, CircleShape)
            .padding(vertical = 8.dp),
        textAlign = TextAlign.Center
    )
}

/**
 * Elemento individual (Círculo con imagen y texto inferior)
 */
@Composable
fun ItemRecurso(
    nombre: String,
    urlMiniatura: String?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val mentaPrincipal = Color(0xFF63D3B5)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(105.dp)
            .clip(RoundedCornerShape(12.dp)) // Para que el click tenga un efecto visual contenido
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(if (isSelected) Color(0xFFF3EDF7) else Color(0xFFE0E0E0))
                .then(
                    if (isSelected) Modifier.border(3.dp, mentaPrincipal, RoundedCornerShape(20.dp))
                    else Modifier
                )
        ) {
            // 🌟 Si el recurso tiene una miniatura en Supabase, la renderizamos con Coil
            if (!urlMiniatura.isNullOrBlank()) {
                coil.compose.AsyncImage(
                    model = urlMiniatura,
                    contentDescription = nombre,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            }

            // Si está seleccionado, oscurecemos un poco el fondo y ponemos el check blanco encima
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                )
                Text(
                    text = "✔",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }
        }

        Text(
            text = nombre,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.Black else Color.DarkGray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 6.dp),
            lineHeight = 13.sp
        )
    }
}