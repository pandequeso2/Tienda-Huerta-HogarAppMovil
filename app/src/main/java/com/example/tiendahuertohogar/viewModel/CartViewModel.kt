package com.example.tiendahuertohogar.viewModel

import androidx.lifecycle.ViewModel
import com.example.tiendahuertohogar.data.model.ItemCarrito
import com.example.tiendahuertohogar.data.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.collections.sumOf

class CartViewModel : ViewModel() {

    // Lista que mantiene el estado del carrito (usa ItemCarrito para manejar la cantidad)
    private val _cartItems = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val cartItems = _cartItems.asStateFlow()

    /**
     * Agrega un producto al carrito (o incrementa su cantidad si ya existe).
     */
    fun addToCart(product: Producto) {
        val currentItems = _cartItems.value.toMutableList()
        // Buscamos si el producto ya está en el carrito
        val existingItem = currentItems.find { it.producto.id == product.id }

        if (existingItem != null) {
            // Si ya existe, creamos una copia con la cantidad incrementada en 1
            val index = currentItems.indexOf(existingItem)
            currentItems[index] = existingItem.copy(cantidad = existingItem.cantidad + 1)
        } else {
            // Si es nuevo, lo añadimos como un ItemCarrito con cantidad 1
            currentItems.add(ItemCarrito(producto = product, cantidad = 1))
        }
        _cartItems.value = currentItems.toList()
    }

    /**
     * Elimina una unidad del item. Si la cantidad llega a 0, elimina el item completo.
     */
    fun decrementQuantity(itemToDecrement: ItemCarrito) {
        val currentItems = _cartItems.value.toMutableList()
        val index = currentItems.indexOfFirst { it.producto.id == itemToDecrement.producto.id }

        if (index != -1) {
            val currentQuantity = currentItems[index].cantidad
            if (currentQuantity > 1) {
                // Decrementamos la cantidad en 1
                currentItems[index] = itemToDecrement.copy(cantidad = currentQuantity - 1)
                _cartItems.value = currentItems.toList()
            } else {
                // Si la cantidad es 1, lo eliminamos completamente (filtrando)
                removeFromCart(itemToDecrement)
            }
        }
    }

    /**
     * Elimina un item completo del carrito, independientemente de la cantidad.
     */
    fun removeFromCart(itemToRemove: ItemCarrito) {
        // Usamos filter para crear una nueva lista sin el ítem a eliminar
        _cartItems.value = _cartItems.value.filter { it.producto.id != itemToRemove.producto.id }
    }


    /**
     * Calcula el total de la compra sumando los subtotales de todos los ítems.
     */
    fun getTotal(): Double {
        // sumOf es ideal para sumar Doubles
        return _cartItems.value.sumOf { it.subtotal }
    }

    /**
     * Limpia completamente el carrito.
     */
    fun clearCart() {
        _cartItems.value = emptyList()
    }
}