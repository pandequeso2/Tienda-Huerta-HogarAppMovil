package com.example.tiendahuertohogar.data.remote

import com.example.tiendahuertohogar.data.model.Producto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductoApiService {

    // Obtener todos los productos
    @GET("productos")
    suspend fun obtenerProductos(): List<Producto>

    // Obtener un producto específico por ID
    @GET("productos/{id}")
    suspend fun obtenerProductoPorId(@Path("id") id: Int): Response<Producto>

    // Crear un nuevo producto
    @POST("productos")
    suspend fun crearProducto(@Body producto: Producto): Response<Producto>

    // Actualizar un producto existente
    @PUT("productos/{id}")
    suspend fun actualizarProducto(@Path("id") id: Int, @Body producto: Producto): Response<Producto>

    // Eliminar un producto
    @DELETE("productos/{id}")
    suspend fun eliminarProducto(@Path("id") id: Int): Response<Unit>
}
