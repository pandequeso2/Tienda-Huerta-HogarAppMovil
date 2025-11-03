package com.example.tiendahuertohogar.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tiendahuertohogar.data.model.Producto
import com.example.tiendahuertohogar.ui.theme.*
import com.example.tiendahuertohogar.viewModel.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoFormScreen(
    navController: NavController,
    nombre: String,
    precio: String,
    viewModel: ProductoViewModel = viewModel()
) {
    // Estados para cada campo del formulario
    var codigo by remember { mutableStateOf("") }
    var nombreState by remember { mutableStateOf(nombre) }
    var descripcion by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf("") }
    var precioState by remember { mutableStateOf(precio) }
    var stock by remember { mutableStateOf("") }
    var showCategoryMenu by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val categorias = listOf(
        "Frutas Frescas",
        "Verduras Orgánicas",
        "Productos Orgánicos",
        "Productos Lácteos"
    )

    // Estado para controlar si el formulario es válido
    val isFormValid by remember(codigo, nombreState, descripcion, categoriaSeleccionada, precioState, stock) {
        derivedStateOf {
            codigo.isNotBlank() &&
                    nombreState.isNotBlank() &&
                    descripcion.isNotBlank() &&
                    categoriaSeleccionada.isNotBlank() &&
                    (precioState.toDoubleOrNull() ?: 0.0) > 0.0 &&
                    (stock.toIntOrNull() ?: -1) >= 0
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Nuevo Producto",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = BlancoNieve
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VerdeEsmeralda,
                    titleContentColor = BlancoNieve
                )
            )
        },
        containerColor = BlancoSuave
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header con gradiente
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                VerdeEsmeralda.copy(alpha = 0.3f),
                                BlancoSuave
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "🌱",
                        style = MaterialTheme.typography.displayMedium
                    )
                    Text(
                        "Registra un nuevo producto",
                        style = MaterialTheme.typography.bodyLarge,
                        color = GrisMedio
                    )
                }
            }

            // Formulario
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .shadow(2.dp, shape = RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = BlancoNieve
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Información del Producto",
                        style = MaterialTheme.typography.titleLarge,
                        color = MarronClaro,
                        fontWeight = FontWeight.SemiBold
                    )

                    Divider(color = GrisClaro)

                    // Código SKU
                    OutlinedTextField(
                        value = codigo,
                        onValueChange = { codigo = it.uppercase() },
                        label = { Text("Código SKU") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.QrCode,
                                contentDescription = null,
                                tint = VerdeEsmeralda
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VerdeEsmeralda,
                            focusedLabelColor = VerdeEsmeralda,
                            cursorColor = VerdeEsmeralda
                        ),
                        shape = RoundedCornerShape(12.dp),
                        placeholder = { Text("Ej: FR001", color = GrisMedio) }
                    )

                    // Nombre
                    OutlinedTextField(
                        value = nombreState,
                        onValueChange = { nombreState = it },
                        label = { Text("Nombre del Producto") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Title,
                                contentDescription = null,
                                tint = VerdeEsmeralda
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VerdeEsmeralda,
                            focusedLabelColor = VerdeEsmeralda,
                            cursorColor = VerdeEsmeralda
                        ),
                        shape = RoundedCornerShape(12.dp),
                        placeholder = { Text("Ej: Manzanas Fuji", color = GrisMedio) }
                    )

                    // Descripción
                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripción") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Description,
                                contentDescription = null,
                                tint = VerdeEsmeralda
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VerdeEsmeralda,
                            focusedLabelColor = VerdeEsmeralda,
                            cursorColor = VerdeEsmeralda
                        ),
                        shape = RoundedCornerShape(12.dp),
                        placeholder = { Text("Describe las características del producto", color = GrisMedio) }
                    )

                    // Categoría (Dropdown)
                    ExposedDropdownMenuBox(
                        expanded = showCategoryMenu,
                        onExpandedChange = { showCategoryMenu = !showCategoryMenu }
                    ) {
                        OutlinedTextField(
                            value = categoriaSeleccionada,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Categoría") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Category,
                                    contentDescription = null,
                                    tint = VerdeEsmeralda
                                )
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCategoryMenu)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = VerdeEsmeralda,
                                focusedLabelColor = VerdeEsmeralda
                            ),
                            shape = RoundedCornerShape(12.dp),
                            placeholder = { Text("Selecciona una categoría", color = GrisMedio) }
                        )

                        ExposedDropdownMenu(
                            expanded = showCategoryMenu,
                            onDismissRequest = { showCategoryMenu = false }
                        ) {
                            categorias.forEach { categoria ->
                                DropdownMenuItem(
                                    text = { Text(categoria) },
                                    onClick = {
                                        categoriaSeleccionada = categoria
                                        showCategoryMenu = false
                                    }
                                )
                            }
                        }
                    }

                    // Fila con Precio y Stock
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Precio
                        OutlinedTextField(
                            value = precioState,
                            onValueChange = { precioState = it },
                            label = { Text("Precio (CLP)") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.MonetizationOn,
                                    contentDescription = null,
                                    tint = AmarilloMostaza
                                )
                            },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = VerdeEsmeralda,
                                focusedLabelColor = VerdeEsmeralda,
                                cursorColor = VerdeEsmeralda
                            ),
                            shape = RoundedCornerShape(12.dp),
                            placeholder = { Text("1000", color = GrisMedio) },
                            prefix = { Text("$", color = AmarilloMostaza) }
                        )

                        // Stock
                        OutlinedTextField(
                            value = stock,
                            onValueChange = { stock = it },
                            label = { Text("Stock") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Inventory,
                                    contentDescription = null,
                                    tint = VerdeEsmeralda
                                )
                            },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = VerdeEsmeralda,
                                focusedLabelColor = VerdeEsmeralda,
                                cursorColor = VerdeEsmeralda
                            ),
                            shape = RoundedCornerShape(12.dp),
                            placeholder = { Text("100", color = GrisMedio) }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Botón Guardar
                    Button(
                        onClick = {
                            val nuevoProducto = Producto(
                                codigo = codigo,
                                nombre = nombreState,
                                descripcion = descripcion,
                                categoria = categoriaSeleccionada,
                                precio = precioState.toDoubleOrNull() ?: 0.0,
                                stock = stock.toIntOrNull() ?: 0,
                                imagenUrl = null
                            )
                            viewModel.guardarProducto(nuevoProducto)
                            showSuccessDialog = true
                        },
                        enabled = isFormValid,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VerdeEsmeralda,
                            contentColor = BlancoNieve,
                            disabledContainerColor = GrisClaro,
                            disabledContentColor = GrisMedio
                        )
                    ) {
                        Text(
                            "Guardar Producto",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Botón Cancelar
                    OutlinedButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = GrisMedio
                        )
                    ) {
                        Text("Cancelar")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Diálogo de éxito
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                navController.popBackStack()
            },
            icon = {
                Text("✅", style = MaterialTheme.typography.displaySmall)
            },
            title = {
                Text(
                    "¡Producto Guardado!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "El producto '$nombreState' se ha registrado correctamente en el sistema.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrisMedio
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdeEsmeralda
                    )
                ) {
                    Text("Aceptar")
                }
            },
            containerColor = BlancoNieve,
            shape = RoundedCornerShape(16.dp)
        )
    }
}