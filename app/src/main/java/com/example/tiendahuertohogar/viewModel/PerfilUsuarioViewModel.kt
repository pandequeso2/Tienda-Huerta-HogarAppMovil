// viewmodel/PerfilUsuarioViewModel.kt
package com.example.tiendahuertohogar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendahuertohogar.data.model.Usuario
import com.example.tiendahuertohogar.data.repository.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// Clase para representar el estado de la UI
data class PerfilUiState(
    val usuario: Usuario? = null,
    val isLoading: Boolean = false,
    val message: String? = null
)

@HiltViewModel
class PerfilUsuarioViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    // Carga el perfil del usuario desde la base de datos
    fun cargarPerfil(usuarioId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            usuarioRepository.getUsuario(usuarioId).collect { usuario ->
                _uiState.update {
                    it.copy(isLoading = false, usuario = usuario)
                }
            }
        }
    }

    // Actualiza los datos del usuario en la base de datos
    fun actualizarPerfil(usuario: Usuario) {
        viewModelScope.launch {
            usuarioRepository.updateUsuario(usuario)
            _uiState.update { it.copy(message = "Perfil actualizado con éxito") }
        }
    }

    // Limpia el mensaje para que no se muestre repetidamente
    fun messageShown() {
        _uiState.update { it.copy(message = null) }
    }
}
