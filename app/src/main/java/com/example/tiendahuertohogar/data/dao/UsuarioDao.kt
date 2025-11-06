// data/dao/UsuarioDao.kt
package com.example.tiendahuertohogar.data.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.tiendahuertohogar.data.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarUsuario(usuario: Usuario)

    @Update
    suspend fun update(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    fun obtenerUsuarioPorCorreo(correo: String): Flow<Usuario?>

    @Query("SELECT * FROM usuarios WHERE id = :id")
    fun obtenerUsuarioPorId(id: Int): Flow<Usuario?>

    @Query("SELECT * FROM usuarios WHERE correo = :correo AND contrasena = :contrasena LIMIT 1")
    suspend fun findUserByEmailAndPassword(correo: String, contrasena: String): Usuario?
}