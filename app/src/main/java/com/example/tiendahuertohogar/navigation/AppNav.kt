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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tiendahuertohogar.ui.Carrito.CarritoScreen
import com.example.tiendahuertohogar.ui.login.LoginScreen
import com.example.tiendahuertohogar.ui.login.LoginViewModel
import com.example.tiendahuertohogar.ui.registro.RegistroScreen
import com.example.tiendahuertohogar.utils.CameraPermissionHelper
import com.example.tiendahuertohogar.view.MainScreen
import com.example.tiendahuertohogar.view.ProductoFormScreen
import com.example.tiendahuertohogar.view.QrScannerScreen
import com.example.tiendahuertohogar.viewModel.CartViewModel
import com.example.tiendahuertohogar.viewModel.QrViewModel
import com.example.tiendahuertohogar.ui.screens.PostScreen
import com.example.tiendahuertohogar.viewModel.PostViewModel
import com.example.tiendahuertohogar.ui.theme.ApiRestTheme

// --- IMPORTANTE: Asegúrate de importar tu pantalla de Carrito ---
// Si el archivo está en 'ui/screens', el import se verá así:
// import com.example.tiendahuertohogar.ui.screens.CarritoScreen
// O si aún no tienes el package definido, tal vez sea:
// import com.example.tiendahuertohogar.view.CarritoScreen

object AppRoutes {
    const val LOGIN = "login"
    const val PANTALLA_PRINCIPAL = "pantalla_principal"
    const val PRODUCTO_FORM = "producto_form"
    const val QR_SCANNER = "qr_scanner"
    const val REGISTRO = "registro"
    const val POSTS = "posts"
    const val CARRITO = "carrito" // <--- 1. NUEVA RUTA AGREGADA
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
                vm = loginViewModel
            )
        }

        composable(
            route = "${AppRoutes.PANTALLA_PRINCIPAL}/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username").orEmpty()
            MainScreen(
                mainNavController = navController,
                username = username,
                cartViewModel = viewModel<CartViewModel>()
            )
        }

        composable(AppRoutes.PRODUCTO_FORM) {
            ProductoFormScreen()
        }

        composable(AppRoutes.REGISTRO) {
            RegistroScreen(navController = navController)
        }

        composable(AppRoutes.QR_SCANNER) {
            val qrViewModel: QrViewModel = viewModel()
            QrScannerScreen(
                viewModel = qrViewModel,
                hasCameraPermission = hasCameraPermission,
                onRequestPermission = {
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            )
        }

        composable(AppRoutes.POSTS) {
            val postViewModel: PostViewModel = viewModel()
            ApiRestTheme {
                PostScreen(viewModel = postViewModel)
            }
        }

        // --- 2. COMPOSABLE AÑADIDO PARA EL CARRITO ---
        composable(AppRoutes.CARRITO) {
            // Aquí llamamos a tu pantalla CarritoScreen.
            // Si usas CarritoManager directamente como vimos antes:
            CarritoScreen(
                onCheckoutClick = {
                    Toast.makeText(context, "Procesando compra...", Toast.LENGTH_SHORT).show()
                    // Aquí podrías navegar a una pantalla de pago real o limpiar el carrito
                    // navController.navigate("pago")
                }
            )
        }
    }
}
