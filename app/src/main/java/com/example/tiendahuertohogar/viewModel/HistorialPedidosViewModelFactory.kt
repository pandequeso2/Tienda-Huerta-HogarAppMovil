package com.example.tiendahuertohogar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tiendahuertohogar.data.repository.PedidoRepository

/**
 * Esta clase es una "Fábrica" que le enseña al sistema cómo crear una instancia
 * de HistorialPedidosViewModel. Es necesaria porque el ViewModel ahora requiere
 * una dependencia (PedidoRepository) en su constructor.
 *
 * Reemplaza la funcionalidad de inyección automática que proveía Hilt.
 */
class HistorialPedidosViewModelFactory(
    private val pedidoRepository: PedidoRepository
) : ViewModelProvider.Factory {

    /**
     * El sistema llama a este método cuando necesita crear un ViewModel.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Comprueba si la clase que se solicita es nuestro HistorialPedidosViewModel.
        if (modelClass.isAssignableFrom(HistorialPedidosViewModel::class.java)) {
            // Si lo es, lo creamos, le pasamos el repositorio que nos dieron en el constructor,
            // y lo devolvemos casteado al tipo genérico T.
            @Suppress("UNCHECKED_CAST")
            return HistorialPedidosViewModel(pedidoRepository) as T
        }
        // Si el sistema intenta usar esta fábrica para crear cualquier otro ViewModel,
        // lanzamos un error para indicar que esta fábrica no sabe cómo hacerlo.
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
