package com.example.tiendahuertohogar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding // <--- ¡Importar padding!
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold // <--- ¡Importar Scaffold!
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat // <--- ¡Importar WindowCompat!
import com.example.tiendahuertohogar.ui.theme.TiendaHuertoHogarTheme
import com.example.tiendahuertohogar.view.PantallaPrincipal
// Importamos PantallaPrincipal (ajusta la ruta según donde la pusiste)
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // PARTE 5: Ajuste para mostrar contenido en pantalla completa (Status bar y Nav bar)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            TiendaHuertoHogarTheme {

                // Usamos Scaffold para definir la estructura y obtener el padding
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    // Aplicamos el padding correcto para status y nav bar [cite: 237]
                    PantallaPrincipal(
                        modifier = Modifier.padding(paddingValues = innerPadding)
                    )
                }
            }
        }
    }
}