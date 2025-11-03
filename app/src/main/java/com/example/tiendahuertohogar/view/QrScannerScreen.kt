package com.example.tiendahuertohogar.view

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tiendahuertohogar.data.model.QrData
import com.example.tiendahuertohogar.navigation.AppRoutes
import com.example.tiendahuertohogar.ui.theme.*
import com.example.tiendahuertohogar.utils.QrScanner
import com.example.tiendahuertohogar.viewModel.QrViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrScannerScreen(
    navController: NavController,
    viewModel: QrViewModel,
    hasCameraPermission: Boolean,
    onRequestPermission: () -> Unit
) {
    val qrResult by viewModel.qrResult.collectAsState()
    val context = LocalContext.current
    var isScanning by remember { mutableStateOf(true) }
    var showInstructions by remember { mutableStateOf(true) }

    LaunchedEffect(qrResult) {
        qrResult?.let { data ->
            Toast.makeText(
                context,
                "¡Código QR detectado! Producto: ${data.nombre}",
                Toast.LENGTH_SHORT
            ).show()
            navController.navigate(
                "${AppRoutes.PRODUCTO_FORM}/${Uri.encode(data.nombre)}/${Uri.encode(data.precio)}"
            )
            viewModel.clearResult()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Escanear Producto",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = BlancoNieve
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VerdeEsmeralda,
                    titleContentColor = BlancoNieve
                )
            )
        },
        containerColor = Color.Black
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (!hasCameraPermission) {
                // Pantalla de solicitud de permisos
                PermissionRequestScreen(onRequestPermission = onRequestPermission)
            } else if (isScanning) {
                // Pantalla del scanner
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Área del scanner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        // Visor de la cámara
                        QrScanner(
                            onQrCodeScanned = { qrContent ->
                                viewModel.onQrDetected(qrContent)
                                isScanning = false
                            },
                            modifier = Modifier.fillMaxSize()
                        )

                        // Overlay con marco de escaneo
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // Marco de escaneo
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f),
                                color = Color.Transparent,
                                shape = RoundedCornerShape(24.dp),
                                border = BorderStroke(4.dp, VerdeEsmeralda)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    // Línea de escaneo animada (opcional)
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(3.dp)
                                            .background(
                                                brush = Brush.horizontalGradient(
                                                    colors = listOf(
                                                        Color.Transparent,
                                                        VerdeEsmeralda,
                                                        Color.Transparent
                                                    )
                                                )
                                            )
                                    )
                                }
                            }

                            // Esquinas del marco (decorativas)
                            ScannerCorners()
                        }

                        // Instrucciones
                        if (showInstructions) {
                            Surface(
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .padding(top = 32.dp)
                                    .shadow(4.dp, shape = RoundedCornerShape(24.dp)),
                                shape = RoundedCornerShape(24.dp),
                                color = BlancoNieve.copy(alpha = 0.95f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Icon(
                                        Icons.Default.QrCodeScanner,
                                        contentDescription = null,
                                        tint = VerdeEsmeralda,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Column {
                                        Text(
                                            "Apunta al código QR",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.SemiBold,
                                            color = GrisOscuro
                                        )
                                        Text(
                                            "Se detectará automáticamente",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = GrisMedio
                                        )
                                    }
                                    IconButton(
                                        onClick = { showInstructions = false },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Text("✕", color = GrisMedio)
                                    }
                                }
                            }
                        }
                    }

                    // Panel inferior con información
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = BlancoSuave,
                        shadowElevation = 8.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = VerdeEsmeralda,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "Escanea el código QR del producto",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = GrisOscuro,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "El código debe contener el nombre y precio del producto",
                                style = MaterialTheme.typography.bodyMedium,
                                color = GrisMedio,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                // Estado de procesamiento
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BlancoSuave),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator(
                            color = VerdeEsmeralda,
                            modifier = Modifier.size(64.dp),
                            strokeWidth = 4.dp
                        )
                        Text(
                            "Procesando código QR...",
                            style = MaterialTheme.typography.titleMedium,
                            color = GrisOscuro
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PermissionRequestScreen(onRequestPermission: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlancoSuave),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .shadow(4.dp, shape = RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = BlancoNieve
            )
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = RoundedCornerShape(40.dp),
                    color = VerdeEsmeralda.copy(alpha = 0.1f)
                ) {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = VerdeEsmeralda,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                    )
                }

                Text(
                    "Permiso de Cámara",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = GrisOscuro
                )

                Text(
                    "Para escanear códigos QR de productos, necesitamos acceso a tu cámara.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrisMedio,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onRequestPermission,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdeEsmeralda
                    )
                ) {
                    Text(
                        "Permitir Acceso",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun ScannerCorners() {
    // Esquinas decorativas del marco de escaneo
    val cornerSize = 40.dp
    val cornerThickness = 4.dp

    Box(modifier = Modifier.fillMaxSize()) {
        // Esquina superior izquierda
        Surface(
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(cornerSize),
            color = VerdeEsmeralda,
            shape = RoundedCornerShape(topStart = 24.dp)
        ) {}

        // Esquina superior derecha
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(cornerSize),
            color = VerdeEsmeralda,
            shape = RoundedCornerShape(topEnd = 24.dp)
        ) {}

        // Esquina inferior izquierda
        Surface(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(cornerSize),
            color = VerdeEsmeralda,
            shape = RoundedCornerShape(bottomStart = 24.dp)
        ) {}

        // Esquina inferior derecha
        Surface(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(cornerSize),
            color = VerdeEsmeralda,
            shape = RoundedCornerShape(bottomEnd = 24.dp)
        ) {}
    }
}