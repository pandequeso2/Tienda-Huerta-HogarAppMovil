package com.example.tiendahuertohogar.ui.login

data class LoginUiStatd(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val loginSuccess: Boolean = false
)
