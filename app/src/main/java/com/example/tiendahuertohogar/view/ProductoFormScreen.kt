// ui/view/ProductoFormScreen.kt
package com.example.tiendahuertohogar.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.* // Importa todo lo de runtime, incluyendo derivedStateOf, getValue, setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tiendahuertohogar.data.model.Producto
import com.example.tiendahuertohogar.viewModel.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoFormScreen(
    viewModel: ProductoViewModel = hiltViewModel(),
    onSave: () -> Unit
) {
    var codigo by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }

    val isFormValid by derivedStateOf {
        codigo.isNotBlank() && nombre.isNotBlank() && precio.toDoubleOrNull() != null && stock.toIntOrNull() != null
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // ... (el resto del código es idéntico y correcto)
        Text("Nuevo Producto", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = codigo, onValueChange = { codigo = it }, label = { Text("Código (ej: FR004)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre del Producto") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoría (ej: Frutas)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            value = precio,
            onValueChange = { precio = it },
            label = { Text("Precio por Kilo/Unidad") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberDecimal),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = stock,
            onValueChange = { stock = it },
            label = { Text("Stock disponible") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val nuevoProducto = Producto(
                    codigo = codigo,
                    nombre = nombre,
                    precio = precio.toDoubleOrNull() ?: 0.0,
                    stock = stock.toIntOrNull() ?: 0,
                    descripcion = descripcion,
                    categoria = categoria,
                    imagenUrl = ""
                )
                viewModel.addProducto(nuevoProducto)
                onSave()
            },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Producto")
        }
    }
}
