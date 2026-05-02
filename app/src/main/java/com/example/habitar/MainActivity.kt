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

    // Observamos la ruta actual para saber qué ítem marcar como seleccionado
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // Definimos las rutas donde queremos que aparezca la barra
            val pantallasPrincipales = listOf(
                Screen.Menu.route,
                Screen.Calendario.route,
                Screen.Perfil.route
            )

            // Solo se muestra si la ruta actual está en la lista de principales
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
                                    // Evita que se acumulen pantallas en el historial al navegar
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Evita múltiples copias de la misma pantalla
                                    launchSingleTop = true
                                    // Restaura el estado (scroll, etc.) si ya existía
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
        // El NavGraph recibe el padding para que el contenido no quede debajo de la barra
        AppNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}