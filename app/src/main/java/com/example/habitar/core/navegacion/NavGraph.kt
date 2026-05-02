package com.example.habitar.core.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
    object OrganizaDia : Screen("organiza_dia_screen")
}

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Inicio.route,
        modifier = modifier // IMPORTANTE: Aplicar el modifier aquí para evitar solapamientos
    ) {
        // 1. Flujo de Bienvenida y Auth
        composable(Screen.Inicio.route) {
            InicioScreen(onNavigateToNext = {
                navController.navigate(Screen.Auth.route)
            })
        }

        composable(Screen.Auth.route) {
            LoginScreen(onAuthSuccess = {
                navController.navigate(Screen.Avatar.route)
            })
        }

        composable(Screen.Avatar.route) {
            AvatarScreen(onNavigateToMenu = {
                navController.navigate(Screen.Menu.route)
            })
        }

        // 2. Menú Principal
        composable(Screen.Menu.route) {
            MenuScreen(
                onNavigateToEmociones = { navController.navigate(Screen.Emociones.route) },
                onNavigateToMeditacion = { navController.navigate(Screen.Meditacion.route) },
                onNavigateToEmergencia = { navController.navigate(Screen.Emergencia.route) },
                onNavigateToSensaciones = { navController.navigate(Screen.Sensaciones.route) }
            )
        }

        // 3. Calendario y Organiza tu Día (Navegación jerárquica)
        composable(Screen.Calendario.route) {
            CalendarioScreen(
                onNavigateToOrganizaDia = {
                    navController.navigate(Screen.OrganizaDia.route)
                }
            )
        }

        composable(Screen.OrganizaDia.route) {
            OrganizaDiaScreen(onNavigateBack = {
                navController.popBackStack()
            })
        }

        // 4. Módulos de Autorregulación y Perfil
        composable(Screen.Emociones.route) { EmocionesScreen() }
        composable(Screen.Meditacion.route) { MeditacionScreen() }
        composable(Screen.Emergencia.route) { EmergenciaScreen() }
        composable(Screen.Sensaciones.route) { SensacionesScreen() }
        composable(Screen.Perfil.route) { PerfilScreen() }
    }
}