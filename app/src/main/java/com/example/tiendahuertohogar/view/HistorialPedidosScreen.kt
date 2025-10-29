package com.example.tiendahuertohogar.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tiendahuertohogar.data.model.Pedido
import com.example.tiendahuertohogar.viewmodel.HistorialPedidosViewModel
import com.example.tiendahuertohogar.viewmodel.PedidoUIState
import java.text.NumberFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialPedidosScreen(
    navController: NavController,
    usuarioId: Long,
    viewModel: HistorialPedidosViewModel = hiltViewModel()
) {
    LaunchedEffect(usuarioId) {
        viewModel.obtenerPedidosPorUsuario(usuarioId)
    }

    val uiState by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Pedidos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when (val estado = uiState) {
                is PedidoUIState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is PedidoUIState.Success -> {
                    if (estado.pedidos.isEmpty()) {
                        Text(
                            text = "Aún no has realizado ningún pedido.",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn {
                            items(estado.pedidos, key = { it.id }) { pedido ->
                                PedidoListItem(
                                    pedido = pedido,
                                    onPedidoClick = { pedidoId ->
                                        // navController.navigate("detallePedido/${pedidoId}")
                                    }
                                )
                                Divider()
                            }
                        }
                    }
                }
                is PedidoUIState.Error -> {
                    Text(
                        text = "Error al cargar los pedidos: ${estado.message}",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun PedidoListItem(pedido: Pedido, onPedidoClick: (Long) -> Unit) {
    val fechaFormateada = formatarFecha(pedido.fecha)
    val totalFormateado = formatarMoneda(pedido.total)

    ListItem(
        headlineContent = { Text("Pedido #${pedido.id}") },
        supportingContent = { Text("Fecha: $fechaFormateada - Estado: ${pedido.estado}") },
        trailingContent = { Text(totalFormateado, style = MaterialTheme.typography.bodyLarge) },
        modifier = Modifier.clickable { onPedidoClick(pedido.id) }
    )
}

private fun formatarFecha(fecha: Date): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    return fecha.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
        .format(formatter)
}

private fun formatarMoneda(total: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    return format.format(total)
}
