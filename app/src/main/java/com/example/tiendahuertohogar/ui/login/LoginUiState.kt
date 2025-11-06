package com.example.tiendahuertohogar.ui.login

data class LoginUiState(
    val correo: String = "",
    val clave: String = "",
    val mensaje: String = "",
    val isLoading: Boolean = false
)
