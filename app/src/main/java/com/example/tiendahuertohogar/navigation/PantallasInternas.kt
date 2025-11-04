package com.example.tiendahuertohogar.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Define las pantallas "internas" que se gestionan DENTRO de PantallaPrincipal.
 * Estas son las opciones principales que se mostrarán en el DrawerMenu.
 */
sealed class PantallaInterna(
    val ruta: String,
    val titulo: String,
    val icono: ImageVector
) {
    // Pantalla de bienvenida
    object Inicio : PantallaInterna("inicio_interno", "Inicio", Icons.Default.Home)

    // Requerimiento: "Visualización de catálogo de productos"
    object Catalogo : PantallaInterna("catalogo", "Catálogo", Icons.Default.Store)

    // Requerimiento: "Gestión de perfiles de Usuario"
    object Perfil : PantallaInterna("perfil", "Mi Perfil", Icons.Default.Person)

    // Deseo: "Incluir un blog o sección de noticias"
    object Blog : PantallaInterna("blog", "Blog", Icons.Default.Book)

    // Deseo: "Añadir un mapa en el parte de nosotros"
    object Nosotros : PantallaInterna("nosotros", "Nuestras Tiendas", Icons.Default.LocationCity)

    // Deseo: "Programas de fidelización"
    object Fidelizacion : PantallaInterna("fidelizacion", "Mis Puntos", Icons.Default.Star)
}