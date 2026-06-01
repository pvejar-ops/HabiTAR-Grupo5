package com.example.habitar.core.navegacion

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Menu : BottomBarItem("menu_screen", "Menú", Icons.Default.Home)
    object Calendario : BottomBarItem("calendario_screen", "Calendario", Icons.Default.CalendarMonth)
    object OrganizaDia : BottomBarItem("organiza_dia_screen", "Organizate", Icons.Default.Edit)
    object Perfil : BottomBarItem("perfil_screen", "Perfil", Icons.Default.Person)
}