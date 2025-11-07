package com.example.tiendahuertohogar.data.repository
import com.example.tiendahuertohogar.data.model.QrResult

class QrRepository {
    fun processQrContent(content: String): QrResult {
        // Aquí podrías guardar o procesar el QR en una BD o API
        return QrResult(content)
    }
}