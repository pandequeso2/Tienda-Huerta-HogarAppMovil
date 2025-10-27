// data/database/Converters.kt
package com.example.tiendahuertohogar.data.database

import androidx.room.TypeConverter
import java.util.Date // ¡Importante añadir esta línea!

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
