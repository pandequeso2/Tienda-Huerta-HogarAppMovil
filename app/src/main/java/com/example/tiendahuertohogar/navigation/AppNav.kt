package com.example.tiendahuertohogar.navigation

// Se han añadido y corregido las importaciones necesarias
import android.Manifest
import android.net.Uri
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
import com.example.tiendahuertohogar.ui.login.LoginScreen
import com.example.tiendahuertohogar.ui.view.HistorialPedidosScreen
import com.example.tiendahuertohogar.utils.CameraPermissionHelper
import com.example.tiendahuertohogar.view.PantallaPrincipal
import com.example.tiendahuertohogar.view.ProductoFormScreen
import com.example.tiendahuertohogar.view.QrScannerScreen
import com.example.tiendahuertohogar.viewmodel.HistorialPedidosViewModel
import com.example.tiendahuertohogar.ui.login.LoginViewModel


object AppRoutes {
    const val LOGIN = "login"
    const val PANTALLA_PRINCIPAL = "pantalla_principal"
    const val PRODUCTO_FORM = "producto_form"
    const val QR_SCANNER = "qr_scanner"
    const val HISTORIAL_PEDIDOS = "historial_pedidos"
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
                viewModel = loginViewModel
            )
        }

        composable(
            route = "${AppRoutes.PANTALLA_PRINCIPAL}/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username").orEmpty()
            PantallaPrincipal(navController = navController, username = username)
        }

        composable(
            route = "${AppRoutes.PRODUCTO_FORM}/{nombre}/{precio}",
            arguments = listOf(
                navArgument("nombre") { type = NavType.StringType },
                navArgument("precio") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre")?.let { Uri.decode(it) }.orEmpty()
            val precio = backStackEntry.arguments?.getString("precio")?.let { Uri.decode(it) }.orEmpty()
            ProductoFormScreen(navController = navController, nombre = nombre, precio = precio)
        }

//        composable(AppRoutes.QR_SCANNER) {
//            val qrViewModel: QrViewModel = viewModel()
        //          QrScannerScreen(
        //      navController = navController,
        //      viewModel = qrViewModel,
        //      hasCameraPermission = hasCameraPermission,
        //      onRequestPermission = { requestPermissionLauncher.launch(Manifest.permission.CAMERA) }
        //  )
        //}

        composable(
            route = "${AppRoutes.HISTORIAL_PEDIDOS}/{usuarioId}",
            arguments = listOf(navArgument("usuarioId") { type = NavType.LongType })
        ) { backStackEntry ->
            val usuarioId = backStackEntry.arguments?.getLong("usuarioId") ?: 0L

            val historialViewModel: HistorialPedidosViewModel = viewModel()

            HistorialPedidosScreen(
                navController = navController,
                usuarioId = usuarioId,
                viewModel = historialViewModel
            )
        }
    }
}
