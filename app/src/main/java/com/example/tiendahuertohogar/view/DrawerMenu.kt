package com.example.tiendahuertohogar.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tiendahuertohogar.navigation.AppRoutes
import com.example.tiendahuertohogar.navigation.PantallaInterna

/**
 * Contenido del ModalDrawerSheet (el menú lateral).
 *
 * @param vistaActual La pantalla interna que se está mostrando.
 * @param onNavegarInterna Lambda para cambiar la pantalla *dentro* de PantallaPrincipal.
 * @param onNavegarExterna Lambda para navegar a una ruta de *AppNav* (ej. Historial).
 * @param username El nombre de usuario (o ID) para mostrar y usar en la navegación.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerMenu(
    vistaActual: PantallaInterna,
    onNavegarInterna: (PantallaInterna) -> Unit,
    onNavegarExterna: (String) -> Unit,
    username: String
) {
    // Lista de las pantallas INTERNAS a mostrar
    val vistasInternas = listOf(
        PantallaInterna.Inicio,
        PantallaInterna.Catalogo,
        PantallaInterna.Perfil,
        PantallaInterna.Blog,
        PantallaInterna.Nosotros,
        PantallaInterna.Fidelizacion
    )

    ModalDrawerSheet {
        // Saludo al usuario
        Text(
            text = "¡Hola, $username!",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleMedium
        )
        Divider()

        // --- Items de Navegación Interna ---
        vistasInternas.forEach { vista ->
            NavigationDrawerItem(
                label = { Text(vista.titulo) },
                icon = { Icon(vista.icono, contentDescription = vista.titulo) },
                selected = (vistaActual == vista), // Resalta el ítem activo
                onClick = { onNavegarInterna(vista) }, // Llama a la navegación interna
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }

        Divider()

        // --- Items de Navegación Externa ---
        // (Enlaces a otras rutas de tu AppNav.kt)

        // Requerimiento: "Historial de Compras"
        val usuarioId = username.toLongOrNull() ?: 0L

        NavigationDrawerItem(
            label = { Text("Mi Historial de Pedidos") },
            icon = { Icon(Icons.Default.History, contentDescription = "Historial") },
            selected = false, // Nunca está seleccionada, es una acción
            onClick = {
                // Llama a la navegación externa con la ruta de AppNav
                onNavegarExterna("${AppRoutes.HISTORIAL_PEDIDOS}/$usuarioId")
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        // Botón para Cerrar Sesión
        NavigationDrawerItem(
            label = { Text("Cerrar Sesión") },
            icon = { Icon(Icons.Default.Logout, contentDescription = "Cerrar Sesión") },
            selected = false,
            onClick = {
                // Navega de vuelta al Login
                onNavegarExterna(AppRoutes.LOGIN)
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}