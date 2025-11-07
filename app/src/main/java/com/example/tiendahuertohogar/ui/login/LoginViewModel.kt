// en ui/login/LoginViewModel.kt
package com.example.tiendahuertohogar.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.tiendahuertohogar.data.database.BaseDeDatos
import com.example.tiendahuertohogar.data.repository.UsuarioRepository
import com.example.tiendahuertohogar.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UsuarioRepository

    init {
        val usuarioDao = BaseDeDatos.getDatabase(application).usuarioDao()
        repository = UsuarioRepository(usuarioDao)
    }

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onCorreoChange(value: String) {
        uiState = uiState.copy(correo = value, mensaje = "")
    }

    fun onClaveChange(value: String) {
        uiState = uiState.copy(clave = value, mensaje = "")
    }

    fun submit(onSuccess: (String) -> Unit) {
        uiState = uiState.copy(isLoading = true, mensaje = "")

        viewModelScope.launch(Dispatchers.IO) {
            val user = repository.buscarUsuario(
                uiState.correo.trim(),
                uiState.clave
            )

            launch(Dispatchers.Main) {
                uiState = uiState.copy(isLoading = false)
                if (user != null) {
                    // Guardamos la sesión del usuario
                    SessionManager.saveSession(getApplication(), user.nombreCompleto)
                    SessionManager.saveUserEmail(getApplication(), user.correo)
                    // Si encontramos el usuario, navegamos. Pasamos el nombre completo.
                    onSuccess(user.nombreCompleto)
                } else {
                    uiState = uiState.copy(mensaje = "Correo o clave incorrectos")
                }
            }
        }
    }
}
