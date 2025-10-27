package com.example.tiendahuertohogar.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "pedidos",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            // Corregido: Apuntamos a "id", que es la PK real en la tabla de usuarios
            parentColumns = ["id"],
            childColumns = ["usuario_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class Pedido(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "usuario_id", index = true)
    val usuarioId: Long,

    val fecha: Date,
    val estado: String,
    val total: Double
)
    