package com.example.tiendahuertohogar.data.repository

import com.example.tiendahuertohogar.data.model.Credential


class AuthRepository(
    private val validCredential: Credential = Credential.Admin
) {
    fun login(username: String, password: String): Boolean {
        return username == validCredential.username && password == validCredential.password
    }
}