// data/database/ProductoDatabase.kt
package com.example.tiendahuertohogar.data.database

import android.content.Context // 1. Importar Context
import androidx.room.Database
import androidx.room.Room // 2. Importar Room
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
    version = 2, // Incrementar al cambiar el esquema
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ProductoDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDAO
    abstract fun usuarioDao(): UsuarioDAO
    abstract fun pedidoDao(): PedidoDAO

    // --- ESTA ES LA PARTE AÑADIDA (PATRÓN SINGLETON) ---
    companion object {
        // La anotación @Volatile asegura que la instancia sea siempre la más actualizada
        // y visible para todos los hilos de ejecución.
        @Volatile
        private var INSTANCE: ProductoDatabase? = null

        /**
         * Esta función obtiene la única instancia de la base de datos.
         * Si no existe, la crea de forma segura (sincronizada).
         */
        fun getDatabase(context: Context): ProductoDatabase {
            // Si la instancia ya existe, la devuelve.
            return INSTANCE ?: synchronized(this) {
                // Si no existe, construye la base de datos.
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductoDatabase::class.java,
                    "tienda_huerto_hogar_database" // Nombre del archivo de la base de datos
                )
                    .fallbackToDestructiveMigration() // Opción para desarrollo: borra y recrea la BD si la versión cambia.
                    // En producción, se debe usar .addMigrations(...)
                    .build()
                INSTANCE = instance
                // Devuelve la instancia recién creada.
                instance
            }
        }
    }
}
