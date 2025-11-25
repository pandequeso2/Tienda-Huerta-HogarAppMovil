package com.example.tiendahuertohogar.manager

import androidx.compose.runtime.mutableStateListOf
import com.example.tiendahuertohogar.data.model.ItemCarrito
// Asegúrate de importar tu modelo de Producto si es necesario
import com.example.tiendahuertohogar.data.model.Producto

object CarritoManager {
    // Usamos mutableStateListOf para que Compose detecte cambios automáticamente
    private val _items = mutableStateListOf<ItemCarrito>()

    val items: List<ItemCarrito>
        get() = _items

    /**
     * Función para agregar un producto al carrito.
     * Si ya existe, aumenta la cantidad (dependiendo de tu lógica de CartItem).
     * Nota: Necesitarás ajustar 'Producto' al tipo de dato real que usas.
     */
    fun agregarProducto(producto:Producto) {
        // Buscamos si ya existe el producto en la lista
        val itemExistente = _items.find { it.producto.id == producto.id }

        if (itemExistente != null) {
            // Si es un data class inmutable, reemplazamos el objeto con una copia incrementada
            val index = _items.indexOf(itemExistente)
            _items[index] = itemExistente.copy(cantidad = itemExistente.cantidad + 1)
        } else {
            // Si no existe, lo agregamos (asumiendo que el constructor es Producto, Cantidad)
            _items.add(ItemCarrito(producto = producto, cantidad = 1))
        }
    }

    /**
     * Función para eliminar un item completo del carrito.
     * Esta es la función que te faltaba y causaba el error.
     */
    fun eliminarItem(item: ItemCarrito) {
        _items.remove(item)
    }

    /**
     * Función para cambiar la cantidad de un item específico.
     */
    fun cambiarCantidad(item: ItemCarrito, nuevaCantidad: Int) {
        if (nuevaCantidad <= 0) {
            eliminarItem(item)
            return
        }

        val index = _items.indexOf(item)
        if (index != -1) {
            // Actualizamos el item en la lista
            _items[index] = item.copy(cantidad = nuevaCantidad)
        }
    }

    fun limpiarCarrito() {
        _items.clear()
    }

    /**
     * Calcula el total sumando los subtotales.
     * Convierte a Double para evitar el error de tipos.
     */
    fun getTotal(): Double {
        // Asumiendo que CartItem tiene una propiedad 'subtotal'
        // Si 'subtotal' es Int, lo convertimos a Double.
        return _items.sumOf { it.subtotal.toDouble() }
    }
}
