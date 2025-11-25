package com.example.tiendahuertohogar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.tiendahuertohogar.navigation.AppNav
import com.example.tiendahuertohogar.ui.theme.TiendaHuertoHogarTheme // <-- Asegúrate de usar el nombre correcto de tu tema

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Reemplaza TiendaHuertoHogarTheme si tu tema tiene otro nombre
            TiendaHuertoHogarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Aquí es donde se inicia todo el sistema de navegación
                    AppNav()
                }
            }
        }
    }
}