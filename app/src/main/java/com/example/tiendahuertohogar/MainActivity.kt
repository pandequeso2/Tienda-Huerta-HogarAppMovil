package com.example.tiendahuertohogar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.example.tiendahuertohogar.navigation.AppNav
import com.example.tiendahuertohogar.ui.theme.TiendaHuertoHogarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configuración para pantalla completa (edge-to-edge)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            // Usamos el tema principal y cargamos la navegación completa
            TiendaHuertoHogarTheme {
                AppNav()
            }
        }
    }
}