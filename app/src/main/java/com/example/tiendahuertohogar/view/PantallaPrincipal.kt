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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.tiendahuertohogar.data.database.ProductoDatabase // Importa tu BD
import com.example.tiendahuertohogar.data.repository.ProductoRepository // Importa tu Repo
import com.example.tiendahuertohogar.navigation.PantallaInterna
import com.example.tiendahuertohogar.viewmodel.ProductoViewModel // Importa tu VM
import com.example.tiendahuertohogar.viewmodel.ProductoViewModelFactory // Importa tu Factory
import kotlinx.coroutines.launch

// Importa tus pantallas
import com.example.tiendahuertohogar.view.PerfilUsuarioScreen
import com.example.tiendahuertohogar.view.MapaTiendasScreen
import com.example.tiendahuertohogar.view.InicioScreen
import com.example.tiendahuertohogar.view.CatalogoScreen
import com.example.tiendahuertohogar.view.BlogScreen
import com.example.tiendahuertohogar.view.FidelizacionScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(
    navController: NavHostController,
    username: String
) {
    var vistaActual by remember { mutableStateOf<PantallaInterna>(PantallaInterna.Inicio) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope() // Este scope es crucial
    val usuarioId = username.toLongOrNull() ?: 0L

    // --- LÓGICA DE VIEWMODELS ---
    val context = LocalContext.current.applicationContext

    // 1. Instanciamos las dependencias para ProductoViewModel
    // Pasamos el 'scope' a getDatabase para el callback de pre-poblado
    val database = ProductoDatabase.getDatabase(context, scope)
    val productoRepository = ProductoRepository(database.productoDao()) // Tu repo recibe el DAO
    val productoViewModelFactory = ProductoViewModelFactory(productoRepository)

    // 2. Instanciamos el ProductoViewModel usando su factory
    val productoViewModel: ProductoViewModel = viewModel(factory = productoViewModelFactory)

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
                username = username
            )
        }
    ) {
        Scaffold(
            topBar = { /* ... tu TopAppBar ... */ }
        ) { paddingValues ->

            Surface(
                modifier = Modifier.padding(paddingValues).fillMaxSize(),
                color = Color(0xFFF7F7F7)
            ) {
                // --- 'WHEN' MODIFICADO ---
                when (vistaActual) {
                    is PantallaInterna.Inicio -> InicioScreen(username, navController)

                    // 3. Pasa el ViewModel instanciado a la pantalla
                    is PantallaInterna.Catalogo -> CatalogoScreen(
                        navController = navController,
                        viewModel = productoViewModel // Pasamos tu ViewModel
                    )

                    is PantallaInterna.Blog -> BlogScreen()
                    is PantallaInterna.Fidelizacion -> FidelizacionScreen()
                    is PantallaInterna.Perfil -> PerfilUsuarioScreen(usuarioId = usuarioId)
                    is PantallaInterna.Nosotros -> MapaTiendasScreen()
                }
            }
        }
    }
}