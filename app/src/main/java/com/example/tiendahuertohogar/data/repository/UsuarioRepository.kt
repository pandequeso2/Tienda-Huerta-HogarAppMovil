// data/repository/UsuarioRepository.kt
package com.example.tiendahuertohogar.data.repository

import com.example.tiendahuertohogar.data.dao.UsuarioDAO
import com.example.tiendahuertohogar.data.model.Usuario
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject // 1. Importar Inject

// 2. Usar @Inject constructor para Hilt.
class UsuarioRepository @Inject constructor(private val usuarioDAO: UsuarioDAO) {

    /**
     * Obtiene un usuario específico por su ID como un flujo de datos.
     */
    fun getUsuario(id: Long): Flow<Usuario?> {
        // 3. Esta llamada ahora es correcta, el tipo Long coincide.
        return usuarioDAO.getUsuarioById(id)
    }

    /**
     * Actualiza los datos de un usuario en la base de datos.
     */
    suspend fun updateUsuario(usuario: Usuario) {
        // 4. Llamamos a la función 'update' que ahora sí existe en el DAO.
        usuarioDAO.update(usuario)
    }
}
