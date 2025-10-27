// data/repository/AuthRepository.kt
package com.example.tiendahuertohogar.data.repository

import com.example.tiendahuertohogar.data.model.Credential
import kotlinx.coroutines.delay

class AuthRepository {
    // Simula una llamada a una API o a una base de datos de usuarios
    suspend fun login(credential: Credential): Boolean {
        delay(1000) // Simula la latencia de red
        return credential.email == "admin@huertohogar.cl" && credential.pass == "1234"
    }
}