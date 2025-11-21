package com.example.tiendahuertohogar.data.repository

import com.example.tiendahuertohogar.data.model.Post
import com.example.tiendahuertohogar.data.remote.RetrofitInstance


// Este repositorio se encarga de acceder a los datos usando Retrofit
class PostRepository {

    // Función que obtiene los posts desde la API
    suspend fun getPosts(): List<Post> {
        return RetrofitInstance.api.getPosts()
    }
}
