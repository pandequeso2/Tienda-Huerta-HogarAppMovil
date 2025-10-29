package com.example.tiendahuertohogar.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Declaramos el DataStore con nombre "preferencias_usuario" [cite: 122, 124]
val Context.dataStore by preferencesDataStore(name = "preferencias_usuario")

class EstadoDataStore (private val context: Context) {

    // Clave para guardar el estado del modo especial [cite: 127, 128, 129]
    private val ESTADO_ACTIVADO = booleanPreferencesKey(name = "modo_activado")

    // Función para guardar el estado en DataStore [cite: 130, 131]
    suspend fun guardarEstado (valor: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias [ESTADO_ACTIVADO] = valor
        }
    }

    // Función para obtener el estado como Flow (reactivo) [cite: 135, 136]
    fun obtenerEstado(): Flow<Boolean?> {
        return context.dataStore.data.map { preferencias ->
            preferencias [ESTADO_ACTIVADO]
        }
    }
}