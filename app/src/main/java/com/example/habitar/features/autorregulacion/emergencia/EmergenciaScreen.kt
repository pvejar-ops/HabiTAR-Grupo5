package com.example.habitar.features.autorregulacion.emergencia

import android.media.MediaPlayer
import android.net.Uri
import androidx.activity.compose.LocalActivity
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun EmergenciaScreen(
    onNavigateToMenu: () -> Unit,
    viewModel: EmergenciaViewModel = viewModel()
    ) {

    val activity = LocalActivity.current as androidx.activity.ComponentActivity
    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current

    DisposableEffect(Unit) {
        val originalMode = activity.window.attributes.softInputMode
        activity.window.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        onDispose {
            activity.window.setSoftInputMode(originalMode)
        }
    }

    val context = LocalContext.current
    val coralPrincipal = Color(0xFFF4B3A3) // El color salmón de los botones
    val coralSuave = Color(0xFFFDE8E3)    // Para el estado seleccionado (invertido)
    val grisEncabezado = Color(0xFFE0E0E0)
    val fondoTarjeta = Color(0xFFF8F8F8)

    // --- REPRODUCTOR DE AUDIO ---
    val mediaPlayer = remember { MediaPlayer() }
    LaunchedEffect(viewModel.recursoAuditivoSeleccionado) {
        val audio = viewModel.recursoAuditivoSeleccionado
        try {
            mediaPlayer.reset()
            audio?.url_contenido?.let { url ->
                if (url.isNotBlank()) {
                    mediaPlayer.setDataSource(context, Uri.parse(url))
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

    // Limpieza al destruir o salir de la pantalla
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding() //
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- BOTÓN VOLVER ---
        IconButton(
            onClick = onNavigateToMenu,
            modifier = Modifier
                .background(color = Color(0xFFF3EDF7), shape = CircleShape)
                .size(40.dp)
                .align(Alignment.Start)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color(0xFFF4B3A3))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- TÍTULO PRINCIPAL ---
        Text(
            text = "Emergencia",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(coralPrincipal, CircleShape)
                .padding(vertical = 12.dp),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Autorregúlate en una situación compleja",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )

        // --- SECCIÓN: ¿DÓNDE ESTÁS? ---
        Text(
            text = "¿Dónde estás?",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val opciones = listOf("Universidad", "Vía pública", "Casa")
            opciones.forEach { opcion ->
                val esEste = viewModel.lugarSeleccionado == opcion
                // Lógica de color invertida: Fuerte por defecto, suave al seleccionar
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(if (esEste) coralSuave else coralPrincipal, CircleShape)
                        .clickable { viewModel.lugarSeleccionado = opcion }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = opcion,
                        color = if (esEste) Color(0xFF8B4A3E) else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- ENTRADA DE TEXTO ---
        Text(
            text = "Escribe lo que sientes...",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier
                .fillMaxWidth()
                .background(grisEncabezado, CircleShape)
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )

        OutlinedTextField(
            value = viewModel.comentarioUsuario,
            onValueChange = { viewModel.comentarioUsuario = it },
            placeholder = { Text("Escribe aquí para recibir una respuesta...", textAlign = TextAlign.Center) },
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            shape = RoundedCornerShape(16.dp)
        )

        // --- BOTÓN MANDAR A IA (DINÁMICO) ---
        Button(
            onClick = { viewModel.pedirConsejoYRegistrar()
                focusManager.clearFocus()},
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF4B3A3), // Fondo rosa sólido.
                contentColor = Color.White        // Texto del botón en blanco.
            ),
            // Se bloquea si está cargando o si no ha marcado nada
            enabled = !viewModel.estaProcesandoIA &&
                    (viewModel.comentarioUsuario.isNotBlank() || viewModel.lugarSeleccionado.isNotEmpty())
        ) {
            if (viewModel.estaProcesandoIA) {  //
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
            } else {
                Text("Pedir consejo a HabiTAR", fontWeight = FontWeight.Bold)
            }
        }

        Box(
            modifier = Modifier
                .width(200.dp)
                .height(4.dp)
                .background(color = Color(0xFFF4B3A3), shape = CircleShape)
        )

        // Muestra errores en rojo en la UI si la red o Supabase fallan
        if (viewModel.mensajeError != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = viewModel.mensajeError ?: "",
                color = Color.Red,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }


        Spacer(modifier = Modifier.height(16.dp))

        // --- RESPUESTA IA (CARD) ---
        Text(
            text = "Respuesta IA",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier
                .fillMaxWidth()
                .background(grisEncabezado, CircleShape)
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )

        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = fondoTarjeta)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (viewModel.respuestaIA.isBlank()) {
                    Text(
                        text = "Aquí aparecerá el consejo de la IA basado en tus emociones.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                } else {
                    AndroidView(
                        factory = { context ->
                            android.widget.TextView(context).also { tv ->
                                io.noties.markwon.Markwon.create(context)
                                    .setMarkdown(tv, viewModel.respuestaIA)
                            }
                        },
                        update = { tv ->
                            io.noties.markwon.Markwon.create(tv.context)
                                .setMarkdown(tv, viewModel.respuestaIA)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- SECCIÓN: RECURSOS DESDE SUPABASE  ---
        RecursoEmergenciaHeader(titulo = "Recursos de Emergencia", color = coralPrincipal)

        if (viewModel.estaCargandoRecursos) {
            CircularProgressIndicator(color = coralPrincipal, modifier = Modifier.padding(16.dp))
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                viewModel.recursosAuditivos.forEach { recurso ->
                    val esEste = viewModel.recursoAuditivoSeleccionado?.id_recurso == recurso.id_recurso
                    ItemEmergenciaRecurso(
                        nombre = recurso.nombre_recurso,
                        isSelected = esEste,
                        urlMiniatura = recurso.imagen_miniatura,
                        colorTema = coralPrincipal,
                        onClick = { viewModel.seleccionarAudio(recurso) }
                    )
                }
                // 2. Renderizar los recursos Visuales en la misma sección (.mp4)
                viewModel.recursosVisuales.forEach { recurso ->
                    val esEste = viewModel.recursoVisualSeleccionado?.id_recurso == recurso.id_recurso
                    ItemEmergenciaRecurso(
                        nombre = recurso.nombre_recurso,
                        isSelected = esEste,
                        urlMiniatura = recurso.imagen_miniatura,
                        colorTema = coralPrincipal,
                        onClick = { viewModel.seleccionarVisual(recurso) }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // --- 🌟 REPRODUCTOR DE VIDEO AL FINAL DE LA PESTAÑA (SCROLLABLE) 🌟 ---
        AnimatedVisibility(visible = viewModel.recursoVisualSeleccionado != null) {
            viewModel.recursoVisualSeleccionado?.let { recurso ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Visualizando guía: ${recurso.nombre_recurso}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    val videoPlayer = remember {
                        ExoPlayer.Builder(context).build().apply {
                            repeatMode = Player.REPEAT_MODE_ALL
                        }
                    }

                    LaunchedEffect(recurso.url_contenido) {
                        recurso.url_contenido?.let { urlSegura ->
                            if (urlSegura.isNotBlank()) {
                                val mediaItem = MediaItem.fromUri(Uri.parse(urlSegura))
                                videoPlayer.setMediaItem(mediaItem)
                                videoPlayer.prepare()
                                videoPlayer.playWhenReady = true
                            }
                        }
                    }

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
                        AndroidView(
                            factory = { ctx ->
                                PlayerView(ctx).apply {
                                    player = videoPlayer
                                    useController = false
                                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
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
 * Componente del encabezado tipo cápsula para la sección de Emergencia
 */
@Composable
fun RecursoEmergenciaHeader(titulo: String, color: Color) {
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

@Composable
fun ItemEmergenciaRecurso(
    nombre: String,
    urlMiniatura: String?,
    isSelected: Boolean,
    colorTema: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(140.dp)
            .clip(RoundedCornerShape(12.dp)) // Para que el click tenga un efecto visual contenido
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(if (isSelected) Color(0xFFF3EDF7) else Color(0xFFE0E0E0), RoundedCornerShape(24.dp))
                .then(
                    if (isSelected) Modifier.border(3.dp, colorTema, RoundedCornerShape(20.dp))
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