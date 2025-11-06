package com.example.tiendahuertohogar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * ViewModel para manejar la lógica de la pantalla de Historial de Pedidos.
 */
class HistorialPedidosViewModel(
    private val pedidoRepository: PedidoRepository // Depende del Repositorio de Pedidos
) : ViewModel() {

    // 2. Usar una clase "sealed" para representar el estado de la UI
    private val _state = MutableStateFlow<PedidoUIState>(PedidoUIState.Loading)
    val state: StateFlow<PedidoUIState> = _state.asStateFlow()

    /**
     * Carga la lista de pedidos para un ID de usuario específico.
     */
    fun obtenerPedidosPorUsuario(usuarioId: Long) {
        viewModelScope.launch {
            _state.value = PedidoUIState.Loading // Informa a la UI que estamos cargando
            pedidoRepository.getHistorialPedidos(usuarioId) // Llama al repositorio con el ID
                .catch { exception ->
                    // En caso de error, actualiza el estado a Error
                    _state.value = PedidoUIState.Error(exception.message ?: "Error desconocido")
                }
                .collect { pedidos ->
                    // Si tiene éxito, actualiza el estado con la lista de pedidos
                    _state.value = PedidoUIState.Success(pedidos)
                }
        }
    }
}

/**
 * Define los posibles estados de la UI para la pantalla de historial de pedidos.
 * ESTA CLASE TAMBIÉN FALTABA.
 */
sealed class PedidoUIState {
    object Loading : PedidoUIState()
    data class Success(val pedidos: List<Pedido>) : PedidoUIState()
    data class Error(val message: String) : PedidoUIState()
}