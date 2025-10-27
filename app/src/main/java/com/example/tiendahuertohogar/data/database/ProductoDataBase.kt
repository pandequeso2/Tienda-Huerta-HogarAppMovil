// data/database/ProductoDatabase.kt
package com.example.tiendahuertohogar.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tiendahuertohogar.data.dao.PedidoDAO
import com.example.tiendahuertohogar.data.dao.ProductoDAO
import com.example.tiendahuertohogar.data.dao.UsuarioDAO
import com.example.tiendahuertohogar.data.model.ItemPedido
import com.example.tiendahuertohogar.data.model.Pedido
import com.example.tiendahuertohogar.data.model.Producto
import com.example.tiendahuertohogar.data.model.Usuario

@Database(
    entities = [Producto::class, Usuario::class, Pedido::class, ItemPedido::class],
    version = 2,
    exportSchema = false
)
// La corrección está aquí: Apunta a tu propia clase Converters
@TypeConverters(Converters::class)
abstract class ProductoDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDAO
    abstract fun usuarioDao(): UsuarioDAO
    abstract fun pedidoDao(): PedidoDAO

    // Nota: ItemPedido no necesita su propio DAO si solo se accede a través de Pedido.
    // Si en el futuro necesitas consultas complejas sobre ItemPedido, puedes crear un DAO para él.
}
