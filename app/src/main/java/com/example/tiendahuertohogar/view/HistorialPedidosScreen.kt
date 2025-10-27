// ui/view/HistorialPedidosScreen.kt
package com.example.tiendahuertohogar.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tiendahuertohogar.data.model.Pedido // <--- ASUMO QUE LA FECHA ES java.util.Date
import com.example.tiendahuertohogar.viewmodel.HistorialPedidosViewModel
// 1. Importar las clases de la nueva API de tiempo
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date // Mantenemos la importación si el modelo Pedido usa Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialPedidosScreen(
    onPedidoClick: (Long) -> Unit,
    viewModel: HistorialPedidosViewModel = hiltViewModel()
) {
    val pedidos by viewModel.historialPedidos.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mis Pedidos") }) }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(pedidos) { pedido ->
                // 2. Usar la API moderna para formatear la fecha
                val fechaFormateada = formatarFecha(pedido.fecha)

                ListItem(
                    headlineText = { Text("Pedido #${pedido.id}") },
                    supportingText = { Text("Fecha: $fechaFormateada - Estado: ${pedido.estado}") },
                    trailingContent = { Text("\$${pedido.total.toInt()}") },
                    modifier = Modifier.clickable { onPedidoClick(pedido.id) }
                )
                Divider()
            }
        }
    }
}

// 3. (Opcional) Crear una función de ayuda para mantener el Composable limpio
private fun formatarFecha(fecha: Date): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    // Convertir de java.util.Date a java.time.LocalDate
    return fecha.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .format(formatter)
}
