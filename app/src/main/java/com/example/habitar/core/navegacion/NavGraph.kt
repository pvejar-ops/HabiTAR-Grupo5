package com.example.habitar.core.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.lifecycle.viewmodel.compose.viewModel

// Screens originales de HabiTAR
import com.example.habitar.features.QueSomos.QueSomosScreen
import com.example.habitar.features.auth.LoginScreen
import com.example.habitar.features.inicio.InicioScreen
import com.example.habitar.features.avatar.AvatarScreen
import com.example.habitar.features.menu.MenuScreen
import com.example.habitar.features.autorregulacion.emociones.EmocionesScreen
import com.example.habitar.features.autorregulacion.meditacion.MeditacionScreen
import com.example.habitar.features.autorregulacion.emergencia.EmergenciaScreen
import com.example.habitar.features.autorregulacion.sensaciones.SensacionesScreen
import com.example.habitar.features.perfil.PerfilScreen
import com.example.habitar.features.perfil.PerfilViewModel
import com.example.habitar.features.calendario.CalendarioScreen
import com.example.habitar.features.organiza.OrganizaDiaScreen
import com.example.habitar.features.caracterizacionIA.CaracterizacionIAScreen
import com.example.habitar.features.historial.HistorialScreen
import com.example.habitar.features.auth.ResetPasswordScreen
import com.example.habitar.features.editarPerfil.EditarPerfilScreen

// Pantallas del Módulo de Autoconocimiento (Grupo 5 - Vicente Fuentes)
import com.example.habitar.features.autoconocimiento.*

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
    object QueSomos : Screen("que_somos_screen")
    object CaracterizacionDeIA : Screen("caracterizacion_ia")
    object Historial : Screen("historial_screen")

    // Flujo Autoconocimiento S02 a S17
    object AutoconocimientoBienvenida : Screen("autoconocimiento_bienvenida")
    object AutoconocimientoConsentimiento : Screen("autoconocimiento_consentimiento")
    object AutoconocimientoHubSecciones : Screen("autoconocimiento_hub")
    object AutoconocimientoInstrucciones : Screen("autoconocimiento_instrucciones/{idSeccion}") {
        fun createRoute(idSeccion: Int) = "autoconocimiento_instrucciones/$idSeccion"
    }
    object AutoconocimientoPregunta : Screen("autoconocimiento_pregunta/{idSeccion}?indiceInicial={indiceInicial}&modoEdicion={modoEdicion}") {
        fun createRoute(idSeccion: Int, indiceInicial: Int = 0, esModoEdicion: Boolean = false) =
            "autoconocimiento_pregunta/$idSeccion?indiceInicial=$indiceInicial&modoEdicion=$esModoEdicion"
    }
    object AutoconocimientoPausaGuardada : Screen("autoconocimiento_pausa/{idSeccion}") {
        fun createRoute(idSeccion: Int) = "autoconocimiento_pausa/$idSeccion"
    }
    object AutoconocimientoRevision : Screen("autoconocimiento_revision/{idSeccion}") {
        fun createRoute(idSeccion: Int) = "autoconocimiento_revision/$idSeccion"
    }
    object AutoconocimientoProcesando : Screen("autoconocimiento_procesando")
    object AutoconocimientoResultados : Screen("autoconocimiento_resultados")
    object AutoconocimientoPreguntasSugeridas : Screen("autoconocimiento_preguntas_sugeridas")
    object AutoconocimientoRespuestaOrientativa : Screen("autoconocimiento_respuesta_orientativa/{textoPregunta}") {
        fun createRoute(textoPregunta: String) = "autoconocimiento_respuesta_orientativa/${java.net.URLEncoder.encode(textoPregunta, "UTF-8")}"
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
        // --- 1. Flujo de Bienvenida y Auth original ---
        composable(Screen.Inicio.route) {
            InicioScreen(onNavigateToNext = {
                navController.navigate(Screen.QueSomos.route)
            })
        }

        composable(Screen.QueSomos.route) {
            QueSomosScreen(onNavigateToAuth = {
                navController.navigate(Screen.Auth.route)
            })
        }

        composable(Screen.Auth.route) {
            LoginScreen(
                onAuthSuccess = {
                    navController.navigate(Screen.Menu.route) {
                        popUpTo(Screen.Inicio.route) { inclusive = true }
                    }
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.Avatar.route)
                }
            )
        }

        composable(Screen.Avatar.route) {
            AvatarScreen(
                onNavigateToIA = {
                    navController.navigate(Screen.CaracterizacionDeIA.route)
                },
                onAuthSuccess = {
                    navController.navigate(Screen.Menu.route) {
                        popUpTo(Screen.Inicio.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.CaracterizacionDeIA.route) {
            CaracterizacionIAScreen(
                onTodoListoSuccess = {
                    navController.navigate(Screen.Menu.route) {
                        popUpTo(Screen.Inicio.route) { inclusive = true }
                    }
                }
            )
        }

        // --- 2. Menú Principal (Conectado a Autoconocimiento) ---
        composable(Screen.Menu.route) {
            MenuScreen(
                onNavigateToEmociones = { navController.navigate(Screen.Emociones.route) },
                onNavigateToMeditacion = { navController.navigate(Screen.Meditacion.route) },
                onNavigateToEmergencia = { navController.navigate(Screen.Emergencia.route) },
                onNavigateToSensaciones = { navController.navigate(Screen.Sensaciones.route) },
                onNavigateToAutoconocimiento = {
                    val s1Comp = autoconocimientoViewModel.estaSeccionCompletada(1)
                    val s2Comp = autoconocimientoViewModel.estaSeccionCompletada(2)
                    val tieneAvance = autoconocimientoViewModel.obtenerCantidadRespondidas(1) > 0 || autoconocimientoViewModel.obtenerCantidadRespondidas(2) > 0

                    if (s1Comp && s2Comp) {
                        navController.navigate(Screen.AutoconocimientoResultados.route)
                    } else if (tieneAvance) {
                        navController.navigate(Screen.AutoconocimientoHubSecciones.route)
                    } else {
                        navController.navigate(Screen.AutoconocimientoBienvenida.route)
                    }
                }
            )
        }

        // --- 3. Calendario y Organiza tu Día ---
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
                onNavigateToTodo = { fechaSeleccionada ->
                    navController.navigate("organiza_dia_screen?fecha=$fechaSeleccionada")
                }
            )
        }

        composable(
            route = Screen.OrganizaDia.route,
            arguments = listOf(
                androidx.navigation.navArgument("fecha") {
                    type = androidx.navigation.NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val fechaArgumento = backStackEntry.arguments?.getString("fecha")
            val fechaFinal = if (!fechaArgumento.isNullOrBlank()) fechaArgumento else java.time.LocalDate.now().toString()

            OrganizaDiaScreen(
                fechaFiltro = fechaFinal,
                onNavigateToMenu = {
                    if (!navController.popBackStack()) {
                        navController.navigate(Screen.Menu.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        // --- 4. Módulos de Autorregulación y Perfil originales ---
        composable(Screen.Emociones.route) { EmocionesScreen(onNavigateToMenu = { navController.popBackStack() }) }
        composable(Screen.Meditacion.route) { MeditacionScreen(onNavigateToMenu = { navController.popBackStack() }) }
        composable(Screen.Emergencia.route) { EmergenciaScreen(onNavigateToMenu = { navController.popBackStack() }) }
        composable(Screen.Sensaciones.route) { SensacionesScreen(onNavigateToMenu = { navController.popBackStack() }) }
        composable(Screen.Perfil.route) { PerfilScreen(navController = navController, viewModel = perfilViewModel) }

        composable("historial_screen") {
            HistorialScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(
            route = "reset_password_screen/{token}",
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "habitar://reset-password?access_token={token}&refresh_token={refreshToken}&type=recovery"
                }
            )
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            ResetPasswordScreen(
                token = token,
                onResetSuccess = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("editar_perfil_screen") {
            EditarPerfilScreen(
                viewModel = perfilViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ==============================================================
        // --- 5. MÓDULO DE AUTOCONOCIMIENTO (S02 a S17) ---
        // ==============================================================
        composable(Screen.AutoconocimientoBienvenida.route) {
            BienvenidaScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateNext = { navController.navigate(Screen.AutoconocimientoConsentimiento.route) }
            )
        }

        composable(Screen.AutoconocimientoConsentimiento.route) {
            ConsentimientoScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateNext = { navController.navigate(Screen.AutoconocimientoHubSecciones.route) },
                onCancelToMenu = { navController.navigate(Screen.Menu.route) }
            )
        }

        composable(Screen.AutoconocimientoHubSecciones.route) {
            HubSeccionesScreen(
                viewModel = autoconocimientoViewModel,
                onNavigateBack = { navController.popBackStack() },
                onSelectSeccion = { idSeccion ->
                    navController.navigate(Screen.AutoconocimientoInstrucciones.createRoute(idSeccion))
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
            InstruccionesSeccionScreen(
                idSeccion = idSeccion,
                onNavigateBack = { navController.popBackStack() },
                onStartSeccion = { id ->
                    val primerIndice = autoconocimientoViewModel.obtenerPrimerIndiceSinResponder(id)
                    navController.navigate(Screen.AutoconocimientoPregunta.createRoute(id, primerIndice))
                }
            )
        }

        composable(
            route = Screen.AutoconocimientoPregunta.route,
            arguments = listOf(
                navArgument("idSeccion") { type = NavType.IntType },
                navArgument("indiceInicial") {
                    type = NavType.IntType
                    defaultValue = 0
                },
                navArgument("modoEdicion") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->
            val idSeccion = backStackEntry.arguments?.getInt("idSeccion") ?: 1
            val indiceInicial = backStackEntry.arguments?.getInt("indiceInicial") ?: 0
            val modoEdicion = backStackEntry.arguments?.getBoolean("modoEdicion") ?: false

            PreguntaScreen(
                idSeccion = idSeccion,
                viewModel = autoconocimientoViewModel,
                indiceInicial = indiceInicial,
                esModoEdicion = modoEdicion,
                onVolverClick = { navController.popBackStack() },
                onPausarClick = {
                    navController.navigate(Screen.AutoconocimientoPausaGuardada.createRoute(idSeccion))
                },
                onFinishSeccion = {
                    navController.navigate(Screen.AutoconocimientoRevision.createRoute(idSeccion))
                }
            )
        }

        composable(
            route = Screen.AutoconocimientoPausaGuardada.route,
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
                    navController.navigate(Screen.Menu.route) {
                        popUpTo(Screen.Menu.route) { inclusive = true }
                    }
                },
                onSeguirRespondiendoClick = {
                    val indice = autoconocimientoViewModel.obtenerPrimerIndiceSinResponder(idSeccion)
                    navController.navigate(Screen.AutoconocimientoPregunta.createRoute(idSeccion, indice))
                }
            )
        }

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
                onEditarPreguntaClick = { indice ->
                    navController.navigate(Screen.AutoconocimientoPregunta.createRoute(idSeccion, indice, esModoEdicion = true))
                },
                onConfirmarClick = {
                    autoconocimientoViewModel.marcarSeccionCompletada(idSeccion)
                    val s1Comp = autoconocimientoViewModel.estaSeccionCompletada(1)
                    val s2Comp = autoconocimientoViewModel.estaSeccionCompletada(2)
                    if (s1Comp && s2Comp) {
                        navController.navigate(Screen.AutoconocimientoProcesando.route)
                    } else {
                        navController.navigate(Screen.AutoconocimientoHubSecciones.route) {
                            popUpTo(Screen.AutoconocimientoHubSecciones.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(Screen.AutoconocimientoProcesando.route) {
            ProcesandoScreen(
                viewModel = autoconocimientoViewModel,
                onProcesamientoExitoso = {
                    navController.navigate(Screen.AutoconocimientoResultados.route) {
                        popUpTo(Screen.AutoconocimientoHubSecciones.route)
                    }
                }
            )
        }

        composable(Screen.AutoconocimientoResultados.route) {
            ResultadosScreen(
                viewModel = autoconocimientoViewModel,
                onVolverClick = { navController.navigate(Screen.Menu.route) },
                onInformePdfClick = { navController.navigate(Screen.AutoconocimientoProximosPasos.route) },
                onExplorarClick = { navController.navigate(Screen.AutoconocimientoPreguntasSugeridas.route) },
                onReiniciarCuestionarioClick = {
                    navController.navigate(Screen.AutoconocimientoBienvenida.route) {
                        popUpTo(Screen.Menu.route)
                    }
                }
            )
        }

        composable(Screen.AutoconocimientoPreguntasSugeridas.route) {
            PreguntasSugeridasScreen(
                onVolverClick = { navController.popBackStack() },
                onVolverAlMenuClick = {
                    navController.navigate(Screen.Menu.route) {
                        popUpTo(Screen.Menu.route) { inclusive = true }
                    }
                },
                onSeleccionarPregunta = { _, textoPregunta ->
                    navController.navigate(Screen.AutoconocimientoRespuestaOrientativa.createRoute(textoPregunta))
                }
            )
        }

        composable(
            route = Screen.AutoconocimientoRespuestaOrientativa.route,
            arguments = listOf(
                navArgument("textoPregunta") {
                    type = NavType.StringType
                    defaultValue = "¿Cómo anticipar cambios en mi semana?"
                }
            )
        ) { backStackEntry ->
            val rawPregunta = backStackEntry.arguments?.getString("textoPregunta") ?: "¿Cómo anticipar cambios en mi semana?"
            val textoPregunta = try {
                java.net.URLDecoder.decode(rawPregunta, "UTF-8")
            } catch (_: Exception) {
                rawPregunta
            }

            // Perfil psicométrico real calculado para este usuario
            val perfil = autoconocimientoViewModel.perfilResultado ?: autoconocimientoViewModel.calcularPerfilReal()

            RespuestaOrientativaScreen(
                textoPregunta = textoPregunta,
                perfil = perfil,
                onVolverClick = { navController.popBackStack() },
                onIrAMeditacionClick = { navController.navigate(Screen.Meditacion.route) },
                onVerOtrosTemasClick = { navController.popBackStack() }
            )
        }

        composable(Screen.AutoconocimientoProximosPasos.route) {
            ProximosPasosScreen(
                onVolverClick = { navController.popBackStack() },
                onVolverAlMenuClick = { navController.navigate(Screen.Menu.route) },
                onIrAHerramientasClick = { navController.navigate(Screen.Menu.route) }
            )
        }

        composable(Screen.AutoconocimientoSinConexion.route) {
            SinConexionScreen(
                onReintentarClick = { navController.navigate(Screen.AutoconocimientoProcesando.route) },
                onVolverAlMenuClick = { navController.navigate(Screen.Menu.route) }
            )
        }

        composable(Screen.AutoconocimientoApoyoInmediato.route) {
            ApoyoInmediatoScreen(
                onVolverClick = { navController.popBackStack() },
                onIrAEmergenciaClick = { navController.navigate(Screen.Emergencia.route) }
            )
        }
    }
}