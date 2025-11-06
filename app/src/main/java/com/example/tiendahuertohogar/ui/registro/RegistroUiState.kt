package com.example.tiendahuertohogar.ui.registro

// Gestión de Estado y Validación: Esta data class encapsula todo el estado del formulario de registro.
// Representa el patrón "UI State". Contiene tanto los valores de los campos como los posibles
// mensajes de error, permitiendo un flujo de datos unidireccional desde el ViewModel hacia la UI.
data class RegistroUiState(
    val nombreCompleto: String = "",
    val errorNombre: String? = null,
    val fechaNacimiento: String = "",
    val correo: String = "",
    val errorCorreo: String? = null,
    val contrasena: String = "",
    val errorContrasena: String? = null,
    val confirmarContrasena: String = "",
    val errorConfirmarContrasena: String? = null,
    val telefono: String = "",
    val region: String = "",
    val comuna: String = "",
    val codigoDescuento: String = "",
    val isLoading: Boolean = false,
    val registroExitoso: Boolean = false
)