// data/model/Producto.kt
package com.example.tiendahuertohogar.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val codigo: String,
    val categoria: String,
    val nombre: String,
    val precio: Double,
    val descripcion: String,
    val personalizable: Boolean,
    val imagenResId: Int = 0
    // En la creacion del modelo se debe de dar una imagen por defecto  para que Se puda utilizar si no no se tiene la imagen
)
