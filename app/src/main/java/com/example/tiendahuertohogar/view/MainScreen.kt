package com.example.tiendahuertohogar.view

import kotlinx.coroutines.delay
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tiendahuertohogar.R
import com.example.tiendahuertohogar.navigation.AppRoutes // ¡Importante! Necesitamos las rutas
import com.example.tiendahuertohogar.navigation.BottomNavigationBar
import com.example.tiendahuertohogar.navigation.BottomNavItem
import com.example.tiendahuertohogar.ui.perfil.PerfilScreen
import com.example.tiendahuertohogar.ui.producto.ProductsScreen
import com.example.tiendahuertohogar.viewModel.CartViewModel
import kotlin.math.absoluteValue
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    username: String,
    mainNavController: NavController,
    cartViewModel: CartViewModel
) {
    val bottomNavController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { SearchBar() },
                actions = {

                    // --- 📢 NUEVO BOTÓN: NAVEGAR A PRODUCTO FORM ---
                    IconButton(onClick = {
                        mainNavController.navigate(AppRoutes.PRODUCTO_FORM)
                    }) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Agregar Nuevo Producto"
                        )
                    }
                    // ------------------------------------------------

                    // Botón para el escáner QR
                    IconButton(onClick = {
                        mainNavController.navigate(AppRoutes.QR_SCANNER)
                    }) {
                        Icon(
                            Icons.Outlined.QrCodeScanner,
                            contentDescription = "Escanear QR"
                        )
                    }

                    // --- NUEVO BOTÓN PARA VER LA API (POSTS) ---
                    IconButton(onClick = {
                        mainNavController.navigate(AppRoutes.POSTS)
                    }) {
                        Icon(Icons.Default.List, contentDescription = "Ver API Posts")
                    }

                    IconButton(onClick = { /* TODO: Lista de deseos */ }) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "Lista de Deseos")
                    }
                    IconButton(onClick = { /* TODO: Navegar al carrito */ }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito de Compras")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = bottomNavController)
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding),
            // Animaciones de transición entre pestañas
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreenContent(username = username)
            }
            composable(BottomNavItem.Productos.route) {
                ProductsScreen(cartViewModel = cartViewModel)
            }
            composable(BottomNavItem.Perfil.route) {
                PerfilScreen(mainNavController = mainNavController)
            }
        }
    }
}

@Composable
fun SearchBar() {
    var text by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Buscar",
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.width(8.dp))
        BasicTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            decorationBox = { innerTextField ->
                if (text.isEmpty()) {
                    Text("Buscar producto", color = Color.Gray)
                }
                innerTextField()
            }
        )
    }
}

@Composable
fun HomeScreenContent(username: String) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            StoreInfoSection(modifier = Modifier.padding(vertical = 16.dp))
        }

        // Aquí se llama al Carrusel
        item {
            CarruselDestacados()
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Sobre Nosotros",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "HuertoHogar es una tienda online dedicada a llevar la frescura y calidad de los productos del campo directamente a la puerta de nuestros clientes en Chile.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        item {
            Text(
                text = "@ 2025 Huerto Hogar",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun StoreInfoSection(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val textColor = MaterialTheme.colorScheme.onBackground
    val storeOptions = listOf(
        Triple(Icons.Default.LocationOn, "Tiendas", "geo:0,0?q=Santiago, Puerto Montt, Villarica, Nacimiento, Viña del Mar, Valparaíso, y Concepción"),
        Triple(Icons.Default.Phone, "Llámanos", "tel:+56912345678"),
        Triple(Icons.Default.Share, "Redes", "https://www.instagram.com")
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        storeOptions.forEach { (icon, text, actionUri) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(actionUri))
                    context.startActivity(intent)
                }
            ) {
                Icon(imageVector = icon, contentDescription = text, tint = textColor, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = text, style = MaterialTheme.typography.bodySmall, color = textColor)
            }
        }
    }
}

// Esta función debe tener @Composable y @OptIn para funcionar correctamente
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CarruselDestacados() {
    // Lista de imágenes
    val imagenes = listOf(
        R.drawable.manzana_fuji,
        R.drawable.naranja,
        R.drawable.platano,
        R.drawable.miel
    )

    // Estado del Pager
    val pagerState = rememberPagerState(pageCount = { imagenes.size })

    // --- LÓGICA DE AUTO-SCROLL AÑADIDA ---
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000) // Espera 3000 milisegundos (3 segundos)

            // Calcula la siguiente página
            val nextPage = (pagerState.currentPage + 1) % imagenes.size

            // Realiza la animación de desplazamiento
            pagerState.animateScrollToPage(nextPage)
        }
    }
    // -------------------------------------

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Ofertas del Día",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(all = 16.dp),
            color = MaterialTheme.colorScheme.tertiary
        )

        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 32.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) { page ->
            // Animación de escala al deslizar (Efecto Zoom)
            Card(
                modifier = Modifier
                    .graphicsLayer {
                        val pageOffset = (
                                (pagerState.currentPage - page) + pagerState
                                    .currentPageOffsetFraction
                                ).absoluteValue

                        lerp(
                            start = 0.85f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }

                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Image(
                    painter = painterResource(id = imagenes[page]),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}