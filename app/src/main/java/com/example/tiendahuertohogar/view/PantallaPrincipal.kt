package com.example.tiendahuertohogar.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
// No necesitamos el viewModel factory aquí, PerfilUsuarioScreen lo maneja
import androidx.navigation.NavHostController
import com.example.tiendahuertohogar.navigation.PantallaInterna
import kotlinx.coroutines.launch

// Importa tus pantallas
import com.example.tiendahuertohogar.view.PerfilUsuarioScreen
import com.example.tiendahuertohogar.view.MapaTiendasScreen
import com.example.tiendahuertohogar.view.InicioScreen
import com.example.tiendahuertohogar.view.CatalogoScreen
import com.example.tiendahuertohogar.view.BlogScreen
import com.example.tiendahuertohogar.view.FidelizacionScreen
// Ya no necesitamos importar el ViewModel aquí, lo quitamos

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(
    navController: NavHostController,
    username: String // Este 'username' es el ID que viene como String
) {
    var vistaActual by remember { mutableStateOf<PantallaInterna>(PantallaInterna.Inicio) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // --- CORRECCIÓN CLAVE ---
    // Convertimos el 'username' (String) al 'usuarioId' (Long)
    // que tu PerfilUsuarioScreen necesita.
    val usuarioId = username.toLongOrNull() ?: 0L

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerMenu(
                vistaActual = vistaActual,
                onNavegarInterna = { nuevaVista ->
                    vistaActual = nuevaVista
                    scope.launch { drawerState.close() }
                },
                onNavegarExterna = { ruta ->
                    scope.launch { drawerState.close() }
                    navController.navigate(ruta)
                },
                username = username // El DrawerMenu lo sigue usando como String
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(vistaActual.titulo) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* TODO: Navegar al carrito */ }) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                        }
                    }
                )
            }
        ) { paddingValues ->

            Surface(
                modifier = Modifier.padding(paddingValues).fillMaxSize(),
                color = Color(0xFFF7F7F7)
            ) {
                // --- BLOQUE 'WHEN' CORREGIDO ---
                when (vistaActual) {
                    is PantallaInterna.Inicio -> InicioScreen(username, navController)
                    is PantallaInterna.Catalogo -> CatalogoScreen(navController)
                    is PantallaInterna.Blog -> BlogScreen()
                    is PantallaInterna.Fidelizacion -> FidelizacionScreen()

                    // --- Pantallas que YA tenías (CORREGIDAS) ---

                    // 1. Pásale el 'usuarioId' (Long)
                    is PantallaInterna.Perfil -> PerfilUsuarioScreen(usuarioId = usuarioId)

                    // 2. Asumimos que MapaTiendasScreen también espera el navController
                    //    Si también da error, prueba quitándole el navController: MapaTiendasScreen()
                    is PantallaInterna.Nosotros -> MapaTiendasScreen()
                }
            }
        }
    }
}