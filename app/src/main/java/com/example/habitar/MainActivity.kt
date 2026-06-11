package com.example.habitar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.work.PeriodicWorkRequestBuilder
import java.util.Calendar
import androidx.work.*
import com.example.habitar.core.notificaciones.NotificacionWorker
import java.util.concurrent.TimeUnit
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import android.os.Build

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            programarNotificacionDiaria()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HabiTARTheme(darkTheme = false) {
                MainScreen()
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            // Android < 13 no necesita permiso, programa directo
            programarNotificacionDiaria()
        }
    }
    private fun programarNotificacionDiaria() {
        val ahora = Calendar.getInstance()

        // --- 9 AM ---
        val objetivo9AM = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            if (before(ahora)) add(Calendar.DAY_OF_MONTH, 1)
        }
        val delay9AM = objetivo9AM.timeInMillis - ahora.timeInMillis

        val request9AM = PeriodicWorkRequestBuilder<NotificacionWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(delay9AM, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "notificacion_9am",
            ExistingPeriodicWorkPolicy.UPDATE,
            request9AM
        )

        // --- 9 PM ---
        val objetivo9PM = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 21)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            if (before(ahora)) add(Calendar.DAY_OF_MONTH, 1)
        }
        val delay9PM = objetivo9PM.timeInMillis - ahora.timeInMillis

        val request9PM = PeriodicWorkRequestBuilder<NotificacionWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(delay9PM, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "notificacion_9pm",
            ExistingPeriodicWorkPolicy.UPDATE,
            request9PM
        )
    }

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize() .navigationBarsPadding(),
        contentWindowInsets = WindowInsets(0),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            // Definimos todas las pantallas donde la barra DEBE seguir visible
            val pantallasPrincipales = listOf(
                Screen.Menu.route,
                Screen.Calendario.route,
                Screen.Perfil.route,
                Screen.Emociones.route,
                Screen.Sensaciones.route,
                Screen.Meditacion.route,
                Screen.Emergencia.route,
                Screen.OrganizaDia.route
            )

            if (currentRoute in pantallasPrincipales) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    tonalElevation = 8.dp,
                    windowInsets = WindowInsets(0) // 👈 agrega esto
                ) {
                    val items = listOf(
                        BottomBarItem.Menu,
                        BottomBarItem.Calendario,
                        BottomBarItem.OrganizaDia,
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
                            //selected = currentRoute == item.route,
                            selected = currentRoute?.startsWith(item.route.substringBefore("?")) == true,
                            onClick = {
                                // Limpiamos la ruta para que siempre apunte a la pantalla base
                                // y no intente pasar parámetros nulos o incorrectos al navegar desde la barra
                                val routeBase = item.route.substringBefore("?")

                                navController.navigate(routeBase) {
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
}