package com.example.tiendahuertohogar.data.model

import com.example.tiendahuertohogar.data.model.Producto

data class ItemCarrito(
    val producto: Producto,
    // La cantidad puede ser Int, ¡está perfecto!
    val cantidad: Int
) {
    // CAMBIO CLAVE: El subtotal debe ser Double para mayor precisión.
    val subtotal: Double
        // 1. Convertimos el precio del producto a Double para la multiplicación.
        get() = producto.precio.toDouble() * cantidad
}