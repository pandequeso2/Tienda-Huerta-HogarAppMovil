package com.example.tiendahuertohogar.ui.perfil


import android.app.Application
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendahuertohogar.data.database.BaseDeDatos
import com.example.tiendahuertohogar.data.model.Usuario
import com.example.tiendahuertohogar.data.repository.UsuarioRepository
import com.example.tiendahuertohogar.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UsuarioRepository

    private val _user = MutableStateFlow<Usuario?>(null)
    val user: StateFlow<Usuario?> = _user.asStateFlow()

    private val _photoUri = MutableStateFlow<Uri?>(null)
    val photoUri: StateFlow<Uri?> = _photoUri.asStateFlow()

    init {
        val usuarioDao = BaseDeDatos.getDatabase(application).usuarioDao()
        repository = UsuarioRepository(usuarioDao)
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            val userEmail = SessionManager.getUserEmail(getApplication())
            userEmail?.let {
                repository.usuarioDao.obtenerUsuarioPorCorreo(it).collect { usuario ->
                    _user.value = usuario
                    usuario?.fotoUri?.let { uriString ->
                        _photoUri.value = uriString.toUri()
                    }
                }
            }
        }
    }

    fun updatePhotoUri(uri: Uri) {
        _photoUri.value = uri
        viewModelScope.launch {
            val currentUser = _user.value
            currentUser?.let {
                val updatedUser = it.copy(fotoUri = uri.toString())
                repository.usuarioDao.update(updatedUser)
            }
        }
    }
}