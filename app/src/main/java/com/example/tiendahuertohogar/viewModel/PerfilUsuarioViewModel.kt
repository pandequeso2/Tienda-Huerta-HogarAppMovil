// viewmodel/PerfilUsuarioViewModel.kt
package com.example.tiendahuertohogar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendahuertohogar.data.model.Usuario
import com.example.tiendahuertohogar.data.repository.UsuarioRepository
// 1. Quitar las importaciones de Hilt
// import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
// import javax.inject.Inject

// La clase para el estado de la UI (PerfilUiState) es perfecta y no necesita cambios.
data class PerfilUiState(
    val usuario: Usuario? = null,
    val isLoading: Boolean = false,
    val error: String? = null, // Renombré 'message' a 'error' para claridad en el estado de carga
    val successMessage: String? = null // Añadí un campo específico para mensajes de éxito
)

// 2. Quitar las anotaciones @HiltViewModel y @Inject
class PerfilUsuarioViewModel constructor(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    // Carga el perfil del usuario desde la base de datos
    fun cargarPerfil(usuarioId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            usuarioRepository.getUsuario(usuarioId)
                .catch { exception ->
                    // En caso de error en el flujo, actualizamos el estado
                    _uiState.update {
                        it.copy(isLoading = false, error = "Error al cargar el perfil: ${exception.message}")
                    }
                }
                .collect { usuario ->
                    // Cuando el flujo emite un usuario, actualizamos el estado
                    _uiState.update {
                        it.copy(isLoading = false, usuario = usuario)
                    }
                }
        }
    }

    // Actualiza los datos del usuario en la base de datos
    fun actualizarPerfil(usuario: Usuario) {
        viewModelScope.launch {
            try {
                usuarioRepository.updateUsuario(usuario)
                _uiState.update { it.copy(successMessage = "Perfil actualizado con éxito") }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error al actualizar el perfil: ${e.message}") }
            }
        }
    }

    // Limpia el mensaje de éxito para que no se muestre repetidamente (ej. en un Snackbar)
    fun successMessageShown() {
        _uiState.update { it.copy(successMessage = null) }
    }

    // Limpia el mensaje de error
    fun errorMessageShown() {
        _uiState.update { it.copy(error = null) }
    }
}
