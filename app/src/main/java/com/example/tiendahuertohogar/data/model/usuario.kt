package com.example.tiendahuertohogar.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombreCompleto: String,
    val fechaNacimiento: String,
    val correo: String,
    val contrasena: String,
    val region: String,
    val comuna: String,
    val fotoUri: String? = null,

    // --- Campos Opcionales del Formulario ---
    val telefono: String?,
    val codigoDescuento: String?
)
