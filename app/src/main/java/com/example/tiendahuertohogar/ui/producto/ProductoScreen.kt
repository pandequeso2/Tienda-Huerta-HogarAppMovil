package com.example.tiendahuertohogar.ui.producto

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiendahuertohogar.R
import com.example.tiendahuertohogar.data.model.Producto
import com.example.tiendahuertohogar.viewModel.CartViewModel


@Composable
fun ProductsScreen(cartViewModel: CartViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            // Usa el color de fondo del tema [cite: 149]
            .background(MaterialTheme.colorScheme.background)
    ) {
        ProductList(cartViewModel = cartViewModel)
    }
}

@Composable
fun ProductList(cartViewModel: CartViewModel) {
    // --- LISTA DE PRODUCTOS ACTUALIZADA SEGÚN EL PDF ---
    // (Idealmente, esto vendría de un ViewModel y la BD)
    val listarProducto = listOf(
        Producto(
            id = 1,
            codigo = "FR001", // [cite: 199]
            categoria = "Frutas Frescas", // [cite: 194]
            nombre = "Manzanas Fuji", // [cite: 199]
            precio = 1200, // [cite: 240]
            descripcion = "Manzanas Fuji crujientes y dulces, cultivadas en el Valle del Maule.", // [cite: 242]
            personalizable = false,
            imagenResId = R.drawable.manzana_fuji // Imagen subida
        ),
        Producto(
            id = 2,
            codigo = "FR002", // [cite: 200]
            categoria = "Frutas Frescas", // [cite: 194]
            nombre = "Naranjas Valencia", // [cite: 200]
            precio = 1000, // [cite: 245]
            descripcion = "Jugosas y ricas en vitamina C, ideales para zumos frescos.", // [cite: 247]
            personalizable = false,
            imagenResId = R.drawable.naranja // Imagen subida
        ),
        Producto(
            id = 3,
            codigo = "FR003", // [cite: 201]
            categoria = "Frutas Frescas", // [cite: 194]
            nombre = "Plátanos Cavendish", // [cite: 201]
            precio = 800, // [cite: 250]
            descripcion = "Plátanos maduros y dulces, perfectos para el desayuno.", // [cite: 252]
            personalizable = false,
            imagenResId = R.drawable.platano // Imagen subida
        ),
        Producto(
            id = 4,
            codigo = "VR001", // [cite: 202]
            categoria = "Verduras Orgánicas", // [cite: 195]
            nombre = "Zanahorias Orgánicas", // [cite: 202]
            precio = 900, // [cite: 259]
            descripcion = "Zanahorias crujientes cultivadas sin pesticidas.", // [cite: 261]
            personalizable = false,
            imagenResId = R.drawable.zanahoria // Imagen subida
        ),
        Producto(
            id = 5,
            codigo = "VR002", // [cite: 203]
            categoria = "Verduras Orgánicas", // [cite: 195]
            nombre = "Espinacas Frescas", // [cite: 203]
            precio = 700, // [cite: 264]
            descripcion = "Espinacas frescas y nutritivas, perfectas para ensaladas.", // [cite: 266]
            personalizable = false,
            imagenResId = R.drawable.espinaca // Imagen subida
        ),
        Producto(
            id = 6,
            codigo = "VR003", // [cite: 204]
            categoria = "Verduras Orgánicas", // [cite: 195]
            nombre = "Pimientos Tricolores", // [cite: 204]
            precio = 1500, // [cite: 270]
            descripcion = "Pimientos rojos, amarillos y verdes, ideales para salteados.", // [cite: 272]
            personalizable = false,
            imagenResId = R.drawable.pimiento // Imagen subida
        ),
        Producto(
            id = 7,
            codigo = "PO001", // [cite: 206]
            categoria = "Productos Orgánicos", // [cite: 196]
            nombre = "Miel Orgánica", // [cite: 206]
            precio = 5000, // [cite: 279]
            descripcion = "Miel pura y orgánica producida por apicultores locales.", // [cite: 281]
            personalizable = false,
            imagenResId = R.drawable.miel // Imagen subida
        )
    )

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(listarProducto) { producto ->
            ProductCard(producto = producto, cartViewModel = cartViewModel)
        }
    }
}

@Composable
fun ProductCard(producto: Producto, cartViewModel: CartViewModel) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        // Usa el color de superficie (BlancoNieve) del tema
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            Image(
                painter = painterResource(id = producto.imagenResId),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )
            Box(modifier = Modifier.padding(16.dp)) {
                Column {
                    Text(
                        text = producto.nombre,
                        style = MaterialTheme.typography.titleLarge, // Tipografía del tema
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary // Color MarronClaro [cite: 154]
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = producto.descripcion,
                        style = MaterialTheme.typography.bodyMedium, // Tipografía del tema
                        color = MaterialTheme.colorScheme.onSurfaceVariant // Color GrisMedio [cite: 168]
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$${producto.precio} CLP",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface // Color GrisOscuro [cite: 166]
                    )
                }

                IconButton(
                    onClick = {
                        cartViewModel.addToCart(producto)
                        Toast.makeText(context, "${producto.nombre} añadido al carrito", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Añadir al carrito",
                        tint = MaterialTheme.colorScheme.primary // Color VerdeEsmeralda [cite: 152]
                    )
                }
            }
        }
    }
}