// viewmodel/PerfilUsuarioViewModelFactory.kt
package com.example.tiendahuertohogar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tiendahuertohogar.data.repository.UsuarioRepository

/**
 * Fábrica que le enseña al sistema cómo crear una instancia de PerfilUsuarioViewModel,
 * ya que este requiere una dependencia (UsuarioRepository) en su constructor.
 */
class PerfilUsuarioViewModelFactory(
    private val usuarioRepository: UsuarioRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Comprueba si la clase que se solicita es nuestro PerfilUsuarioViewModel.
        if (modelClass.isAssignableFrom(PerfilUsuarioViewModel::class.java)) {
            // Si lo es, lo creamos y le pasamos el repositorio.
            @Suppress("UNCHECKED_CAST")
            return PerfilUsuarioViewModel(usuarioRepository) as T
        }
        // Si no, lanza un error.
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
