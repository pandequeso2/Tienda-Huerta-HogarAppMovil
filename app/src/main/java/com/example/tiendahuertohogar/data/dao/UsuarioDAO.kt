// data/dao/UsuarioDAO.kt
package com.example.tiendahuertohogar.data.dao

import androidx.room.*
import com.example.tiendahuertohogar.data.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usuario: Usuario)

    // 1. NUEVO: Añadimos la función de actualización.
    @Update
    suspend fun update(usuario: Usuario)

    // 2. CORREGIDO: Cambiamos el tipo del ID a Long para que coincida con la entidad.
    @Query("SELECT * FROM usuarios WHERE id = :id")
    fun getUsuarioById(id: Long): Flow<Usuario?>

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun getUsuarioByEmail(email: String): Usuario?

    @Query("SELECT * FROM usuarios ORDER BY nombre ASC")
    fun getAllUsuarios(): Flow<List<Usuario>>
}
