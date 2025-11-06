// data/repository/UsuarioRepository.kt
package com.example.tiendahuertohogar.data.repository
import com.example.tiendahuertohogar.data.dao.UsuarioDao
import com.example.tiendahuertohogar.data.model.Usuario



class UsuarioRepository(val usuarioDao: UsuarioDao) {
    suspend fun insertarUsuario(usuario: Usuario) {
        usuarioDao.insertarUsuario(usuario)
    }

    suspend fun buscarUsuario(correo: String, contrasena: String): Usuario
    ? {
        return usuarioDao.findUserByEmailAndPassword(correo, contrasena)
    }
}
