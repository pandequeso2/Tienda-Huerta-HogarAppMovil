package com.example.tiendahuertohogar.ui.producto

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiendahuertohogar.R
import com.example.tiendahuertohogar.data.model.Producto
import com.example.tiendahuertohogar.viewModel.CartViewModel
import kotlinx.coroutines.delay
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
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(2000)
        isLoading = false
    }

    val listarProducto = listOf(
        Producto(
            id = 1,
            codigo = "FR001",
            categoria = "Frutas Frescas",
            nombre = "Manzanas Fuji",
            precio = 1200.00,
            descripcion = "Manzanas Fuji crujientes y dulces, cultivadas en el Valle del Maule.",
            personalizable = false,
            imagenResId = R.drawable.manzana_fuji
        ),
        Producto(
            id = 2,
            codigo = "FR002",
            categoria = "Frutas Frescas",
            nombre = "Naranjas Valencia",
            precio = 1000.00,
            descripcion = "Jugosas y ricas en vitamina C, ideales para zumos frescos.",
            personalizable = false,
            imagenResId = R.drawable.naranja
        ),
        Producto(
            id = 3,
            codigo = "FR003",
            categoria = "Frutas Frescas",
            nombre = "Plátanos Cavendish",
            precio = 800.00,
            descripcion = "Plátanos maduros y dulces, perfectos para el desayuno.",
            personalizable = false,
            imagenResId = R.drawable.platano
        ),
        Producto(
            id = 4,
            codigo = "VR001",
            categoria = "Verduras Orgánicas",
            nombre = "Zanahorias Orgánicas",
            precio = 900.00,
            descripcion = "Zanahorias crujientes cultivadas sin pesticidas.",
            personalizable = false,
            imagenResId = R.drawable.zanahoria
        ),
        Producto(
            id = 5,
            codigo = "VR002",
            categoria = "Verduras Orgánicas",
            nombre = "Espinacas Frescas",
            precio = 700.00,
            descripcion = "Espinacas frescas y nutritivas, perfectas para ensaladas.",
            personalizable = false,
            imagenResId = R.drawable.espinaca
        ),
        Producto(
            id = 6,
            codigo = "VR003",
            categoria = "Verduras Orgánicas",
            nombre = "Pimientos Tricolores",
            precio = 1500.00,
            descripcion = "Pimientos rojos, amarillos y verdes, ideales para salteados.",
            personalizable = false,
            imagenResId = R.drawable.pimiento
        ),
        Producto(
            id = 7,
            codigo = "PO001",
            categoria = "Productos Orgánicos",
            nombre = "Miel Orgánica",
            precio = 5000.00,
            descripcion = "Miel pura y orgánica producida por apicultores locales.",
            personalizable = false,
            imagenResId = R.drawable.miel
        )
    )

    AnimatedContent(
        targetState = isLoading,
        label = "listTransition"
    ) { loading ->
        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(listarProducto) { producto ->
                    ProductCard(producto = producto, cartViewModel = cartViewModel)
                }
            }
        }
    }
}

@Composable
fun ProductCard(producto: Producto, cartViewModel: CartViewModel) {
    val context = LocalContext.current
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.2f else 1f,
        label = "scaleAnimation"
    )

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(150)
            isPressed = false
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
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
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = producto.descripcion,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$${producto.precio} CLP",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(
                    onClick = {
                        isPressed = true
                        cartViewModel.addToCart(producto)
                        Toast.makeText(
                            context,
                            "${producto.nombre} añadido al carrito",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .graphicsLayer(scaleX = scale, scaleY = scale)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Añadir al carrito",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}