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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.tiendahuertohogar.data.database.ProductoDataBase
// --- !! IMPORTACIONES AÑADIDAS !! ---
import com.example.tiendahuertohogar.data.repository.PedidoRepository
import com.example.tiendahuertohogar.data.repository.ProductoRepository
import com.example.tiendahuertohogar.ui.login.LoginScreen
import com.example.tiendahuertohogar.view.HistorialPedidosScreen // Corregida la ruta a 'view'
import com.example.tiendahuertohogar.utils.CameraPermissionHelper
import com.example.tiendahuertohogar.view.DetalleProductoScreen // Importa la nueva pantalla
import com.example.tiendahuertohogar.view.PantallaPrincipal
import com.example.tiendahuertohogar.view.ProductoFormScreen
// import com.example.tiendahuertohogar.view.QrScannerScreen // IGNORADO
import com.example.tiendahuertohogar.viewmodel.HistorialPedidosViewModel
// --- !! IMPORTACIÓN AÑADIDA !! ---
import com.example.tiendahuertohogar.ui.login.LoginViewModel
import com.example.tiendahuertohogar.viewmodel.ProductoViewModel
import com.example.tiendahuertohogar.viewmodel.ProductoViewModelFactory
// import com.example.tiendahuertohogar.viewmodel.QrViewModel // <-- IGNORADO


object AppRoutes {
    const val LOGIN = "login"
    const val PANTALLA_PRINCIPAL = "pantalla_principal"
    const val PRODUCTO_FORM = "producto_form"
    const val QR_SCANNER = "qr_scanner"
    const val HISTORIAL_PEDIDOS = "historial_pedidos"
    // --- !! NUEVA RUTA !! ---
    const val DETALLE_PRODUCTO = "detalle_producto"
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

        // --- !! SECCIÓN QR COMENTADA PARA IGNORARLA !! ---
        /*
        composable(AppRoutes.QR_SCANNER) {
            val qrViewModel: QrViewModel = viewModel()
                  QrScannerScreen(
              navController = navController,
              viewModel = qrViewModel,
              hasCameraPermission = hasCameraPermission,
              onRequestPermission = { requestPermissionLauncher.launch(Manifest.permission.CAMERA) }
          )
        }
        */

        // --- !! NUEVO COMPOSABLE PARA LA PANTALLA DE DETALLE !! ---
        composable(
            route = "${AppRoutes.DETALLE_PRODUCTO}/{productoId}",
            arguments = listOf(navArgument("productoId") { type = NavType.LongType })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getLong("productoId") ?: 0L

            // Necesitamos instanciar el ViewModel aquí también
            val scope = rememberCoroutineScope()
            // Asegúrate de que el contexto usado sea 'applicationContext' para la BD
            val database = ProductoDataBase.getDatabase(context.applicationContext, scope)
            val productoRepository = ProductoRepository(database.productoDao())
            val productoViewModelFactory = ProductoViewModelFactory(productoRepository)
            val productoViewModel: ProductoViewModel = viewModel(factory = productoViewModelFactory)

            DetalleProductoScreen(
                navController = navController,
                viewModel = productoViewModel,
                productoId = productoId
            )
        }

        composable(
            route = "${AppRoutes.HISTORIAL_PEDIDOS}/{usuarioId}",
            arguments = listOf(navArgument("usuarioId") { type = NavType.LongType })
        ) { backStackEntry ->
            val usuarioId = backStackEntry.arguments?.getLong("usuarioId") ?: 0L

            // --- !! CORRECCIÓN AQUÍ !! ---
            // HistorialPedidosViewModel SÍ tiene dependencias (PedidoRepository).
            // Necesitamos instanciarlo usando su Factory.

            // 1. Crear dependencias
            val scope = rememberCoroutineScope()
            val database = ProductoDataBase.getDatabase(context.applicationContext, scope)
            val pedidoRepository = PedidoRepository(database.pedidoDao()) // <-- Necesita el PedidoDAO
            val historialViewModelFactory = HistorialPedidosViewModelFactory(pedidoRepository) // <-- Usar la Factory

            // 2. Instanciar el ViewModel con la Factory
            val historialViewModel: HistorialPedidosViewModel = viewModel(factory = historialViewModelFactory)

            HistorialPedidosScreen(
                navController = navController,
                usuarioId = usuarioId,
                viewModel = historialViewModel
            )
        }
    }
}