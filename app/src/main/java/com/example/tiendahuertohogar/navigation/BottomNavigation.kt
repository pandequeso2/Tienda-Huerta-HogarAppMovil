package com.example.tiendahuertohogar.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store // Icono para "Productos"
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(
        route = "home",
        title = "Inicio",
        icon = Icons.Default.Home
    )
    object Productos : BottomNavItem(
        route = "productos",
        title = "Productos",
        icon = Icons.Default.Store
    )
    object Perfil : BottomNavItem(
        route = "perfil",
        title = "Perfil",
        icon = Icons.Default.Person
    )
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    // Lista de todas las pestañas que queremos mostrar
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Productos,
        BottomNavItem.Perfil
    )

    NavigationBar(
        // Aplicamos los colores del tema (VerdeEsmeralda)
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    // Navega a la pestaña seleccionada
                    navController.navigate(item.route) {
                        // Limpia la pila de navegación para evitar acumular pantallas
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}