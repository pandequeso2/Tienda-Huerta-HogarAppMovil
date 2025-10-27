// data/dao/ProductoDAO.kt
package com.example.tiendahuertohogar.data.dao

import androidx.room.*
import com.example.tiendahuertohogar.data.model.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDAO {
    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    fun getAll(): Flow<List<Producto>>

    @Query("SELECT * FROM productos WHERE id = :id")
    fun findById(id: Long): Flow<Producto?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(producto: Producto)

    @Update
    suspend fun update(producto: Producto)

    @Delete
    suspend fun delete(producto: Producto)
}