package com.example.tiendahuertohogar.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters // 1. IMPORTA ESTO
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tiendahuertohogar.data.dao.ProductoDAO
import com.example.tiendahuertohogar.data.dao.UsuarioDAO
import com.example.tiendahuertohogar.data.dao.PedidoDAO
import com.example.tiendahuertohogar.data.model.ItemPedido
import com.example.tiendahuertohogar.data.model.Pedido
import com.example.tiendahuertohogar.data.model.Producto
import com.example.tiendahuertohogar.data.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Asegúrate de listar todas tus @Entity aquí
@Database(
    entities = [Usuario::class, Producto::class, Pedido::class, ItemPedido::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class) // 2. AÑADE ESTA LÍNEA
abstract class ProductoDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDAO
    abstract fun productoDao(): ProductoDAO
    abstract fun pedidoDao(): PedidoDAO

    companion object {
        @Volatile
        private var INSTANCE: ProductoDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): ProductoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductoDatabase::class.java,
                    "producto_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(ProductoDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    // (El resto de tu clase ProductoDatabaseCallback va aquí...)
    private class ProductoDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    prePoblarProductos(database.productoDao())
                }
            }
        }

        suspend fun prePoblarProductos(productoDao: ProductoDAO) {
            productoDao.deleteAll()

            val listaProductos = listOf(
                Producto(
                    codigo = "FR001",
                    nombre = "Manzana Fuji",
                    descripcion = "Manzanas Fuji crujientes y dulces, cultivadas en el Valle del Maule.",
                    categoria = "Frutas Frescas",
                    precio = 1200.0,
                    stock = 150,
                    imagenUrl = "manzana_fuji"
                ),
                Producto(
                    codigo = "FR002",
                    nombre = "Naranjas Valencia",
                    descripcion = "Jugosas y ricas en vitamina C, estas naranjas Valencia son ideales para zumos frescos.",
                    categoria = "Frutas Frescas",
                    precio = 1000.0,
                    stock = 200,
                    imagenUrl = "naranja"
                ),
                Producto(
                    codigo = "FR003",
                    nombre = "Plátanos Cavendish",
                    descripcion = "Plátanos maduros y dulces, perfectos para el desayuno o como snack energético.",
                    categoria = "Frutas Frescas",
                    precio = 800.0,
                    stock = 250,
                    imagenUrl = "platano"
                ),
                Producto(
                    codigo = "VR001",
                    nombre = "Zanahorias Orgánicas",
                    descripcion = "Zanahorias crujientes cultivadas sin pesticidas en la Región de O'Higgins.",
                    categoria = "Verduras Orgánicas",
                    precio = 900.0,
                    stock = 100,
                    imagenUrl = "zanahoria"
                ),
                Producto(
                    codigo = "VR002",
                    nombre = "Espinacas Frescas",
                    descripcion = "Espinacas frescas y nutritivas, perfectas para ensaladas y batidos verdes.",
                    categoria = "Verduras Orgánicas",
                    precio = 700.0,
                    stock = 80,
                    imagenUrl = "espinaca"
                ),
                Producto(
                    codigo = "VR003",
                    nombre = "Pimientos Tricolores",
                    descripcion = "Pimientos rojos, amarillos y verdes, ideales para salteados y platos coloridos.",
                    categoria = "Verduras Orgánicas",
                    precio = 1500.0,
                    stock = 120,
                    imagenUrl = "pimiento"
                ),
                Producto(
                    codigo = "PO001",
                    nombre = "Miel Orgánica",
                    descripcion = "Miel pura y orgánica producida por apicultores locales.",
                    categoria = "Productos Orgánicos",
                    precio = 5000.0,
                    stock = 50,
                    imagenUrl = "miel"
                )
            )

            productoDao.insertAll(listaProductos)
        }
    }
}