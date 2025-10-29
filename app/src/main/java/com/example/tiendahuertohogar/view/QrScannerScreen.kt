package com.example.tiendahuertohogar.view

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tiendahuertohogar.data.model.QrData // Importa el modelo
import com.example.tiendahuertohogar.navigation.AppRoutes
import com.example.tiendahuertohogar.utils.QrScanner
import com.example.tiendahuertohogar.viewModel.QrViewModel

@Composable
fun QrScannerScreen(
    navController: NavController,
    viewModel: QrViewModel,
    hasCameraPermission: Boolean,
    onRequestPermission: () -> Unit
) {
    // Ahora 'collectAsState' puede inferir el tipo correctamente (State<QrData?>)
    val qrResult by viewModel.qrResult.collectAsState()
    val context = LocalContext.current
    var isScanning by remember { mutableStateOf(true) }

    LaunchedEffect(qrResult) {
        // El tipo de 'it' ahora se infiere como 'QrData'
        qrResult?.let { data ->
            Toast.makeText(context, "Navegando a formulario...", Toast.LENGTH_SHORT).show()
            // Usamos 'data.nombre' y 'data.precio'
            navController.navigate(
                "${AppRoutes.PRODUCTO_FORM}/${Uri.encode(data.nombre)}/${Uri.encode(data.precio)}"
            )
            viewModel.clearResult()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (!hasCameraPermission) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Se necesita permiso para usar la cámara y escanear códigos QR.",
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onRequestPermission) {
                        Text("Conceder Permiso")
                    }
                }
            }
        } else if (isScanning) {
            Text(
                "Escanea un código QR",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                QrScanner(
                    onQrCodeScanned = { qrContent ->
                        viewModel.onQrDetected(qrContent)
                        isScanning = false
                    },
                    modifier = Modifier.fillMaxSize()
                )

                Surface(
                    modifier = Modifier
                        .size(250.dp)
                        .align(Alignment.Center),
                    color = Color.Transparent,
                    shape = MaterialTheme.shapes.medium,
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                ) {}
            }
        } else {
            Text("Procesando QR...", style = MaterialTheme.typography.titleLarge)
        }
    }
}
