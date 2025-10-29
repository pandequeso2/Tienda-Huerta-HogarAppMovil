package com.example.tiendahuertohogar.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement // <- SUGERENCIA: Importar Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue // <- SUGERENCIA: Usar 'by' para delegados
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiendahuertohogar.viewModel.EstadoViewModel

@Composable
fun PantallaPrincipal(modifier: Modifier = Modifier, viewModel: EstadoViewModel = viewModel()) {

    // SUGERENCIA: Usar 'by' para una sintaxis más limpia al acceder al valor
    val estado by viewModel.activo.collectAsState()
    val mostrarMensaje by viewModel.mostrarMensaje.collectAsState()

    // Mantenemos el estado de carga
    if (estado == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        // SUGERENCIA: Usamos 'let' para manejar de forma segura el estado no nulo
        // en lugar de usar el operador de aserción no nula (!!)
        estado?.let { estaActivo ->
            // Animamos el color del botón según el estado
            val colorAnimado by animateColorAsState(
                // --- ERROR CORREGIDO ---
                // Se cambió el código de color inválido 0xFFBBBECS por 0xFFBBBBCC
                targetValue = if (estaActivo) Color(0xFF4CAF50) else Color(0xFFBBBBCC),
                animationSpec = tween(durationMillis = 500),
                label = "colorAnimacion"
            )

            // Texto derivado (sin cambios, ya estaba bien)
            val textoBoton by remember(key1 = estaActivo) {
                derivedStateOf { if (estaActivo) "Desactivar Modo Especial" else "Activar Modo Especial" }
            }

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(32.dp),
                // SUGERENCIA: Usar el nombre corto 'Arrangement'
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

                // Feedback animado
                AnimatedVisibility(visible = mostrarMensaje) { // No es necesario .value con 'by'
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
