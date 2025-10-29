// ... otras importaciones
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.tiendahuertohogar.data.model.Producto
import com.example.tiendahuertohogar.viewModel.ProductoViewModel

// ...

@Composable
fun MuestraDatosScreen(
    viewModel: ProductoViewModel,
    onAddProducto: () -> Unit
) {
    // --- CAMBIO AQUÍ ---
    // En lugar de `uiState`, obtén directamente la lista `productos`.
    val listaDeProductos by viewModel.productos.collectAsState()

    Scaffold(
        // ... (el resto del Scaffold se queda igual)
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            // --- CAMBIO AQUÍ ---
            // Usa la nueva variable `listaDeProductos`
            items(listaDeProductos) { producto: Producto ->
                ListItem(
                    headlineContent = { Text(producto.nombre) },
                    supportingContent = { Text("${producto.precio} CLP - Stock: ${producto.stock}") },
                    trailingContent = { Text(producto.categoria) }
                )
                HorizontalDivider()
            }
        }
    }
}
