// data/model/Producto.kt
package com.example.tiendahuertohogar.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// Añadimos un índice único para 'codigo' para evitar duplicados
@Entity(tableName = "productos",
    indices = [Index(value = ["codigo"], unique = true)])
data class Producto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val codigo: String, // CAMPO AÑADIDO
    val nombre: String,
    val descripcion: String,
    val categoria: String, // CAMPO AÑADIDO
    val precio: Double,
    val stock: Int,
    val imagenUrl: String?
)
