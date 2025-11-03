package com.example.tiendahuertohogar.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendahuertohogar.data.model.QrData
import com.example.tiendahuertohogar.ui.login.LoginViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow



class QrViewModel : ViewModel() {


    // --- CORRECCIÓN CLAVE ---
    // Especifica el tipo de dato que contendrá el StateFlow.
    // Debe ser anulable (QrData?) para representar que al inicio no hay resultado.
    private val _qrResult = MutableStateFlow<QrData?>(null)
    val qrResult = _qrResult.asStateFlow()

    fun onQrDetected(qrContent: String) {
        // Aquí parseas el contenido del QR.
        // Este es un ejemplo simple, ajústalo a tu formato real.
        // Ejemplo de formato esperado: "nombre=Producto A,precio=123.45"
        try {
            val parts = qrContent.split(",")
            val nombre = parts.find { it.startsWith("nombre=") }?.substringAfter("=") ?: "N/A"
            val precio = parts.find { it.startsWith("precio=") }?.substringAfter("=") ?: "0"

            _qrResult.value = QrData(nombre = nombre, precio = precio)

        } catch (e: Exception) {
            // Maneja el caso en que el QR no tenga el formato esperado
            _qrResult.value = QrData(nombre = "Error de formato", precio = "0")
        }
    }

    // Función para limpiar el resultado y evitar navegaciones múltiples
    fun clearResult() {
        _qrResult.value = null
    }


}
