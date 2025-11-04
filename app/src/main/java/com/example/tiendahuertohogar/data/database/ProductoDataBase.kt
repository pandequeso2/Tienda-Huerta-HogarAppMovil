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

        /**
         * Obtiene la instancia Singleton de la base de datos.
         * Coincide con la firma `getDatabase(context, scope)` que usan tus vistas.
         */
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
                        //    (El login de "admin@huertohogar.cl" es simulado y no usa la BD)
                        val usuarioPrueba = Usuario(
                            id = 1L, // ID 1 para asociar a pedidos de prueba
                            nombre = "Usuario de Prueba",
                            email = "test@huerto.cl",
                            direccion = "Avenida Siempreviva 742",
                            telefono = "+56912345678"
                        )
                        usuarioDao.insert(usuarioPrueba)

                        // 3. Crear Productos iniciales
                        val productosIniciales = listOf(
                            Producto(
                                codigo = "FR001",
                                nombre = "Manzana Fuji (Ejemplo)",
                                descripcion = "Manzana roja dulce y crujiente, ideal para comer sola.",
                                categoria = "Frutas Frescas",
                                precio = 1290.0,
                                stock = 100,
                                imagenUrl = "manzana_fuji" // (Debes tener un drawable llamado "manzana_fuji.png" o similar)
                            ),
                            Producto(
                                codigo = "VR001",
                                nombre = "Lechuga Costina (Ejemplo)",
                                descripcion = "Lechuga fresca y orgánica para tus ensaladas.",
                                categoria = "Verduras Orgánicas",
                                precio = 890.0,
                                stock = 50,
                                imagenUrl = "lechuga_costina" // (drawable "lechuga_costina.png")
                            ),
                            Producto(
                                codigo = "OR001",
                                nombre = "Huevos Orgánicos (Ejemplo)",
                                descripcion = "Docena de huevos de gallina feliz, de libre pastoreo.",
                                categoria = "Productos Orgánicos",
                                precio = 3500.0,
                                stock = 30,
                                imagenUrl = "huevos_organicos" // (drawable "huevos_organicos.png")
                            )
                        )

                        // 4. Insertar todos los productos
                        productoDao.insertAll(productosIniciales)

                        // --- FIN DE DATOS INICIALES ---
                    }
                }
            }
        }
    }
}

