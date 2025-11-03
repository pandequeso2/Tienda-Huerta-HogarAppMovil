  // viewModel/HistorialPedidosViewModel.kt
package com.example.tiendahuertohogar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendahuertohogar.data.model.Pedido
import com.example.tiendahuertohogar.data.repository.PedidoRepository // Necesitarás crear este repositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

// 1. Quitar anotaciones de Hilt (@HiltViewModel, @Inject)
class HistorialPedidosViewModel(
    private val pedidoRepository: PedidoRepository // Mantenemos la dependencia del repositorio
) : ViewModel() {

    // 2. Usar una clase "sealed" para representar el estado de la UI
    private val _state = MutableStateFlow<PedidoUIState>(PedidoUIState.Loading)
    val state: StateFlow<PedidoUIState> = _state.asStateFlow()

    // 3. Crear una función para cargar pedidos basada en un ID dinámico
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

// 4. Definir los posibles estados de la UI para que la pantalla sepa qué dibujar
sealed class PedidoUIState {
    object Loading : PedidoUIState()
    data class Success(val pedidos: List<Pedido>) : PedidoUIState()
    data class Error(val message: String) : PedidoUIState()
}
