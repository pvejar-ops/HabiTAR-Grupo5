package com.example.habitar.core.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.habitar.features.QueSomos.QueSomosScreen
import com.example.habitar.features.auth.LoginScreen
import com.example.habitar.features.auth.ResetPasswordScreen
import com.example.habitar.features.autoconocimiento.ApoyoInmediatoScreen
import com.example.habitar.features.autoconocimiento.AutoconocimientoViewModel
import com.example.habitar.features.autoconocimiento.BienvenidaScreen
import com.example.habitar.features.autoconocimiento.ConsentimientoScreen
import com.example.habitar.features.autoconocimiento.HubSeccionesScreen
import com.example.habitar.features.autoconocimiento.InstruccionesSeccionScreen
import com.example.habitar.features.autoconocimiento.PausaGuardadaScreen
import com.example.habitar.features.autoconocimiento.PreguntaScreen
import com.example.habitar.features.autoconocimiento.PreguntasSugeridasScreen
import com.example.habitar.features.autoconocimiento.ProcesandoScreen
import com.example.habitar.features.autoconocimiento.ProximosPasosScreen
import com.example.habitar.features.autoconocimiento.RespuestaOrientativaScreen
import com.example.habitar.features.autoconocimiento.ResultadosScreen
import com.example.habitar.features.autoconocimiento.RevisionSeccionScreen
import com.example.habitar.features.autoconocimiento.SinConexionScreen
import com.example.habitar.features.autorregulacion.emergencia.EmergenciaScreen
import com.example.habitar.features.autorregulacion.emociones.EmocionesScreen
import com.example.habitar.features.autorregulacion.meditacion.MeditacionScreen
import com.example.habitar.features.autorregulacion.sensaciones.SensacionesScreen
import com.example.habitar.features.avatar.AvatarScreen
import com.example.habitar.features.calendario.CalendarioScreen
import com.example.habitar.features.editarPerfil.EditarPerfilScreen
import com.example.habitar.features.historial.HistorialScreen
import com.example.habitar.features.inicio.InicioScreen
import com.example.habitar.features.menu.MenuScreen
import com.example.habitar.features.organiza.OrganizaDiaScreen
import com.example.habitar.features.perfil.PerfilScreen
import com.example.habitar.features.perfil.PerfilViewModel

sealed class Screen(val route: String) {
    object Inicio : Screen("inicio_screen")
    object Auth : Screen("auth_screen")
    object Avatar : Screen("avatar_screen")
    object Menu : Screen("menu_screen")
    object Emociones : Screen("emociones_screen")
    object Meditacion : Screen("meditacion_screen")
    object Emergencia : Screen("emergencia_screen")
    object Sensaciones : Screen("sensaciones_screen")
    object Perfil : Screen("perfil_screen")
    object Calendario : Screen("calendario_screen")
    object OrganizaDia : Screen("organiza_dia_screen?fecha={fecha}")
    object QueSomos : Screen(route = "que_somos_screen")
    object CaracterizacionDeIA : Screen(route = "caracterizacion_ia")
    object Historial : Screen("historial_screen")

    // Flujo Autoconocimiento (Grupo 5 - UTEM 2026)
    object AutoconocimientoBienvenida : Screen("autoconocimiento_bienvenida")
    object AutoconocimientoConsentimiento : Screen("autoconocimiento_consentimiento")
    object AutoconocimientoHub : Screen("autoconocimiento_hub")
    object AutoconocimientoInstrucciones : Screen("autoconocimiento_instrucciones/{idSeccion}") {
        fun crearRuta(idSeccion: Int) = "autoconocimiento_instrucciones/$idSeccion"
    }
    object AutoconocimientoPregunta : Screen("autoconocimiento_pregunta/{idSeccion}?indice={indice}&edicion={edicion}") {
        fun crearRuta(idSeccion: Int, indice: Int = 0, edicion: Boolean = false) =
            "autoconocimiento_pregunta/$idSeccion?indice=$indice&edicion=$edicion"
    }
    object AutoconocimientoPausa : Screen("autoconocimiento_pausa/{idSeccion}") {
        fun crearRuta(idSeccion: Int) = "autoconocimiento_pausa/$idSeccion"
    }
    object AutoconocimientoRevision : Screen("autoconocimiento_revision/{idSeccion}") {
        fun crearRuta(idSeccion: Int) = "autoconocimiento_revision/$idSeccion"
    }
    object AutoconocimientoProcesando : Screen("autoconocimiento_procesando")
    object AutoconocimientoResultados : Screen("autoconocimiento_resultados")
    object AutoconocimientoPreguntasSugeridas : Screen("autoconocimiento_preguntas_sugeridas")
    object AutoconocimientoRespuestaOrientativa : Screen("autoconocimiento_respuesta_orientativa/{preguntaTexto}") {
        fun crearRuta(preguntaTexto: String) = "autoconocimiento_respuesta_orientativa/${android.net.Uri.encode(preguntaTexto)}"
    }
    object AutoconocimientoProximosPasos : Screen("autoconocimiento_proximos_pasos")
    object AutoconocimientoSinConexion : Screen("autoconocimiento_sin_conexion")
    object AutoconocimientoApoyoInmediato : Screen("autoconocimiento_apoyo_inmediato")
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Inicio.route,
    modifier: Modifier = Modifier
) {
    val perfilViewModel: PerfilViewModel = viewModel()
    val autoconocimientoViewModel: AutoconocimientoViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Inicio.route) {
            InicioScreen(onNavigateToNext = { navController.navigate(Screen.QueSomos.route) })
        }

        composable(Screen.QueSomos.route) {
            QueSomosScreen(onNavigateToAuth = { navController.navigate(Screen.Auth.route) })
        }

        composable(Screen.Auth.route) {
            LoginScreen(
                onAuthSuccess = {
                    navController.navigate(Screen.Menu.route) { popUpTo(Screen.Inicio.route) { inclusive = true } }
                },
                onRegisterSuccess = { navController.navigate(Screen.Avatar.route) }
            )
        }

        composable(Screen.Avatar.route) {
            AvatarScreen(
                onNavigateToIA = { navController.navigate(Screen.CaracterizacionDeIA.route) },
                onAuthSuccess = { navController.navigate(Screen.Menu.route) { popUpTo(Screen.Inicio.route) { inclusive = true } } }
            )
        }

        composable(Screen.CaracterizacionDeIA.route) {}

        composable(Screen.Menu.route) {
            MenuScreen(
                onNavigateToEmociones = { navController.navigate(Screen.Emociones.route) },
                onNavigateToMeditacion = { navController.navigate(Screen.Meditacion.route) },
                onNavigateToEmergencia = { navController.navigate(Screen.Emergencia.route) },
                onNavigateToSensaciones = { navController.navigate(Screen.Sensaciones.route) },
                onNavigateToAutoconocimiento = { navController.navigate(Screen.AutoconocimientoBienvenida.route) }
            )
        }

        composable(Screen.Calendario.route) {
            CalendarioScreen(
                onNavigateToMenu = {
                    val popped = navController.popBackStack()
                    if (!popped || navController.currentDestination?.route == Screen.Inicio.route) {
                        navController.navigate(Screen.Menu.route) {
                            popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                },
                onNavigateToTodo = { fecha -> navController.navigate("organiza_dia_screen?fecha=$fecha") }
            )
        }

        composable(
            route = Screen.OrganizaDia.route,
            arguments = listOf(navArgument("fecha") { type = NavType.StringType; nullable = true; defaultValue = null })
        ) { backStackEntry ->
            val fechaArgumento = backStackEntry.arguments?.getString("fecha")
            val fechaFinal = if (!fechaArgumento.isNullOrBlank()) fechaArgumento else java.time.LocalDate.now().toString()
            OrganizaDiaScreen(
                fechaFiltro = fechaFinal,
                onNavigateToMenu = {
                    if (!navController.popBackStack()) {
                        navController.navigate(Screen.Menu.route) {
                            popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        composable(Screen.Emociones.route) { EmocionesScreen(onNavigateToMenu = { navController.popBackStack() }) }
        composable(Screen.Meditacion.route) { MeditacionScreen(onNavigateToMenu = { navController.popBackStack() }) }
        composable(Screen.Emergencia.route) { EmergenciaScreen(onNavigateToMenu = { navController.popBackStack() }) }
        composable(Screen.Sensaciones.route) { SensacionesScreen(onNavigateToMenu = { navController.popBackStack() }) }
        composable(Screen.Perfil.route) { PerfilScreen(navController = navController, viewModel = perfilViewModel) }
        composable("historial_screen") { HistorialScreen(onNavigateBack = { navController.popBackStack() }) }

        composable(
            route = "reset_password_screen/{token}",
            deepLinks = listOf(navDeepLink { uriPattern = "habitar://reset-password?access_token={token}&refresh_token={refreshToken}&type=recovery" })
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            ResetPasswordScreen(
                token = token,
                onResetSuccess = { navController.navigate(Screen.Auth.route) { popUpTo(0) { inclusive = true } } },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("editar_perfil_screen") {
            EditarPerfilScreen(viewModel = perfilViewModel, onNavigateBack = { navController.popBackStack() })
        }

        // ======================= AUTOCONOCIMIENTO =======================

        composable(Screen.AutoconocimientoBienvenida.route) {
            BienvenidaScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateNext = { navController.navigate(Screen.AutoconocimientoConsentimiento.route) }
            )
        }

        composable(Screen.AutoconocimientoConsentimiento.route) {
            ConsentimientoScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateNext = { navController.navigate(Screen.AutoconocimientoHub.route) },
                onCancelToMenu = {
                    navController.navigate(Screen.Menu.route) { popUpTo(Screen.Menu.route) { inclusive = true } }
                }
            )
        }

        composable(Screen.AutoconocimientoHub.route) {
            HubSeccionesScreen(
                viewModel = autoconocimientoViewModel,
                onNavigateBack = {
                    navController.navigate(Screen.Menu.route) { popUpTo(Screen.Menu.route) { inclusive = true } }
                },
                onSelectSeccion = { idSeccion ->
                    navController.navigate(Screen.AutoconocimientoInstrucciones.crearRuta(idSeccion))
                },
                onNavigateToApoyoInmediato = {
                    navController.navigate(Screen.AutoconocimientoApoyoInmediato.route)
                },
                onVerOrientacion = {
                    navController.navigate(Screen.AutoconocimientoProcesando.route)
                }
            )
        }

        composable(
            route = Screen.AutoconocimientoInstrucciones.route,
            arguments = listOf(navArgument("idSeccion") { type = NavType.IntType })
        ) { backStackEntry ->
            val idSeccion = backStackEntry.arguments?.getInt("idSeccion") ?: 1
            val primerIndicePendiente = autoconocimientoViewModel.obtenerPrimerIndiceSinResponder(idSeccion)

            InstruccionesSeccionScreen(
                idSeccion = idSeccion,
                onNavigateBack = { navController.popBackStack() },
                onStartSeccion = {
                    navController.navigate(Screen.AutoconocimientoPregunta.crearRuta(idSeccion, primerIndicePendiente, edicion = false))
                }
            )
        }

        // S06 / S08: Preguntas (con soporte de modo edición)
        composable(
            route = Screen.AutoconocimientoPregunta.route,
            arguments = listOf(
                navArgument("idSeccion") { type = NavType.IntType },
                navArgument("indice") { type = NavType.IntType; defaultValue = 0 },
                navArgument("edicion") { type = NavType.BoolType; defaultValue = false }
            )
        ) { backStackEntry ->
            val idSeccion = backStackEntry.arguments?.getInt("idSeccion") ?: 1
            val indice = backStackEntry.arguments?.getInt("indice") ?: 0
            val edicion = backStackEntry.arguments?.getBoolean("edicion") ?: false

            PreguntaScreen(
                idSeccion = idSeccion,
                viewModel = autoconocimientoViewModel,
                indiceInicial = indice,
                esModoEdicion = edicion,
                onVolverClick = { navController.popBackStack() },
                onPausarClick = {
                    navController.navigate(Screen.AutoconocimientoPausa.crearRuta(idSeccion))
                },
                onFinishSeccion = {
                    navController.navigate(Screen.AutoconocimientoRevision.crearRuta(idSeccion)) {
                        popUpTo(Screen.AutoconocimientoRevision.crearRuta(idSeccion)) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.AutoconocimientoPausa.route,
            arguments = listOf(navArgument("idSeccion") { type = NavType.IntType })
        ) { backStackEntry ->
            val idSeccion = backStackEntry.arguments?.getInt("idSeccion") ?: 1
            val respondidas = autoconocimientoViewModel.obtenerCantidadRespondidas(idSeccion)
            val total = autoconocimientoViewModel.obtenerTotalPreguntas(idSeccion)

            PausaGuardadaScreen(
                idSeccion = idSeccion,
                respondidas = respondidas,
                totalPreguntas = total,
                onVolverMenuClick = {
                    navController.navigate(Screen.Menu.route) { popUpTo(Screen.Menu.route) { inclusive = true } }
                },
                onSeguirRespondiendoClick = { navController.popBackStack() }
            )
        }

        // S10: Revisión antes de confirmar (PASA edicion = true al editar)
        composable(
            route = Screen.AutoconocimientoRevision.route,
            arguments = listOf(navArgument("idSeccion") { type = NavType.IntType })
        ) { backStackEntry ->
            val idSeccion = backStackEntry.arguments?.getInt("idSeccion") ?: 1
            val preguntas = autoconocimientoViewModel.obtenerPreguntas(idSeccion)
            val respuestas = autoconocimientoViewModel.obtenerMapaRespuestas(idSeccion)

            RevisionSeccionScreen(
                idSeccion = idSeccion,
                preguntas = preguntas,
                respuestas = respuestas,
                onVolverClick = { navController.popBackStack() },
                onEditarPreguntaClick = { indicePregunta ->
                    navController.navigate(Screen.AutoconocimientoPregunta.crearRuta(idSeccion, indicePregunta, edicion = true))
                },
                onConfirmarClick = {
                    autoconocimientoViewModel.marcarSeccionCompletada(idSeccion)

                    if (autoconocimientoViewModel.todasLasSeccionesCompletadas()) {
                        navController.navigate(Screen.AutoconocimientoProcesando.route)
                    } else {
                        navController.navigate(Screen.AutoconocimientoHub.route) {
                            popUpTo(Screen.AutoconocimientoHub.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        // S11: Procesando
        composable(Screen.AutoconocimientoProcesando.route) {
            ProcesandoScreen(
                viewModel = autoconocimientoViewModel,
                onProcesamientoExitoso = {
                    navController.navigate(Screen.AutoconocimientoResultados.route) {
                        popUpTo(Screen.AutoconocimientoHub.route) { inclusive = false }
                    }
                }
            )
        }

        // S12: Resultados
        composable(Screen.AutoconocimientoResultados.route) {
            ResultadosScreen(
                viewModel = autoconocimientoViewModel,
                onVolverClick = {
                    navController.navigate(Screen.AutoconocimientoHub.route) {
                        popUpTo(Screen.AutoconocimientoHub.route) { inclusive = true }
                    }
                },
                onInformePdfClick = {
                    navController.navigate(Screen.AutoconocimientoProximosPasos.route)
                },
                onExplorarClick = {
                    navController.navigate(Screen.AutoconocimientoPreguntasSugeridas.route)
                }
            )
        }

        // S13: Preguntas sugeridas
        composable(Screen.AutoconocimientoPreguntasSugeridas.route) {
            PreguntasSugeridasScreen(
                onVolverClick = { navController.popBackStack() },
                onSeleccionarPregunta = { _, textoPregunta ->
                    navController.navigate(Screen.AutoconocimientoRespuestaOrientativa.crearRuta(textoPregunta))
                }
            )
        }

        // S14: Respuesta orientativa
        composable(
            route = Screen.AutoconocimientoRespuestaOrientativa.route,
            arguments = listOf(navArgument("preguntaTexto") { type = NavType.StringType })
        ) { backStackEntry ->
            val textoPregunta = android.net.Uri.decode(backStackEntry.arguments?.getString("preguntaTexto") ?: "")

            RespuestaOrientativaScreen(
                textoPregunta = textoPregunta,
                onVolverClick = { navController.popBackStack() },
                onIrAMeditacionClick = { navController.navigate(Screen.Meditacion.route) },
                onVerOtrosTemasClick = { navController.popBackStack() }
            )
        }

        // S15: Próximos pasos y apoyo
        composable(Screen.AutoconocimientoProximosPasos.route) {
            ProximosPasosScreen(
                onVolverClick = { navController.popBackStack() },
                onVolverAlMenuClick = {
                    navController.navigate(Screen.Menu.route) { popUpTo(Screen.Menu.route) { inclusive = true } }
                },
                onIrAHerramientasClick = {
                    navController.navigate(Screen.Menu.route) { popUpTo(Screen.Menu.route) { inclusive = true } }
                }
            )
        }

        // S16: Sin conexión
        composable(Screen.AutoconocimientoSinConexion.route) {
            SinConexionScreen(
                onReintentarClick = {
                    navController.navigate(Screen.AutoconocimientoProcesando.route) {
                        popUpTo(Screen.AutoconocimientoSinConexion.route) { inclusive = true }
                    }
                },
                onVolverAlMenuClick = {
                    navController.navigate(Screen.Menu.route) { popUpTo(Screen.Menu.route) { inclusive = true } }
                }
            )
        }

        // S17: Apoyo inmediato
        composable(Screen.AutoconocimientoApoyoInmediato.route) {
            ApoyoInmediatoScreen(
                onVolverClick = { navController.popBackStack() },
                onIrAEmergenciaClick = { navController.navigate(Screen.Emergencia.route) }
            )
        }
    }
}