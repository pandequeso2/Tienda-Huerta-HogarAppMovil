// ui/view/HistorialPedidosScreen.kt
package com.example.tiendahuertohogar.view // Ojo: el package puede ser ui.view o solo view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext // 1. Importar LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tiendahuertohogar.data.database.ProductoDatabase // 2. Importar tu clase de Base de Datos
import com.example.tiendahuertohogar.data.model.Pedido
import com.example.tiendahuertohogar.data.repository.PedidoRepository // 3. Importar el Repositorio
import com.example.tiendahuertohogar.viewmodel.HistorialPedidosViewModel
import com.example.tiendahuertohogar.viewmodel.HistorialPedidosViewModelFactory // 4. Importar la Factory
import com.example.tiendahuertohogar.viewmodel.PedidoUIState
import java.text.NumberFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialPedidosScreen(
    navController: NavController,
    // El ID del usuario es fundamental para saber qué pedidos mostrar
    usuarioId: Long
) {
    // --- INICIO DE LA LÓGICA DE INSTANCIACIÓN MANUAL (SIN HILT) ---
    // A. Obtenemos el contexto de la aplicación para crear la base de datos.
    val context = LocalContext.current.applicationContext

    // B. Creamos manualmente la cadena de dependencias que Hilt hacía por nosotros.
    val database = ProductoDatabase.getDatabase(context)
    val pedidoRepository = PedidoRepository(database.pedidoDao()) // Creamos el Repo con el Dao
    val viewModelFactory = HistorialPedidosViewModelFactory(pedidoRepository) // Creamos la Factory con el Repo

    // C. Pasamos nuestra factory al creador del ViewModel.
    val viewModel: HistorialPedidosViewModel = viewModel(factory = viewModelFactory)
    // --- FIN DE LA LÓGICA DE INSTANCIACIÓN MANUAL ---

    // El efecto se lanza cuando `usuarioId` cambia o la pantalla se muestra por primera vez.
    LaunchedEffect(usuarioId) {
        viewModel.obtenerPedidosPorUsuario(usuarioId)
    }

    // Observamos el estado completo de la UI (Cargando, Éxito, Error)
    val uiState by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Pedidos") },
                // Aquí puedes agregar un botón para volver atrás
                // navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { ... } }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Decidimos qué mostrar en la pantalla basado en el estado de la UI
            when (val estado = uiState) { // Usamos una variable local 'estado' para el smart casting
                is PedidoUIState.Loading -> {
                    // Muestra un indicador de carga en el centro
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is PedidoUIState.Success -> {
                    if (estado.pedidos.isEmpty()) {
                        // Muestra un mensaje si no hay pedidos
                        Text(
                            text = "Aún no has realizado ningún pedido.",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        // Muestra la lista de pedidos
                        LazyColumn {
                            items(estado.pedidos, key = { it.id }) { pedido ->
                                PedidoListItem(
                                    pedido = pedido,
                                    onPedidoClick = { pedidoId ->
                                        // TODO: Navegar a la pantalla de detalle del pedido
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

// Función de ayuda para formatear la fecha
private fun formatarFecha(fecha: Date): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    return fecha.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
        .format(formatter)
}

// Función de ayuda para formatear el total como moneda local
private fun formatarMoneda(total: Double): String {
    // Usando Locale("es", "CL") para formato de peso chileno. Puedes ajustarlo.
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    return format.format(total)
}
