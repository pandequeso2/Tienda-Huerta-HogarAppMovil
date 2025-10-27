// ui/home/MuestraDatosScreen.kt
package com.example.tiendahuertohogar.ui.home

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.tiendahuertohogar.viewModel.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuestraDatosScreen(
    viewModel: ProductoViewModel,
    onAddProducto: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Productos HuertoHogar") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddProducto) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Producto")
            }
        }
    ) { paddingValues ->
        LazyColumn(contentPadding = paddingValues) {
            items(uiState.productos) { producto ->
                ListItem(
                    headlineText = { Text(producto.nombre) },
                    supportingText = { Text("${producto.precio} CLP - Stock: ${producto.stock}") },
                    trailingContent = { Text(producto.categoria) }
                )
                Divider()
            }
        }
    }
}