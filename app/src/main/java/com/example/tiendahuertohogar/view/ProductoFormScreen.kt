package com.example.tiendahuertohogar.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn // Seguimos usando LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tiendahuertohogar.data.model.Producto
import com.example.tiendahuertohogar.data.repository.ProductoRepository
import com.example.tiendahuertohogar.data.dao.ProductoDao
import com.example.tiendahuertohogar.viewModel.ProductoViewModel
import com.example.tiendahuertohogar.viewModel.ProductoViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoFormScreen(
    repository: ProductoRepository,
    navController: NavController
) {
    val factory = remember {
        ProductoViewModelFactory(repository)
    }
    val viewModel: ProductoViewModel = viewModel(factory = factory)

    // Estados para los campos del formulario
    var codigo by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var personalizable by remember { mutableStateOf(false) }

    // Observar la lista de productos
    val productos by viewModel.productos.collectAsState()

    // 📢 CAMBIO CLAVE: Usamos LazyColumn como contenedor principal para manejar todo el scroll
    LazyColumn( // 👈 LazyColumn ahora contiene TODO el contenido
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Contenido del Formulario (Ahora dentro del LazyColumn) ---
        item {
            Text("Registrar Nuevo Producto", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

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
                    val precioDouble = precio.toDoubleOrNull() ?: 0.0
                    val producto = Producto(
                        id = 0,
                        codigo = codigo,
                        categoria = categoria,
                        nombre = nombre,
                        precio = precioDouble,
                        descripcion = descripcion,
                        personalizable = personalizable
                        // Asegúrate de incluir aquí el campo 'comentario' si sigue en el Model
                        // comentario = ""
                    )
                    viewModel.guardarProducto(producto)

                    // Limpiar formulario
                    codigo = ""
                    categoria = ""
                    nombre = ""
                    precio = ""
                    descripcion = ""
                    personalizable = false
                    navController.popBackStack()
                },
                enabled = nombre.isNotBlank() && precio.isNotBlank() && categoria.isNotBlank()
            ) {
                Text("Guardar Producto")
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Productos Registrados", style = MaterialTheme.typography.headlineSmall)
        }
        // --- FIN: Contenido del Formulario ---


        // --- Lista de Productos (Usamos items(productos) directo en LazyColumn) ---
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
    } // FIN LazyColumn
}

// ... (Preview)