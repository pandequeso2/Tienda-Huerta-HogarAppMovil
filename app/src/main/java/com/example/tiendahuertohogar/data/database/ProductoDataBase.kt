package com.example.tiendahuertohogar.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tiendahuertohogar.data.dao.PedidoDAO
import com.example.tiendahuertohogar.data.dao.ProductoDAO
import com.example.tiendahuertohogar.data.dao.UsuarioDAO
import com.example.tiendahuertohogar.data.model.ItemPedido
import com.example.tiendahuertohogar.data.model.Pedido
import com.example.tiendahuertohogar.data.model.Producto
import com.example.tiendahuertohogar.data.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [Usuario::class, Producto::class, Pedido::class, ItemPedido::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ProductoDataBase : RoomDatabase() {

    // DAOs abstractos que la base de datos proveerá
    abstract fun usuarioDao(): UsuarioDAO
    abstract fun productoDao(): ProductoDAO
    abstract fun pedidoDao(): PedidoDAO

    companion object {
        @Volatile
        private var INSTANCE: ProductoDataBase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ProductoDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductoDataBase::class.java,
                    "huerto_hogar_database"
                )
                    .addCallback(ProductoDatabaseCallback(scope)) // Usa el scope para el callback
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        /**
         * Callback para poblar la base de datos cuando se crea por primera vez.
         * AÑADIMOS TUS PRODUCTOS AQUÍ.
         */
        private class ProductoDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        // --- INICIO DE DATOS INICIALES ---

                        // 1. Obtener DAOs
                        val usuarioDao = database.usuarioDao()
                        val productoDao = database.productoDao()

                        // 2. Crear Usuario de prueba (para el Perfil y Pedidos)
                        val usuarioPrueba = Usuario(
                            id = 1L, // ID 1 para asociar a pedidos
                            nombre = "Usuario de Prueba",
                            email = "test@huerto.cl",
                            direccion = "Avenida Siempreviva 742",
                            telefono = "+56912345678"
                        )
                        usuarioDao.insert(usuarioPrueba)

                        // 3. Crear Productos iniciales (basados en tu snippet e imagen)
                        val listaProductos = listOf(
                            Producto(
                                codigo = "FR001",
                                nombre = "Manzana Fuji", // De tu imagen
                                descripcion = "Manzanas Fuji crujientes y dulces, cultivadas en el Valle del Maule.",
                                categoria = "Frutas Frescas",
                                precio = 1290.0,
                                stock = 150,
                                imagenUrl = "manzana_fuji" // De tu imagen
                            ),
                            Producto(
                                codigo = "VR001",
                                nombre = "Espinaca",
                                descripcion = "Hojas de espinaca fresca, listas para ensalada o cocinar.",
                                categoria = "Verduras Orgánicas",
                                precio = 990.0,
                                stock = 80,
                                imagenUrl = "espinaca" // De tu imagen
                            ),
                            Producto(
                                codigo = "VR002",
                                nombre = "Zanahoria",
                                descripcion = "Zanahorias dulces y crujientes, 1kg.",
                                categoria = "Verduras Orgánicas",
                                precio = 850.0,
                                stock = 120,
                                imagenUrl = "zanahoria" // De tu imagen
                            ),
                            Producto(
                                codigo = "FR002",
                                nombre = "Plátano",
                                descripcion = "Plátanos maduros, fuente de potasio. 1kg.",
                                categoria = "Frutas Frescas",
                                precio = 1100.0,
                                stock = 200,
                                imagenUrl = "platano" // De tu imagen
                            ),
                            Producto(
                                codigo = "FR003",
                                nombre = "Naranja",
                                descripcion = "Naranjas jugosas para exprimir, 1kg.",
                                categoria = "Frutas Frescas",
                                precio = 1050.0,
                                stock = 100,
                                imagenUrl = "naranja" // De tu imagen
                            ),
                            Producto(
                                codigo = "VR003",
                                nombre = "Pimiento",
                                descripcion = "Pimiento morrón rojo fresco.",
                                categoria = "Verduras Orgánicas",
                                precio = 700.0,
                                stock = 70,
                                imagenUrl = "pimiento" // De tu imagen
                            ),
                            Producto(
                                codigo = "OR001",
                                nombre = "Miel",
                                descripcion = "Miel de abeja 100% natural, 500g.",
                                categoria = "Productos Orgánicos",
                                precio = 4500.0,
                                stock = 50,
                                imagenUrl = "miel" // De tu imagen
                            )
                        )

                        // 4. Insertar todos los productos
                        productoDao.insertAll(listaProductos)

                        // --- FIN DE DATOS INICIALES ---
                    }
                }
            }
        }
    }
}