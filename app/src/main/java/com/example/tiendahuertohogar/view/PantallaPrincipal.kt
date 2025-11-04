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
// --- CORRECCIÓN EN LA IMPORTACIÓN ---
import com.example.tiendahuertohogar.data.database.ProductoDataBase
import com.example.tiendahuertohogar.data.repository.ProductoRepository
import com.example.tiendahuertohogar.navigation.PantallaInterna
import com.example.tiendahuertohogar.viewmodel.ProductoViewModel
import com.example.tiendahuertohogar.viewmodel.ProductoViewModelFactory
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
    val scope = rememberCoroutineScope()
    val usuarioId = username.toLongOrNull() ?: 0L

    val context = LocalContext.current.applicationContext

    // --- CORRECCIÓN AQUÍ (ProductoDataBase con 'B') ---
    val database = ProductoDataBase.getDatabase(context, scope)
    val productoRepository = ProductoRepository(database.productoDao())
    val productoViewModelFactory = ProductoViewModelFactory(productoRepository)

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
            topBar = {
                TopAppBar(
                    title = { Text(vistaActual.titulo) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Abrir menú"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* TODO: Carrito */ }) {
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
                when (vistaActual) {
                    is PantallaInterna.Inicio -> InicioScreen(username, navController)
                    is PantallaInterna.Catalogo -> CatalogoScreen(
                        navController = navController,
                        viewModel = productoViewModel
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