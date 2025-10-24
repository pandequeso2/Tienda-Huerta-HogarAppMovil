package com.example.tiendahuertohogar.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tiendahuertohogar.data.dao.ProductoDao
import com.example.tiendahuertohogar.data.model.Producto

@Database(
    entities = [Producto::class],
    version=1,
    exportSchema = false // Agregar para evitar el warning
)
abstract class ProductoDatabase: RoomDatabase(){
    abstract fun productoDao(): ProductoDao

    companion object{
        @Volatile
        private var INSTANCE: ProductoDatabase?=null
        fun getDatabase(context: Context): ProductoDatabase{
            return INSTANCE?: synchronized(this){
                val instance= Room.databaseBuilder(
                    context.applicationContext,
                    ProductoDatabase::class.java,
                    "producto_database"
                ).build() // fin Room
                INSTANCE=instance
                instance

            }//fin return
        }// fin getDatabase

    }// fin companion


}// fin abstract