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
import android.content.Context
import android.os.Build
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.habitar.core.navegacion.AppNavGraph
import com.example.habitar.core.ui.theme.HabiTARTheme
import androidx.compose.ui.unit.dp
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.*
import com.example.habitar.core.SupabaseConfig
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {

    var tokenRestablecimiento: String? = null
    var usuarioLogueado: Boolean = false

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            programarNotificacionDiaria()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleIntent(intent)

        // Verificar si hay sesión guardada
        runBlocking {
            try {
                val prefs = getSharedPreferences("habitar_prefs", Context.MODE_PRIVATE)
                val recordarme = prefs.getBoolean("recordarme", false)

                if (recordarme) {
                    SupabaseConfig.client.auth.awaitInitialization()
                    usuarioLogueado = SupabaseConfig.client.auth.currentSessionOrNull() != null
                }
            } catch (e: Exception) {
                usuarioLogueado = false
            }
        }

        setContent {
            HabiTARTheme(darkTheme = false) {
                MainScreen(
                    tokenReset = tokenRestablecimiento,
                    usuarioLogueado = usuarioLogueado
                )
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            programarNotificacionDiaria()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val data: Uri? = intent.data
        if (data != null && data.scheme == "habitar" && data.host == "reset-password") {
            val accessToken = data.getQueryParameter("access_token")
            if (!accessToken.isNullOrBlank()) {
                tokenRestablecimiento = accessToken
            }
        }
    }

    private fun programarNotificacionDiaria() {
        val ahora = Calendar.getInstance()

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
    fun MainScreen(
        tokenReset: String? = null,
        usuarioLogueado: Boolean = false
    ) {
        val navController = rememberNavController()
        val startDestination = if (usuarioLogueado) Screen.Menu.route else Screen.Inicio.route

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        if (tokenReset != null) {
            androidx.compose.runtime.LaunchedEffect(tokenReset) {
                navController.navigate("reset_password_screen/$tokenReset")
            }
        }

        Scaffold(
            modifier = Modifier.fillMaxSize().navigationBarsPadding(),
            contentWindowInsets = WindowInsets(0),
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                val pantallasPrincipales = listOf(
                    Screen.Menu.route,
                    Screen.Calendario.route,
                    Screen.Perfil.route,
                    Screen.Emociones.route,
                    Screen.Sensaciones.route,
                    Screen.Meditacion.route,
                    Screen.Emergencia.route,
                    Screen.OrganizaDia.route,
                    Screen.Historial.route
                )

                if (currentRoute in pantallasPrincipales) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        tonalElevation = 8.dp,
                        windowInsets = WindowInsets(0)
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
                                selected = currentRoute?.startsWith(item.route.substringBefore("?")) == true,
                                onClick = {
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
                startDestination = startDestination,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}