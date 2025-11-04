package com.example.tiendahuertohogar.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.error
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiendahuertohogar.data.database.ProductoDatabase
import com.example.tiendahuertohogar.data.model.Usuario
import com.example.tiendahuertohogar.data.repository.UsuarioRepository
import com.example.tiendahuertohogar.viewmodel.PerfilUsuarioViewModel
import com.example.tiendahuertohogar.viewmodel.PerfilUsuarioViewModelFactory
// 1. IMPORTA rememberCoroutineScope
import androidx.compose.runtime.rememberCoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilUsuarioScreen(
    usuarioId: Long
) {
    // --- INICIO DE LA LÓGICA DE INSTANCIACIÓN MANUAL (SIN HILT) ---
    val context = LocalContext.current.applicationContext

    // 2. OBTÉN EL SCOPE
    val scope = rememberCoroutineScope()

    // B. Creamos manualmente la cadena de dependencias.
    // 3. PASA EL SCOPE A getDatabase
    val database = ProductoDatabase.getDatabase(context, scope)
    val usuarioRepository = UsuarioRepository(database.usuarioDao())
    val viewModelFactory = PerfilUsuarioViewModelFactory(usuarioRepository)

    // C. Pasamos nuestra factory al creador del ViewModel.
    val viewModel: PerfilUsuarioViewModel = viewModel(factory = viewModelFactory)
    // --- FIN DE LA LÓGICA DE INSTANCIACIÓN MANUAL ---

    // Observar el estado de la UI desde el ViewModel
    val uiState by viewModel.uiState.collectAsState()
    val usuario = uiState.usuario

    // Cargar los datos del usuario una sola vez cuando la pantalla aparece
    LaunchedEffect(key1 = usuarioId) {
        viewModel.cargarPerfil(usuarioId)
    }

    // Estados locales para los campos del formulario
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
                // Estado de carga inicial
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                // Si el usuario se carga correctamente, mostrar el formulario
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
                // Si hay un error al cargar
                uiState.error != null -> {
                    Text(
                        text = "Error: ${uiState.error}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                // Estado por defecto
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

// (Tu Composable FormularioPerfil se mantiene igual)
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
            readOnly = true // El email no debería ser editable
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