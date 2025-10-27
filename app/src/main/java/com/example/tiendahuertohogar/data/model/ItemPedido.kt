// data/model/ItemPedido.kt
package com.example.tiendahuertohogar.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "items_pedido",
    foreignKeys = [
        ForeignKey(
            entity = Pedido::class,
            // Asume que la PK en Pedido es 'idPedido'. ¡Verifica esto!
            parentColumns = ["idPedido"],
            childColumns = ["pedido_id"],
            onDelete = ForeignKey.CASCADE // Si se borra un pedido, se borran sus items.
        ),
        ForeignKey(
            entity = Producto::class,
            // Asume que la PK en Producto es 'id'. ¡Verifica esto!
            parentColumns = ["id"],
            childColumns = ["producto_id"],
            onDelete = ForeignKey.RESTRICT // No permite borrar un producto si está en un pedido.
        )
    ]
)
data class ItemPedido(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // Usar @ColumnInfo mejora la claridad y evita conflictos con nombres de variables.
    @ColumnInfo(name = "pedido_id", index = true)
    val pedidoId: Long,

    @ColumnInfo(name = "producto_id", index = true)
    val productoId: Long,

    val cantidad: Int,

    val precioUnitario: Double
)
