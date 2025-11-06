package com.example.tiendahuertohogar.view

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tiendahuertohogar.navigation.BottomNavigationBar // Asumiendo que estos existen
import com.example.tiendahuertohogar.navigation.BottomNavItem       // y están correctos
import com.example.tiendahuertohogar.ui.perfil.PerfilScreen
import com.example.tiendahuertohogar.ui.producto.ProductsScreen
import com.example.tiendahuertohogar.viewModel.CartViewModel

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
                    IconButton(onClick = { /* TODO: Lista de deseos */ }) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "Lista de Deseos")
                    }
                    IconButton(onClick = { /* TODO: Navegar al carrito */ }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito de Compras")
                    }
                },
                // Color primario del tema (VerdeEsmeralda) [cite: 152]
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
            modifier = Modifier.padding(innerPadding)
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
            // Color de fondo del tema (BlancoSuave) [cite: 149]
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            StoreInfoSection(modifier = Modifier.padding(vertical = 16.dp))
        }

        // --- CONTENIDO DE PASTELERÍA ELIMINADO ---
        // Se ha quitado ProductCarousel y FeaturedProducts de pasteles.
        // Aquí se debería añadir el contenido de Huerto Hogar (p.ej. Categorías)

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), // GrisClaro
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Sobre Nosotros",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.tertiary, // MarronClaro [cite: 154]
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        // Texto actualizado al de Huerto Hogar [cite: 86]
                        text = "HuertoHogar es una tienda online dedicada a llevar la frescura y calidad de los productos del campo directamente a la puerta de nuestros clientes en Chile.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface // GrisOscuro [cite: 166]
                    )
                }
            }
        }
        item {
            Text(
                text = "@ 2025 Huerto Hogar", // Actualizado
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
    // Texto principal (GrisOscuro) [cite: 166]
    val textColor = MaterialTheme.colorScheme.onBackground
    val storeOptions = listOf(
        // Ubicaciones del PDF [cite: 141]
        Triple(Icons.Default.LocationOn, "Tiendas", "geo:0,0?q=Santiago, Puerto Montt, Villarica, Nacimiento, Viña del Mar, Valparaíso, y Concepción"),
        Triple(Icons.Default.Phone, "Llámanos", "tel:+56912345678"), // Teléfono de ejemplo
        Triple(Icons.Default.Share, "Redes", "https://www.instagram.com") // RRSS de ejemplo
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