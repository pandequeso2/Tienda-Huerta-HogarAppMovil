// Archivo: ui/screens/CarritoScreen.kt (ajusta el paquete según tu estructura)
package com.example.tiendahuertohogar.ui.Carrito

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tiendahuertohogar.manager.CarritoManager
import com.example.tiendahuertohogar.data.model.ItemCarrito

@Composable
fun CarritoScreen(
    onCheckoutClick: () -> Unit // Callback para ir a pagar
) {
    // Observamos la lista. Nota: Como _items es una MutableList normal en tu Singleton,
    // Compose no detectará cambios automáticamente a menos que usemos un State.
    // Una forma rápida (aunque no la arquitectura ideal MVVM) es forzar la recomposición
    // o usar un mutableStateListOf en el Manager.

    // **Mejora recomendada para CarritoManager:**
    // Sería ideal cambiar `private val _items = mutableListOf<CartItem>()` 
    // por `private val _items = mutableStateListOf<CartItem>()` para que Compose reaccione solo.

    // Asumiendo que _items es una lista normal por ahora, usaremos un truco simple de estado aquí:
    var items by remember { mutableStateOf(CarritoManager.items) }

    // Recalcular total cada vez que cambien los items
    val total = remember(items) { CarritoManager.getTotal() }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Mi Carrito", style = MaterialTheme.typography.headlineMedium)

        if (items.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("El carrito está vacío")
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(items) { item ->
                    CartItemRow(
                        item = item,
                        onRemove = {
                            CarritoManager.eliminarItem(item)
                            items = CarritoManager.items // Actualizar estado local
                        },
                        onAdd = {
                            CarritoManager.agregarProducto(item.producto)
                            items = CarritoManager.items
                        },
                        onDecrease = {
                            if (item.cantidad > 1) {
                                CarritoManager.cambiarCantidad(item, item.cantidad - 1)
                            } else {
                                CarritoManager.eliminarItem(item)
                            }
                            items = CarritoManager.items
                        }
                    )
                    HorizontalDivider()
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sección de Total y Botón Pagar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Total:", style = MaterialTheme.typography.titleLarge)
            Text(text = "$${total}", style = MaterialTheme.typography.titleLarge)
        }

        Button(
            onClick = onCheckoutClick,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            enabled = items.isNotEmpty()
        ) {
            Text("Finalizar Compra")
        }
    }
}

@Composable
fun CartItemRow(
    item: ItemCarrito,
    onRemove: () -> Unit,
    onAdd: () -> Unit,
    onDecrease: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Aquí podrías poner la imagen del producto si tienes la URL
        // AsyncImage(model = item.producto.imagenUrl, ...)

        Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
            Text(text = item.producto.nombre, style = MaterialTheme.typography.bodyLarge)
            Text(text = "$${item.producto.precio}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Subtotal: $${item.subtotal}", style = MaterialTheme.typography.labelMedium)
        }

        // Controles de cantidad
        IconButton(onClick = onDecrease) { Icon(Icons.Default.Remove, "Disminuir") }
        Text(text = "${item.cantidad}")
        IconButton(onClick = onAdd) { Icon(Icons.Default.Add, "Aumentar") }

        // Botón eliminar
        IconButton(onClick = onRemove) {
            Icon(Icons.Default.Delete, "Eliminar", tint = MaterialTheme.colorScheme.error)
        }
    }
}
