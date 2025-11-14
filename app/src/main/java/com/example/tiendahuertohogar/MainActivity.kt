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
import com.example.tiendahuertohogar.ui.theme.ApiRestTheme
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiendahuertohogar.ui.sreens.PostScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Permite que la app dibuje contenido debajo de las barras del sistema
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Aquí inicia Jetpack Compose
        setContent {
            ApiRestTheme {
                val postViewModel: com.example.tiendahuertohogar.viewModel.PostViewModel = viewModel()
                PostScreen(viewModel = postViewModel)
            }
        }
    }
}




/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TiendaHuertoHogarTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Llama al sistema de navegación principal de la app
                    AppNav()
                }

                    // Permite que la app dibuje contenido debajo de las barras del sistema
                    WindowCompat.setDecorFitsSystemWindows(window, false)

                    // Aquí inicia Jetpack Compose
                    setContent {
                        ApiRestTheme {
                            val postViewModel: com.example.tiendahuertohogar.viewModel.PostViewModel = viewModel()
                            PostScreen(viewModel = postViewModel)
                        }
                    }
                }
            }
        }
    }*/
