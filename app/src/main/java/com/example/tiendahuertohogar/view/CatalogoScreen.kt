package com.example.tiendahuertohogar.view

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tiendahuertohogar.data.model.Producto
import com.example.tiendahuertohogar.navigation.AppRoutes
import com.example.tiendahuertohogar.viewmodel.ProductoViewModel
import java.net.URLEncoder

@Composable
fun CatalogoScreen(
    navController: NavHostController,
    viewModel: ProductoViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (uiState.productos.isEmpty()) {
            Text(
                text = "Cargando productos...",
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.productos) { producto ->
                    ProductoItemCard(
                        producto = producto,
                        onProductoClick = {
                            val nombreEncoded = URLEncoder.encode(producto.nombre, "UTF-8")
                            val precioEncoded = URLEncoder.encode(producto.precio.toString(), "UTF-8")
                            navController.navigate("${AppRoutes.PRODUCTO_FORM}/$nombreEncoded/$precioEncoded")
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoItemCard(
    producto: Producto,
    onProductoClick: () -> Unit
) {
    val context = LocalContext.current

    // Llama a la función helper para obtener el ID del drawable
    val imageResId = getDrawableId(context, producto.imagenUrl)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onProductoClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageResId), // Usa el ID encontrado
                contentDescription = producto.nombre,
                modifier = Modifier
                    .size(100.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(16.dp))

            Column {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "$${producto.precio} CLP",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Stock: ${producto.stock}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * Función helper para encontrar un ID de drawable basado en su nombre (String).
 * Si no lo encuentra, devuelve un 'placeholder' para evitar el crash.
 */
@Composable
private fun getDrawableId(context: Context, imageName: String?): Int {
    // 1. Define el nombre de tu imagen placeholder
    val placeholderName = "placeholder" // <-- CAMBIA ESTO por "placeholder_image" si así la llamaste

    if (imageName.isNullOrBlank()) {
        // Devuelve el placeholder si la URL está vacía
        return context.resources.getIdentifier(placeholderName, "drawable", context.packageName)
    }

    // 2. Intenta encontrar la imagen por su nombre (ej: "manzana_fuji")
    val id = context.resources.getIdentifier(imageName, "drawable", context.packageName)

    // 3. Si el ID es 0 (no encontrado), devuelve el placeholder. Si no, devuelve el ID.
    return if (id == 0) {
        context.resources.getIdentifier(placeholderName, "drawable", context.packageName)
    } else {
        id
    }
}