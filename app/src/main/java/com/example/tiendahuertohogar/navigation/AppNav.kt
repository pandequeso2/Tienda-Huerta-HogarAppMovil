package com.example.tiendahuertohogar.navigation

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel // <-- IMPORTACIÓN CLAVE 1
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tiendahuertohogar.ui.login.LoginScreen
import com.example.tiendahuertohogar.ui.login.LoginViewModel
import com.example.tiendahuertohogar.ui.registro.RegistroScreen // Importa RegistroScreen
import com.example.tiendahuertohogar.utils.CameraPermissionHelper
import com.example.tiendahuertohogar.view.MainScreen
import com.example.tiendahuertohogar.view.ProductoFormScreen
import com.example.tiendahuertohogar.viewModel.CartViewModel

// <-- IMPORTACIÓN CLAVE 2

object AppRoutes {
    const val LOGIN = "login"
    const val PANTALLA_PRINCIPAL = "pantalla_principal"
    const val PRODUCTO_FORM = "producto_form"
    const val QR_SCANNER = "qr_scanner"
    const val REGISTRO = "registro" // Define la ruta de registro
}

@Composable
fun AppNav(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current

    var hasCameraPermission by rememberSaveable {
        mutableStateOf(CameraPermissionHelper.hasCameraPermission(context))
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasCameraPermission = isGranted
            val message = if (isGranted) "Permiso de cámara concedido" else "Permiso de cámara denegado"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    )

    NavHost(
        navController = navController,
        startDestination = AppRoutes.LOGIN
    ) {
        composable(AppRoutes.LOGIN) {
            val loginViewModel: LoginViewModel = viewModel()
            LoginScreen(
                navController = navController,
                vm = loginViewModel // Tu LoginScreen SÍ espera este parámetro 'vm'
            )
        }

        composable(
            route = "${AppRoutes.PANTALLA_PRINCIPAL}/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username").orEmpty()

            // CORREGIDO: Llama a viewModel<CartViewModel>()
            // Las importaciones de arriba solucionan todos los errores aquí
            MainScreen(
                mainNavController = navController,
                username = username,
                cartViewModel = viewModel<CartViewModel>()
            )
        }

        composable(
            route = AppRoutes.PRODUCTO_FORM
        ) {
            // ProductoFormScreen usa un viewModel por defecto,
            // no necesita parámetros
            ProductoFormScreen()
        }

        // AÑADIDO: La ruta para la pantalla de Registro
        composable(AppRoutes.REGISTRO) {
            RegistroScreen(navController = navController)
        }
    }
}