package com.example.tiendahuertohogar.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.rememberCoroutineScope
// --- CORRECCIÓN EN LA IMPORTACIÓN ---
import com.example.tiendahuertohogar.data.database.AppDataBase
import com.example.tiendahuertohogar.data.repository.UsuarioRepository
import com.example.tiendahuertohogar.viewmodel.PerfilUsuarioViewModel
import com.example.tiendahuertohogar.viewmodel.PerfilUsuarioViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilUsuarioScreen(
    usuarioId: Long
) {
    val context = LocalContext.current.applicationContext
    val scope = rememberCoroutineScope()

    // --- CORRECCIÓN AQUÍ (ProductoDataBase con 'B') ---
    val database = AppDataBase.getDatabase(context, scope)
    val usuarioRepository = UsuarioRepository(database.usuarioDao())
    val viewModelFactory = PerfilUsuarioViewModelFactory(usuarioRepository)

    val viewModel: PerfilUsuarioViewModel = viewModel(factory = viewModelFactory)

    val uiState by viewModel.uiState.collectAsState()
    val usuario = uiState.usuario

    LaunchedEffect(key1 = usuarioId) {
        viewModel.cargarPerfil(usuarioId)
    }

    var nombre by remember(usuario?.nombre) { mutableStateOf(usuario?.nombre ?: "") }
    var email by remember(usuario?.email) { mutableStateOf(usuario?.email ?: "") }
    var direccion by remember(usuario?.direccion) { mutableStateOf(usuario?.direccion ?: "") }
    var telefono by remember(usuario?.telefono) { mutableStateOf(usuario?.telefono ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mi Perfil") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                usuario != null -> {
                    FormularioPerfil(
                        nombre = nombre,
                        onNombreChange = { nombre = it },
                        email = email,
                        onEmailChange = { email = it },
                        direccion = direccion,
                        onDireccionChange = { direccion = it },
                        telefono = telefono,
                        onTelefonoChange = { telefono = it },
                        onGuardarClick = {
                            val usuarioActualizado = usuario.copy(
                                nombre = nombre,
                                email = email,
                                direccion = direccion,
                                telefono = telefono
                            )
                            viewModel.actualizarPerfil(usuarioActualizado)
                        }
                    )
                }
                uiState.error != null -> {
                    Text(
                        text = "Error: ${uiState.error}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    Text(
                        "No se pudo cargar el perfil del usuario.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun FormularioPerfil(
    nombre: String, onNombreChange: (String) -> Unit,
    email: String, onEmailChange: (String) -> Unit,
    direccion: String, onDireccionChange: (String) -> Unit,
    telefono: String, onTelefonoChange: (String) -> Unit,
    onGuardarClick: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = onNombreChange,
            label = { Text("Nombre Completo") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = direccion,
            onValueChange = onDireccionChange,
            label = { Text("Dirección de Envío") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = telefono,
            onValueChange = onTelefonoChange,
            label = { Text("Teléfono de Contacto") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onGuardarClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Cambios")
        }
    }
}