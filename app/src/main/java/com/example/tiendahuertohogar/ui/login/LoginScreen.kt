package com.example.tiendahuertohogar.ui.login

import android.app.Application // ✨ Importación necesaria
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.proyectologin006d_final.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as Application)
    val vm: LoginViewModel = viewModel(factory = factory)

    val state = vm.uiState
    var showPass by remember { mutableStateOf(false) }

    val pastelBackground = Color(0xFFFFF8F0)
    val pastelAccent = Color(0xFFFFCCBC)
    val pastelText = Color(0xFF5D4037)

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = pastelAccent,
            onPrimary = Color.White,
            surface = pastelBackground,
            onSurface = pastelText,
            background = pastelBackground
        )
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Pastelería App",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = pastelAccent)
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
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = pastelText
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = painterResource(id = R.drawable.logoduoc2),
                    contentDescription = "Logo App",
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
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        vm.submit { nombreUsuario ->
                            navController.navigate("home/$nombreUsuario") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    },
                    enabled = !state.isLoading,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = pastelAccent,
                        contentColor = pastelText
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
                        navController.navigate("registro")
                    },
                    color = Color.Blue,
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
    LoginScreen(navController = navController)
}
