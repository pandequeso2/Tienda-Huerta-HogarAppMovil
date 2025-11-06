// data/repository/AuthRepository.kt
package com.example.tiendahuertohogar.data.repository
import com.example.tiendahuertohogar.data.model.Credential

class AuthRepository(
    private val credentials: List<Credential> = listOf(
        Credential.Admin,
        Credential.Cliente
    )
) {
    fun login(correo: String, clave: String): Credential? {
        return credentials.find {
            it.username == correo && it.password == clave
        }
    }
}
