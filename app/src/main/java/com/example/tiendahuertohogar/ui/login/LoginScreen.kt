package com.example.tiendahuertohogar.ui.login

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tiendahuertohogar.R // Asegúrate de que R se importe correctamente
import com.example.tiendahuertohogar.ui.theme.TiendaHuertoHogarTheme // Importa el tema principal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    vm: LoginViewModel // Recibe el ViewModel
) {
    val state = vm.uiState
    var showPass by remember { mutableStateOf(false) }

    // Usamos el Tema principal que ya tiene los colores del PDF
    TiendaHuertoHogarTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Tienda Huerto Hogar", // Título actualizado
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    // Color primario (VerdeEsmeralda) del tema
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Bienvenido",
                    style = MaterialTheme.typography.headlineMedium, // Usa tipografía del tema
                    color = MaterialTheme.colorScheme.tertiary // Color MarronClaro para títulos
                )

                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    // Logo de Huerto Hogar (asumiendo que se llama 'logo' en drawable)
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo Huerto Hogar",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = state.correo,
                    onValueChange = vm::onCorreoChange,
                    label = { Text("Correo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.95f)
                )

                OutlinedTextField(
                    value = state.clave,
                    onValueChange = vm::onClaveChange,
                    label = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(onClick = { showPass = !showPass }) {
                            Text(if (showPass) "Ocultar" else "Ver")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.95f)
                )

                if (state.mensaje.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = state.mensaje,
                        color = MaterialTheme.colorScheme.error, // Color de error del tema
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        vm.submit { nombreUsuario ->
                            navController.navigate("pantalla_principal/$nombreUsuario") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    },
                    enabled = !state.isLoading,
                    shape = RoundedCornerShape(12.dp),
                    // Colores del tema (Botón primario)
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(50.dp)
                ) {
                    Text(if (state.isLoading) "Validando..." else "Iniciar sesión")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Registrarse",
                    modifier = Modifier.clickable {
                        navController.navigate("registro") // Asumiendo que la ruta es "registro"
                    },
                    color = MaterialTheme.colorScheme.primary, // Color de acento
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as Application)
    val vm: LoginViewModel = viewModel(factory = factory)
    LoginScreen(navController = navController, vm = vm)
}