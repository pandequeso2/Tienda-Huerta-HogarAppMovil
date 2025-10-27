// data/dao/PedidoDAO.kt
package com.example.tiendahuertohogar.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction // Importante para la consistencia
import com.example.tiendahuertohogar.data.model.ItemPedido
import com.example.tiendahuertohogar.data.model.Pedido
import kotlinx.coroutines.flow.Flow

@Dao
interface PedidoDAO {

    // 1. CORREGIDO: El insert de un solo pedido. Devuelve el ID del nuevo pedido.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPedido(pedido: Pedido): Long

    // 2. NUEVO: Inserta una lista de ItemPedido.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItemsPedido(items: List<ItemPedido>)

    // 3. CORREGIDO: Aseguramos que el tipo de dato sea Long para consistencia.
    @Query("SELECT * FROM pedidos WHERE usuario_id = :usuarioId ORDER BY fecha DESC")
    fun getPedidosByUsuario(usuarioId: Long): Flow<List<Pedido>>

    // 4. NUEVO: Obtiene los items de un pedido específico.
    @Query("SELECT * FROM items_pedido WHERE pedido_id = :pedidoId")
    fun getItemsByPedido(pedidoId: Long): Flow<List<ItemPedido>>
}
