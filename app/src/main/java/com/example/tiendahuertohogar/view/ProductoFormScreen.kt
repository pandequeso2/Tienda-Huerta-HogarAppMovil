package com.example.tiendahuertohogar.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiendahuertohogar.data.model.Producto
import com.example.tiendahuertohogar.viewModel.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoFormScreen(
    viewModel: ProductoViewModel = viewModel()
) {
    // Estados para los campos del formulario
    var codigo by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var personalizable by remember { mutableStateOf(false) }

    // Observar la lista de productos del ViewModel
    val productos by viewModel.productos.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registrar Nuevo Producto", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // --- Formulario ---
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del Producto") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = precio,
            onValueChange = { precio = it },
            label = { Text("Precio") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = categoria,
            onValueChange = { categoria = it },
            label = { Text("Categoría (ej: Tortas, Galletas)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = codigo,
            onValueChange = { codigo = it },
            label = { Text("Código/SKU") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = personalizable,
                onCheckedChange = { personalizable = it }
            )
            Text("Es personalizable")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // CAMBIO REALIZADO: Convertir a Double
                val precioDouble = precio.toDoubleOrNull() ?: 0.0

                val producto = Producto(
                    id = 0,
                    codigo = codigo,
                    categoria = categoria,
                    nombre = nombre,
                    precio = precioDouble, // Asignamos el Double
                    descripcion = descripcion,
                    personalizable = personalizable
                )
                viewModel.guardarProducto(producto)

                // Limpiar formulario
                codigo = ""
                categoria = ""
                nombre = ""
                precio = ""
                descripcion = ""
                personalizable = false
            },
            // Validación básica para habilitar el botón
            enabled = nombre.isNotBlank() && precio.isNotBlank() && categoria.isNotBlank()
        ) {
            Text("Guardar Producto")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        Text("Productos Registrados", style = MaterialTheme.typography.headlineSmall)

        // --- Lista de Productos ---
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(productos) { producto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                        Text(producto.descripcion, style = MaterialTheme.typography.bodySmall)
                        Text(
                            "Precio: $${producto.precio}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProductoFormScreen() {
    ProductoFormScreen()
}
