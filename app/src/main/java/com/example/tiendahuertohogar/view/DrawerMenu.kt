package com.example.tiendahuertohogar.view

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

data class MenuItem(
    val title: String,
    val icon: ImageVector,
    val price: String
)

@Composable
fun DrawerMenu(
    username: String,
    navController: NavController
) {
    val menuItems = listOf(
        MenuItem("Hamburgesa Clasica", Icons.Default.Fastfood, "5000"),
        MenuItem("Hamburgesa BBQ", Icons.Default.LunchDining, "5500"),
        MenuItem("Hamburgesa Veggie", Icons.Default.Grass, "6000"),
        MenuItem("Hamburgesa Picante", Icons.Default.LocalFireDepartment, "5700"),
        MenuItem("Hamburgesa Doble", Icons.Default.Star, "7000")
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.BottomStart
        ) {
            Text(
                text = "Categorias user: $username",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(16.dp)
            )
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(menuItems) { item ->
                NavigationDrawerItem(
                    label = { Text(item.title) },
                    selected = false,
                    onClick = {
                        val nombre = Uri.encode(item.title)
                        navController.navigate("ProductoFormScreen/$nombre/${item.price}")
                    },
                    icon = { Icon(item.icon, contentDescription = item.title) }
                )
            }
        }

        Text(
            text = "@ 2025 Burger App",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DrawerMenuPreview() {
    val navController = rememberNavController()
    DrawerMenu(username = "Usuario Prueba", navController = navController)
}