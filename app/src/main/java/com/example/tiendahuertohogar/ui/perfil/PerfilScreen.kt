package com.example.tiendahuertohogar.ui.perfil


import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tiendahuertohogar.R
import com.example.tiendahuertohogar.data.model.Usuario
import com.example.tiendahuertohogar.utils.SessionManager

@Composable
fun PerfilScreen(mainNavController: NavController) {
    val viewModel: PerfilViewModel = viewModel()
    val usuario by viewModel.user.collectAsStateWithLifecycle()
    val photoUri by viewModel.photoUri.collectAsStateWithLifecycle()

    val navBackStackEntry = mainNavController.currentBackStackEntry
    LaunchedEffect(navBackStackEntry) {
        val uri = navBackStackEntry?.savedStateHandle?.get<Uri>("photo_uri")
        uri?.let {
            viewModel.updatePhotoUri(it)
            navBackStackEntry.savedStateHandle.remove<Uri>("photo_uri")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            // CORREGIDO: Usa el color de fondo del tema (BlancoSuave)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileHeader(
            usuario = usuario,
            photoUri = photoUri,
            // Inicia el flujo del recurso nativo (cámara).
            onImageClick = { mainNavController.navigate("tomar_foto") }
        )

        Spacer(modifier = Modifier.height(24.dp))

        UserInfoSection(usuario = usuario)

        Spacer(modifier = Modifier.weight(1f))

        LogoutButton(navController = mainNavController)
    }
}

@Composable
fun ProfileHeader(usuario: Usuario?, photoUri: Uri?, onImageClick: () -> Unit) {
    Spacer(modifier = Modifier.height(48.dp))

    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            // CORREGIDO: Usa un color del tema
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable(onClick = onImageClick),
        contentAlignment = Alignment.Center
    ) {
        // Carga la imagen desde la URI que entrega la cámara.
        AsyncImage(
            model = photoUri,
            contentDescription = "Foto de perfil",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
            error = painterResource(id = R.drawable.ic_launcher_foreground)
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = usuario?.nombreCompleto ?: "Cargando nombre...",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )

    Text(
        text = usuario?.correo ?: "Cargando correo...",
        fontSize = 16.sp,
        // CORREGIDO: Usa un color del tema (GrisMedio)
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

// Organización: Componente modular para la info del usuario.
@Composable
fun UserInfoSection(usuario: Usuario?) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        UserInfoRow(
            icon = Icons.Default.Phone,
            label = "Teléfono",
            value = usuario?.telefono ?: "No especificado"
        )
        UserInfoRow(
            icon = Icons.Default.LocationOn,
            label = "Dirección",
            // Validación en UI: Se manejan datos nulos para evitar errores.
            value = usuario?.let { "${it.comuna}, ${it.region}" } ?: "No especificada"
        )
        UserInfoRow(
            icon = Icons.Default.Email,
            label = "Correo",
            value = usuario?.correo ?: "No especificado"
        )
    }
}

// Organización: Componente reutilizable para mostrar un dato.
@Composable
fun UserInfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            // CORREGIDO: Usa un color del tema (GrisMedio)
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                // CORREGIDO: Usa un color del tema (GrisMedio)
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
    Divider()
}


@Composable
fun LogoutButton(navController: NavController) {
    val context = LocalContext.current
    Button(
        onClick = {
            SessionManager.clearSession(context)
            navController.navigate("login") { popUpTo(0) }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Cerrar Sesión")
        Spacer(modifier = Modifier.width(8.dp))
        Text("Cerrar Sesión")
    }
}