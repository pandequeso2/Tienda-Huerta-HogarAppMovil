// viewmodel/HistorialPedidosViewModel.kt
package com.example.tiendahuertohogar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendahuertohogar.data.model.Pedido
import com.example.tiendahuertohogar.data.repository.PedidoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HistorialPedidosViewModel @Inject constructor(
    pedidoRepository: PedidoRepository
) : ViewModel() {

    // TODO: El ID del usuario debe obtenerse de una sesión de login, no estar fijo.
    private val usuarioIdLogueado = 1L // Por ahora, usamos un ID de ejemplo.

    // El ViewModel expone un StateFlow que la UI puede observar.
    // Se actualiza automáticamente gracias a que el DAO devuelve un Flow.
    val historialPedidos: StateFlow<List<Pedido>> = pedidoRepository.getHistorialPedidos(usuarioIdLogueado)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList() // Empieza con una lista vacía mientras carga.
        )
}
