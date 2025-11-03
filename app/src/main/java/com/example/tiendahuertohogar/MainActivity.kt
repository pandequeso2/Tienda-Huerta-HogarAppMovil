package com.example.tiendahuertohogar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.example.tiendahuertohogar.ui.theme.TiendaHuertoHogarTheme
import com.example.tiendahuertohogar.navigation.AppNav


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            TiendaHuertoHogarTheme {
                AppNav()
            }
        }
    }
}
