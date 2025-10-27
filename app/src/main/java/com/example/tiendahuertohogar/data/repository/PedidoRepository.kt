// data/repository/PedidoRepository.kt
package com.example.tiendahuertohogar.data.repository

import androidx.room.Transaction
import com.example.tiendahuertohogar.data.dao.PedidoDAO
import com.example.tiendahuertohogar.data.model.ItemPedido
import com.example.tiendahuertohogar.data.model.Pedido
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject // 1. Importar Inject

// 2. Usar @Inject constructor para que Hilt lo gestione.
class PedidoRepository @Inject constructor(private val pedidoDAO: PedidoDAO) {

    /**
     * Obtiene el historial de pedidos de un usuario como un flujo de datos.
     */
    fun getHistorialPedidos(usuarioId: Long): Flow<List<Pedido>> {
        // Ahora esta llamada es correcta porque la firma en el DAO coincide.
        return pedidoDAO.getPedidosByUsuario(usuarioId)
    }

    /**
     * Obtiene los items (el detalle) de un pedido específico como un flujo de datos.
     */
    fun getDetallePedido(pedidoId: Long): Flow<List<ItemPedido>> {
        // Ahora esta llamada es correcta porque la función existe en el DAO.
        return pedidoDAO.getItemsByPedido(pedidoId)
    }

    /**
     * Crea un nuevo pedido con sus items en una única transacción.
     * La anotación @Transaction asegura que si algo falla, no se guarda nada.
     */
    @Transaction
    suspend fun crearNuevoPedido(pedido: Pedido, items: List<ItemPedido>) {
        // 3. La función insertPedido ahora devuelve el ID (Long) generado.
        val pedidoId = pedidoDAO.insertPedido(pedido)

        // 4. Asigna el ID del pedido recién creado a cada ítem.
        val itemsConId = items.map { it.copy(pedidoId = pedidoId) }

        // 5. Inserta todos los items en la base de datos.
        pedidoDAO.insertItemsPedido(itemsConId)
    }
}
