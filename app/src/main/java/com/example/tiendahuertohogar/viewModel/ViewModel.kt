package com.example.tiendahuertohogar.viewModel


import androidx.lifecycle.ViewModel
import com.example.tiendahuertohogar.data.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartViewModel : ViewModel() {

    // _cartItems es una lista interna mutable que contiene los productos.
    // MutableStateFlow permite que los observadores (la UI) reaccionen a los cambios.
    private val _cartItems = MutableStateFlow<List<Producto>>(emptyList())

    // cartItems es la versión pública e inmutable que la UI observará.
    val cartItems = _cartItems.asStateFlow()

    // Función para añadir un producto al carrito.
    fun addToCart(product: Producto) {
        // Obtenemos la lista actual y le añadimos el nuevo producto.
        _cartItems.value = _cartItems.value + product
    }

    // Función para obtener el precio total del carrito.
    fun getTotalPrice(): Double {
        return _cartItems.value.sumOf { it.precio.toDouble() }
    }
}
