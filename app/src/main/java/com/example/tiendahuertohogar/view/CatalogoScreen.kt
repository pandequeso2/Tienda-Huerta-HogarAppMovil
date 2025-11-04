package com.example.tiendahuertohogar.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tiendahuertohogar.navigation.AppRoutes
import java.net.URLEncoder

@Composable
fun CatalogoScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Aquí va el CATÁLOGO DE PRODUCTOS")
        // Aquí deberías usar un LazyColumn para mostrar los productos

        Spacer(Modifier.height(20.dp))

        // Ejemplo de cómo navegar a tu ProductoFormScreen
        Button(onClick = {
            val nombreProducto = "Manzana Fuji"
            val precioProducto = "1200"

            val nombreEncoded = URLEncoder.encode(nombreProducto, "UTF-8")
            val precioEncoded = URLEncoder.encode(precioProducto, "UTF-8")

            navController.navigate("${AppRoutes.PRODUCTO_FORM}/$nombreEncoded/$precioEncoded")
        }) {
            Text("Ir a Formulario (Ejemplo)")
        }
    }
}