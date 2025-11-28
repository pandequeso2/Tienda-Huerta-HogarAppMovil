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
import androidx.compose.material3.HorizontalDivider // <-- Usamos HorizontalDivider
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
import com.example.tiendahuertohogar.data.repository.ProductoRepository // ¡Importación necesaria!
import com.example.tiendahuertohogar.data.dao.ProductoDao // Importación ficticia para Preview
import com.example.tiendahuertohogar.viewModel.ProductoViewModel
import com.example.tiendahuertohogar.viewModel.ProductoViewModelFactory // ¡Importación de la Factoría!


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoFormScreen(
    // 📢 AHORA RECIBE EL REPOSITORIO Y EL NAVCONTROLLER
    repository: ProductoRepository,
    navController: NavController
) {
    // 1. INSTANCIAMOS LA FACTORÍA Y EL VIEWMODEL
    val factory = remember {
        ProductoViewModelFactory(repository)
    }
    val viewModel: ProductoViewModel = viewModel(factory = factory)
    // ----------------------------------------------------

    // Estados para los campos del formulario
    var codigo by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var personalizable by remember { mutableStateOf(false) }

    // Observar la lista de productos del ViewModel (ahora viene de la BBDD)
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
                val precioDouble = precio.toDoubleOrNull() ?: 0.0

                // El ID se deja en 0, Room lo asignará automáticamente
                val producto = Producto(
                    id = 0,
                    codigo = codigo,
                    categoria = categoria,
                    nombre = nombre,
                    precio = precioDouble,
                    descripcion = descripcion,
                    personalizable = personalizable
                )

                // 📢 LLAMADA AL VIEWMODEL (guarda en la BBDD)
                viewModel.guardarProducto(producto)

                // Limpiar formulario
                codigo = ""
                categoria = ""
                nombre = ""
                precio = ""
                descripcion = ""
                personalizable = false

                // 📢 Volver a la pantalla anterior
                navController.popBackStack()
            },
            // Validación básica para habilitar el botón
            enabled = nombre.isNotBlank() && precio.isNotBlank() && categoria.isNotBlank()
        ) {
            Text("Guardar Producto")
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider() // Usamos HorizontalDivider para evitar el warning
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

// ⚠️ El Preview ya no funcionará directamente porque requiere el Repositorio.
// Necesitas crear un Mock del Repositorio para que el Preview funcione.
// Dejamos este preview de ejemplo, sabiendo que puede fallar.
@Preview(showBackground = true)
@Composable
fun PreviewProductoFormScreen() {
    // Ejemplo de cómo mockear el repositorio para el preview:
    // val mockDao = object : ProductoDao { /* Implementación vacía */ }
    // val mockRepository = ProductoRepository(mockDao)
    // ProductoFormScreen(repository = mockRepository, navController = rememberNavController())
    Text("Preview desactivado, requiere inyección de dependencias.")
}