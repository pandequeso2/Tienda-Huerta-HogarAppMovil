// ui/view/PerfilUsuarioScreen.kt
package com.example.tiendahuertohogar.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tiendahuertohogar.viewmodel.PerfilUsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilUsuarioScreen(
    viewModel: PerfilUsuarioViewModel = hiltViewModel()
) {
    // Observar el estado de la UI desde el ViewModel
    val uiState by viewModel.uiState.collectAsState()
    val usuario = uiState.usuario

    // ID del usuario que ha iniciado sesión (por ahora, fijo)
    // TODO: Obtener este ID de la sesión del usuario real
    val loggedInUserId = 1L

    // Cargar los datos del usuario una sola vez cuando la pantalla aparece
    LaunchedEffect(key1 = loggedInUserId) {
        viewModel.cargarPerfil(loggedInUserId)
    }

    // Estados locales para los campos del formulario, inicializados desde el ViewModel
    var nombre by remember(usuario?.nombre) { mutableStateOf(usuario?.nombre ?: "") }
    var email by remember(usuario?.email) { mutableStateOf(usuario?.email ?: "") }
    var direccion by remember(usuario?.direccion) { mutableStateOf(usuario?.direccion ?: "") }
    var telefono by remember(usuario?.telefono) { mutableStateOf(usuario?.telefono ?: "") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Mi Perfil", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(24.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else if (usuario != null) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección de Envío") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono de Contacto") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val usuarioActualizado = usuario.copy(
                        nombre = nombre,
                        email = email,
                        direccion = direccion,
                        telefono = telefono
                    )
                    viewModel.actualizarPerfil(usuarioActualizado)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Cambios")
            }
        } else {
            Text("No se pudo cargar el perfil del usuario.")
        }
    }
}
