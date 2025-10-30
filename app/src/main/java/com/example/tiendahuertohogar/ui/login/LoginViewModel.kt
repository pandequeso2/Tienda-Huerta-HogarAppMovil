// en ui/login/LoginViewModel.kt
package com.example.tiendahuertohogar.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// Importa tu repositorio si no lo has hecho
import com.example.tiendahuertohogar.data.repository.AuthRepository
import com.example.tiendahuertohogar.data.model.Credential // Importa Credential
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Tu LoginUiState se mantiene igual, está perfecto
data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val loginSuccess: Boolean = false
)

class LoginViewModel : ViewModel() {

    // --- 1. CREA UNA INSTANCIA DEL REPOSITORIO ---
    private val authRepository = AuthRepository()

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    // --- 2. FUNCIÓN PARA MANEJAR CAMBIOS EN LOS TEXTFIELDS ---
    fun onLoginChanged(username: String, pass: String) {
        _uiState.update { currentState ->
            currentState.copy(username = username, password = pass)
        }
    }

    // --- 3. MODIFICA onLogin PARA QUE USE EL ESTADO INTERNO ---
    fun onLogin() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // Usa los valores del estado actual
            val credential = Credential(_uiState.value.username, _uiState.value.password)
            val loginExitoso = authRepository.login(credential)

            if (loginExitoso) {
                _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Credenciales inválidas") }
            }
        }
    }
}
