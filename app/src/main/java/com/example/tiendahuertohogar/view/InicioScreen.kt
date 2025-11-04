package com.example.tiendahuertohogar.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun InicioScreen(username: String, navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
        Text(text = "Aquí va el INICIO\n¡Bienvenido, $username!")
        // Aquí puedes mostrar productos destacados, etc.
    }
}