package com.example.tiendahuertohogar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Esta clase es la "Fábrica" que le enseña al sistema cómo crear
 * un HistorialPedidosViewModel.
 *
 * Es necesaria porque el ViewModel requiere un PedidoRepository en su constructor.
 */
class HistorialPedidosViewModelFactory(
    private val pedidoRepository: PedidoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Comprueba si la clase que se solicita es nuestro HistorialPedidosViewModel.
        if (modelClass.isAssignableFrom(HistorialPedidosViewModel::class.java)) {
            // Si lo es, lo creamos y le pasamos el repositorio.
            @Suppress("UNCHECKED_CAST")
            return HistorialPedidosViewModel(pedidoRepository) as T
        }
        // Si no, lanza un error.
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}