package com.example.tiendahuertohogar.data.model

import com.example.tiendahuertohogar.data.model.Producto
data class CartItem(
    // Asumo que tu clase Producto ya existe, y la usaremos como base.
    val producto: Producto,
    var cantidad: Int = 1
) {
    // Función para calcular el subtotal de este ítem
    val subtotal: Int
        get() = producto.precio * cantidad
}