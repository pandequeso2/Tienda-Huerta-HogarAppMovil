// Asegúrate de que el paquete sea el correcto para tu proyecto
package com.example.tiendahuertohogar.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tiendahuertohogar.data.model.Producto
import com.example.tiendahuertohogar.viewModel.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoFormScreen(
    navController: NavController,
    nombre: String,
    precio: String,
    viewModel: ProductoViewModel = hiltViewModel()
) {
    // Estados para cada campo del formulario
    var codigo by remember { mutableStateOf("") }
    var nombreState by remember { mutableStateOf(nombre) }
    var descripcion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var precioState by remember { mutableStateOf(precio) }
    var stock by remember { mutableStateOf("") }

    // Estado para controlar si el formulario es válido
    val isFormValid by remember(codigo, nombreState, descripcion, categoria, precioState, stock) {
        derivedStateOf {
            codigo.isNotBlank() &&
                    nombreState.isNotBlank() &&
                    descripcion.isNotBlank() &&
                    categoria.isNotBlank() &&
                    (precioState.toDoubleOrNull() ?: 0.0) > 0.0 &&
                    (stock.toIntOrNull() ?: -1) >= 0
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Producto") },
                navigationIcon = {
                    // Puedes agregar un ícono para volver atrás si lo necesitas
                    // IconButton(onClick = { navController.popBackStack() }) { ... }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), // Para que el formulario sea deslizable
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Detalles del Producto", style = MaterialTheme.typography.headlineMedium)

            OutlinedTextField(
                value = codigo,
                onValueChange = { codigo = it },
                label = { Text("Código (SKU)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = nombreState,
                onValueChange = { nombreState = it },
                label = { Text("Nombre del Producto") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            OutlinedTextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoría") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // ... (dentro de la Row)
                OutlinedTextField(
                    value = precioState,
                    onValueChange = { precioState = it },
                    label = { Text("Precio") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), // <-- LÍNEA CORREGIDA
                    prefix = { Text("$") }
                )

                OutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it },
                    label = { Text("Stock Disponible") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val nuevoProducto = Producto(
                        // el 'id' se autogenera, por eso no lo incluimos aquí
                        codigo = codigo,
                        nombre = nombreState,
                        descripcion = descripcion,
                        categoria = categoria,
                        // Convertimos a Double y Int al guardar
                        precio = precioState.toDoubleOrNull() ?: 0.0,
                        stock = stock.toIntOrNull() ?: 0,
                        imagenUrl = null // Puedes añadir lógica para la imagen más tarde
                    )
                    viewModel.guardarProducto(nuevoProducto)

                    // Opcional: Navegar hacia atrás después de guardar
                    navController.popBackStack()
                },
                enabled = isFormValid, // El botón solo se activa si el formulario es válido
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Producto")
            }
        }
    }
}