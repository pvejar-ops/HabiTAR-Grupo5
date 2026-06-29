package com.example.habitar.core.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.example.habitar.features.QueSomos.QueSomosScreen
//Screens de las carpetas features
import com.example.habitar.features.auth.LoginScreen
import com.example.habitar.features.inicio.InicioScreen
import com.example.habitar.features.avatar.AvatarScreen
import com.example.habitar.features.menu.MenuScreen
import com.example.habitar.features.autorregulacion.emociones.EmocionesScreen
import com.example.habitar.features.autorregulacion.meditacion.MeditacionScreen
import com.example.habitar.features.autorregulacion.emergencia.EmergenciaScreen
import com.example.habitar.features.autorregulacion.sensaciones.SensacionesScreen
import com.example.habitar.features.perfil.PerfilScreen
import com.example.habitar.features.calendario.CalendarioScreen
import com.example.habitar.features.organiza.OrganizaDiaScreen
import com.example.habitar.features.caracterizacionIA.CaracterizacionIAScreen
import com.example.habitar.features.historial.HistorialScreen
import com.example.habitar.features.auth.ResetPasswordScreen
import com.example.habitar.features.editarPerfil.EditarPerfilScreen
import androidx.lifecycle.viewmodel.compose.viewModel
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
}

@Composable
fun AppNavGraph(
    navController: NavHostController, // Este debe ser el mismo de la MainActivity
    startDestination: String = Screen.Inicio.route, // <- NUEVO!!!
    modifier: Modifier = Modifier
) {
    val perfilViewModel: PerfilViewModel = viewModel() // NUEVO!!

    NavHost(
        navController = navController,
        //startDestination = Screen.Inicio.route,
        startDestination = startDestination, // <- NUEVO!!!
        modifier = modifier
    ) {
        // 1. Flujo de Bienvenida y Auth
        composable(Screen.Inicio.route) {
            InicioScreen(onNavigateToNext = {
                navController.navigate(Screen.QueSomos.route)
            })
        }

        composable(Screen.QueSomos.route) {
            // Llamamos a la función que creaste en el archivo QueSomos.kt
            QueSomosScreen(onNavigateToAuth = {
                    navController.navigate(Screen.Auth.route)
                }
            )
        }

        composable(Screen.Auth.route) {
            LoginScreen(
                onAuthSuccess = {
                    // Si el usuario ya tiene cuenta, va al Menú y borra el historial de login
                    navController.navigate(Screen.Menu.route) {
                        popUpTo(Screen.Inicio.route) { inclusive = true }
                    }
                },
                onRegisterSuccess = {
                    // Si el usuario se está registrando, va al Avatar
                    navController.navigate(Screen.Avatar.route)
                    //Para testeo con usuario evitamos pestaña de avatar y pasa directo al cuentanos de ti
                    //navController.navigate(Screen.CaracterizacionDeIA.route)
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

// Sección de Caracterización de IA
        composable(Screen.CaracterizacionDeIA.route) {
            CaracterizacionIAScreen(
                onTodoListoSuccess = {
                    navController.navigate(Screen.Menu.route) {
                        popUpTo(Screen.Inicio.route) { inclusive = true }
                    }
                }
            )
        }

        // 2. Menú Principal
        composable(Screen.Menu.route) {
            MenuScreen(
                onNavigateToEmociones = { navController.navigate(Screen.Emociones.route)},
                onNavigateToMeditacion = { navController.navigate(Screen.Meditacion.route) },
                onNavigateToEmergencia = { navController.navigate(Screen.Emergencia.route) },
                onNavigateToSensaciones = { navController.navigate(Screen.Sensaciones.route) }
            )
        }

        // 3. Calendario y Organiza tu Día (Navegación jerárquica)
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
            // 🌟 Le avisamos a Jetpack Navigation que este destino puede recibir un String "fecha"
            arguments = listOf(
                androidx.navigation.navArgument("fecha") {
                    type = androidx.navigation.NavType.StringType
                    nullable = true
                    defaultValue = null // Si es null, la pantalla sabrá que debe usar "hoy"
                }
            )
        ) { backStackEntry ->
            // Extraemos la fecha que viene viajando en la ruta (si es que existe)
            val fechaArgumento = backStackEntry.arguments?.getString("fecha")

            // Importamos java.time.LocalDate para obtener el día actual del dispositivo como respaldo
            val fechaFinal =
                if (!fechaArgumento.isNullOrBlank()) fechaArgumento else java.time.LocalDate.now()
                    .toString()

            OrganizaDiaScreen(
                fechaFiltro = fechaFinal,
                onNavigateToMenu = {
                    // Si hay pantallas en el stack (ej. vino del Calendario), retrocede.
                    // Si no, asegura ir al Menú limpiando el historial.
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

        // 4. Módulos de Autorregulación y Perfil
        composable(Screen.Emociones.route) { EmocionesScreen(onNavigateToMenu = { navController.popBackStack() }) }
        composable(Screen.Meditacion.route) { MeditacionScreen(onNavigateToMenu = { navController.popBackStack() }) }
        composable(Screen.Emergencia.route) { EmergenciaScreen(onNavigateToMenu = { navController.popBackStack() }) }
        composable(Screen.Sensaciones.route) { SensacionesScreen(onNavigateToMenu = { navController.popBackStack() }) }
        composable(Screen.Perfil.route) { PerfilScreen(navController = navController, viewModel = perfilViewModel) }

        // Historial
        composable("historial_screen") {
            HistorialScreen(
                onNavigateBack = { navController.popBackStack() }
            )
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
    }
}

@Composable
fun CaracterizacionIAScreen(
    onNavigateBack: () -> Boolean,
    onNextStep: () -> Unit,
    onCaracIA: () -> Unit
) {
    TODO("Not yet implemented")
}