// MainActivity.kt
package com.example.tiendahuertohogar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.tiendahuertohogar.navigation.AppNav
import com.example.tiendahuertohogar.ui.theme.TiendaHuertoHogarTheme
import dagger.hilt.android.AndroidEntryPoint // 1. Importar la anotación

@AndroidEntryPoint // 2. ¡Añadir esta anotación!
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TiendaHuertoHogarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Aquí se llama a la navegación principal
                    AppNav()
                }
            }
        }
    }
}
