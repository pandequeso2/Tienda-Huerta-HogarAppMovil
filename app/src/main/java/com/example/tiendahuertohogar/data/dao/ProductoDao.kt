package com.example.tiendahuertohogar.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.tiendahuertohogar.data.model.Producto
import kotlinx.coroutines.flow.Flow

@Dao

interface ProductoDao{
    @Insert
    suspend fun insertarProducto(producto: Producto)

    @Query("SELECT * FROM productos")
    fun obtenerProductos(): Flow<List<Producto>>

    @Query("SELECT * FROM productos WHERE id = :id")
    fun obtenerProductoPorId(id: Int): Flow<Producto>

    @Query("DELETE FROM productos WHERE id = :id")
    suspend fun eliminarProductoPorId(id: Int)

    @Query("DELETE FROM productos")
    suspend fun eliminarTodosLosProductos()

    // Si quieres una función específica de actualización:
    @Update
    suspend fun actualizarProducto(producto: Producto)

}