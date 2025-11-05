package com.example.tiendahuertohogar.view

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tiendahuertohogar.ui.theme.BlancoNieve
import com.example.tiendahuertohogar.ui.theme.GrisClaro
import com.example.tiendahuertohogar.ui.theme.GrisMedio
import com.example.tiendahuertohogar.ui.theme.VerdeEsmeralda
import com.example.tiendahuertohogar.viewmodel.ProductoViewModel
import java.text.NumberFormat
import java.util.*

/**
 * Esta es la nueva pantalla que muestra los detalles de un producto.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    navController: NavController,
    viewModel: ProductoViewModel,
    productoId: Long
) {
    // Carga el producto cuando la pantalla aparece
    LaunchedEffect(productoId) {
        viewModel.cargarProductoPorId(productoId)
    }

    val producto by viewModel.productoSeleccionado.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(producto?.nombre ?: "Detalle") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            // Barra inferior con el botón de "Añadir al carrito"
            producto?.let {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 8.dp,
                    color = BlancoNieve
                ) {
                    Button(
                        onClick = { /* TODO: Implementar lógica de añadir al carrito */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VerdeEsmeralda
                        )
                    ) {
                        Text(
                            "Añadir al Carrito",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            producto?.let { prod ->
                // Formato de moneda
                val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
                val precioFormateado = format.format(prod.precio)

                Column(modifier = Modifier.fillMaxWidth()) {
                    // Imagen del producto
                    Image(
                        painter = painterResource(id = getDrawableId(context, prod.imagenUrl)),
                        contentDescription = prod.nombre,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp) // <-- ERROR CORREGIDO AQUÍ
                            .background(GrisClaro),
                        contentScale = ContentScale.Crop
                    )

                    // Contenido de texto
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Nombre
                        Text(
                            text = prod.nombre,
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold
                        )

                        // Precio
                        Text(
                            text = precioFormateado,
                            style = MaterialTheme.typography.headlineMedium,
                            color = VerdeEsmeralda,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 36.sp
                        )

                        // Descripción
                        Text(
                            text = "Descripción",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = prod.descripcion,
                            style = MaterialTheme.typography.bodyLarge,
                            color = GrisMedio,
                            lineHeight = 24.sp
                        )

                        Divider(color = GrisClaro)

                        // Detalles (Stock y Categoría)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    "Categoría",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GrisMedio
                                )
                                Text(
                                    prod.categoria,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    "Stock Disponible",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GrisMedio
                                )
                                Text(
                                    "${prod.stock} unidades",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
                ?: if (productoId == 0L) {
                    // Caso de error donde el ID es inválido
                    Text("ID de producto inválido.", modifier = Modifier.align(Alignment.Center))
                } else {
                    // Estado de carga
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
        }
    }
}

/**
 * Función helper (copiada de CatalogoScreen) para obtener un ID de drawable seguro.
 */
@Composable
private fun getDrawableId(context: Context, imageName: String?): Int {
    val fallbackImageId = context.resources.getIdentifier(
        "ic_launcher_foreground",
        "drawable",
        context.packageName
    )

    if (imageName.isNullOrBlank()) {
        return fallbackImageId
    }

    val id = context.resources.getIdentifier(imageName, "drawable", context.packageName)

    return if (id == 0) {
        fallbackImageId
    } else {
        id
    }
}