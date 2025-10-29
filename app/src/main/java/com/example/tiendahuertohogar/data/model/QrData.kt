package com.example.tiendahuertohogar.data.model // O donde pongas tus modelos

data class QrData(
    val nombre: String,
    val precio: String // Lo mantenemos como String para simplificar el parseo
)
