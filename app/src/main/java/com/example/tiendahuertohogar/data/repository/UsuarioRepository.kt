// data/repository/UsuarioRepository.kt
package com.example.tiendahuertohogar.data.repository

import com.example.tiendahuertohogar.data.dao.UsuarioDAO
import com.example.tiendahuertohogar.data.model.Usuario
import kotlinx.coroutines.flow.Flow
// 1. Quitar la importación de Hilt, ya no es necesaria.
// import javax.inject.Inject

// 2. Quitar la anotación @Inject del constructor.
class UsuarioRepository constructor(private val usuarioDAO: UsuarioDAO) {

    /**
     * Obtiene un usuario específico por su ID como un flujo de datos.
     * Ideal para observar cambios en el perfil del usuario.
     */
    fun getUsuario(id: Long): Flow<Usuario?> {
        return usuarioDAO.getUsuarioById(id)
    }

    /**
     * Actualiza los datos de un usuario en la base de datos.
     * Es una función suspendida, por lo que debe ser llamada desde una corrutina.
     */
    suspend fun updateUsuario(usuario: Usuario) {
        usuarioDAO.update(usuario)
    }

    /**
     * Inserta un nuevo usuario en la base de datos.
     * Útil para la pantalla de registro.
     */
    suspend fun insertUsuario(usuario: Usuario) {
        usuarioDAO.insert(usuario)
    }

    /**
     * Busca un usuario por su email.
     * Función clave para la lógica de inicio de sesión y para comprobar si un email ya está registrado.
     */
    suspend fun getUsuarioByEmail(email: String): Usuario? {
        return usuarioDAO.getUsuarioByEmail(email)
    }
}
