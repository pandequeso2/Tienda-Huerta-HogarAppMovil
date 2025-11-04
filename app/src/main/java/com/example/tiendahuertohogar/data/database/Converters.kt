package com.example.tiendahuertohogar.data.database

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        // Convierte el Long (número) de la BD a un objeto Date
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        // Convierte el objeto Date a un Long (número) para guardarlo en la BD
        return date?.time
    }
}