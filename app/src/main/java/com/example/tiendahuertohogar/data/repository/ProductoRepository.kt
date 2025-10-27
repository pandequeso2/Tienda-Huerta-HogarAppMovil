// data/repository/ProductoRepository.kt
package com.example.tiendahuertohogar.data.repository

import com.example.tiendahuertohogar.data.dao.ProductoDAO
import com.example.tiendahuertohogar.data.model.Producto
import kotlinx.coroutines.flow.Flow

class ProductoRepository(private val productoDAO: ProductoDAO) {
    val allProductos: Flow<List<Producto>> = productoDAO.getAll()

    suspend fun insert(producto: Producto) {
        productoDAO.insert(producto)
    }

    suspend fun update(producto: Producto) {
        productoDAO.update(producto)
    }

    suspend fun delete(producto: Producto) {
        productoDAO.delete(producto)
    }

    fun findById(id: Long): Flow<Producto?> {
        return productoDAO.findById(id)
    }
}