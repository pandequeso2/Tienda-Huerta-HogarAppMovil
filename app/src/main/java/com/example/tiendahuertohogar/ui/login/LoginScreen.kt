package com.example.tiendahuertohogar.ui.login

import LoginViewModel
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.password
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel // Importación necesaria
import androidx.navigation.NavController
import com.example.tiendahuertohogar.navigation.AppRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    // --- ¡CORRECCIÓN CLAVE! ---
    // Se recibe el ViewModel como parámetro.
    viewModel: LoginViewModel
) {
    // Se obtiene el uiState desde el viewModel
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Este LaunchedEffect ahora funcionará porque uiState.loginSuccess existe
    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            Toast.makeText(context, "Login exitoso!", Toast.LENGTH_SHORT).show()
            navController.navigate("${AppRoutes.PANTALLA_PRINCIPAL}/${Uri.encode(uiState.username)}") {
                popUpTo(AppRoutes.LOGIN) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Iniciar Sesión", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = uiState.username,
            onValueChange = { viewModel.onLoginChanged(it, uiState.password) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.password,
            onValueChange = { viewModel.onLoginChanged(uiState.username, it) },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        // El acceso a uiState.isLoading ahora es correcto
        if (uiState.isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }

        // El acceso a uiState.error ahora es correcto
        uiState.error?.let { errorMsg ->
            Text(
                text = errorMsg,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            // La llamada a viewModel.onLogin() ahora es correcta
            onClick = { viewModel.onLogin() },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
    }
}
