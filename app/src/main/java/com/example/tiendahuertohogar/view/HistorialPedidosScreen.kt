package com.example.tiendahuertohogar.view // <-- ASEGÚRATE QUE EL PACKAGE SEA '.view'

// Corregido: La importación debe apuntar a la clase en el paquete 'viewmodel'
import com.example.tiendahuertohogar.viewmodel.HistorialPedidosViewModel
import com.example.tiendahuertohogar.viewmodel.PedidoUIState
// -------------------------------------
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
// import androidx.lifecycle.viewmodel.compose.viewModel // No es necesario si se recibe como parámetro
import androidx.navigation.NavController
import java.text.NumberFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialPedidosScreen(
    navController: NavController,
    usuarioId: Long,
    viewModel: HistorialPedidosViewModel // Recibe el ViewModel instanciado desde AppNav
) {
    // Llama al ViewModel para cargar los pedidos cuando la pantalla se inicia
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
            // Reacciona al estado del ViewModel
            when (val estado = uiState) {
                is PedidoUIState.Loading -> {
                    // Muestra un indicador de carga
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is PedidoUIState.Success -> {
                    // Muestra la lista de pedidos si no está vacía
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
                                        // TODO: Navegar al detalle del pedido (aún no implementado)
                                        // navController.navigate("detallePedido/${pedidoId}")
                                    }
                                )
                                Divider()
                            }
                        }
                    }
                }
                is PedidoUIState.Error -> {
                    // Muestra un mensaje de error
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

/**
 * Un Composable para mostrar un solo ítem de pedido en la lista.
 */
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

/**
 * Helper para formatear un objeto Date a un String legible.
 */
private fun formatarFecha(fecha: Date): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    return fecha.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
        .format(formatter)
}

/**
 * Helper para formatear un Double a moneda local (Peso Chileno).
 */
private fun formatarMoneda(total: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    return format.format(total)
}