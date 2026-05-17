package com.example.habitar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.habitar.core.navegacion.BottomBarItem
import com.example.habitar.core.navegacion.AppNavGraph
import com.example.habitar.core.navegacion.Screen
import com.example.habitar.core.ui.theme.HabiTARTheme
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabiTARTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // Definimos todas las pantallas donde la barra DEBE seguir visible
            val pantallasPrincipales = listOf(
                Screen.Menu.route,
                Screen.Calendario.route,
                Screen.Perfil.route,
                Screen.Emociones.route,
                Screen.Sensaciones.route,
                Screen.Meditacion.route,
                Screen.Emergencia.route
            )

            if (currentRoute in pantallasPrincipales) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    tonalElevation = 8.dp
                ) {
                    val items = listOf(
                        BottomBarItem.Menu,
                        BottomBarItem.Calendario,
                        BottomBarItem.Perfil
                    )

                    items.forEach { item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title
                                )
                            },
                            label = { Text(text = item.title) },
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    // Corregido: Volvemos a colocar la limpieza de pila
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        AppNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}