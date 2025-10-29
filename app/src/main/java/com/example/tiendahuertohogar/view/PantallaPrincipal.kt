package com.example.tiendahuertohogar.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tiendahuertohogar.viewModel.EstadoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(
    navController: NavController,
    username: String,
    viewModel: EstadoViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerMenu(username = username, navController = navController)
        }
    ) { 
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Huerto Hogar") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    }
                )
            }
        ) {
            ContenidoPantallaPrincipal(modifier = Modifier.padding(it), viewModel = viewModel)
        }
    }
}

@Composable
fun ContenidoPantallaPrincipal(modifier: Modifier = Modifier, viewModel: EstadoViewModel) {
    val estado by viewModel.activo.collectAsState()
    val mostrarMensaje by viewModel.mostrarMensaje.collectAsState()

    if (estado == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        estado?.let { estaActivo ->
            val colorAnimado by animateColorAsState(
                targetValue = if (estaActivo) Color(0xFF4CAF50) else Color(0xFFBBBBCC),
                animationSpec = tween(durationMillis = 500),
                label = "colorAnimacion"
            )

            val textoBoton by remember(key1 = estaActivo) {
                derivedStateOf { if (estaActivo) "Desactivar Modo Especial" else "Activar Modo Especial" }
            }

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { viewModel.alternarEstado() },
                    colors = ButtonDefaults.buttonColors(containerColor = colorAnimado),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 60.dp)
                ) {
                    Text(text = textoBoton, style = MaterialTheme.typography.titleLarge)
                }

                Spacer(modifier = Modifier.height(height = 24.dp))

                AnimatedVisibility(visible = mostrarMensaje) {
                    Text(
                        text = "Estado guardado exitosamente!",
                        color = Color(color = 0xFF4CAF50),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}