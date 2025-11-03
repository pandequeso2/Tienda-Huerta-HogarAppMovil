package com.example.tiendahuertohogar.view

import android.app.Application
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tiendahuertohogar.navigation.AppRoutes
import com.example.tiendahuertohogar.ui.theme.*
import com.example.tiendahuertohogar.viewModel.EstadoViewModel
import kotlinx.coroutines.launch

data class MenuOption(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val color: Color,
    val route: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(
    navController: NavController,
    username: String
) {
    // Obtener el contexto y crear el ViewModel con la Application
    val context = LocalContext.current
    val viewModel: EstadoViewModel = viewModel(
        factory = androidx.lifecycle.viewmodel.compose.viewModel<EstadoViewModel>(
            factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return EstadoViewModel(context.applicationContext as android.app.Application) as T
                }
            }
        ).javaClass.kotlin.objectInstance as androidx.lifecycle.ViewModelProvider.Factory
    )
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val menuOptions = listOf(
        MenuOption(
            "Catálogo",
            "Explora productos",
            Icons.Default.ShoppingCart,
            VerdeEsmeralda
        ),
        MenuOption(
            "Escanear QR",
            "Agrega productos",
            Icons.Default.QrCodeScanner,
            AmarilloMostaza,
            AppRoutes.QR_SCANNER
        ),
        MenuOption(
            "Mis Pedidos",
            "Historial de compras",
            Icons.Default.Receipt,
            MarronClaro,
            "${AppRoutes.HISTORIAL_PEDIDOS}/1"
        ),
        MenuOption(
            "Mi Perfil",
            "Datos personales",
            Icons.Default.Person,
            VerdeFrutas
        ),
        MenuOption(
            "Ubicaciones",
            "Nuestras tiendas",
            Icons.Default.LocationOn,
            AzulLacteos
        ),
        MenuOption(
            "Favoritos",
            "Productos guardados",
            Icons.Default.Favorite,
            RojoError
        )
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = BlancoSuave
            ) {
                DrawerMenu(username = username, navController = navController)
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                "HuertoHogar",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = BlancoNieve
                            )
                            Text(
                                "¡Hola, $username!",
                                style = MaterialTheme.typography.bodySmall,
                                color = BlancoNieve.copy(alpha = 0.8f)
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menú",
                                tint = BlancoNieve
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* TODO: Notificaciones */ }) {
                            Badge(
                                containerColor = AmarilloMostaza
                            ) {
                                Text("3")
                            }
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Notificaciones",
                                tint = BlancoNieve
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = VerdeEsmeralda
                    )
                )
            },
            containerColor = BlancoSuave
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Banner superior
                BannerPromocional()

                // Grid de opciones
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(menuOptions.size) { index ->
                        val option = menuOptions[index]
                        MenuCard(
                            option = option,
                            onClick = {
                                if (option.route.isNotEmpty()) {
                                    navController.navigate(option.route)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BannerPromocional() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(140.dp)
            .shadow(4.dp, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            VerdeEsmeralda,
                            VerdeEsmeraldaClaro
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text(
                    "🌱 Productos Frescos",
                    style = MaterialTheme.typography.headlineSmall,
                    color = BlancoNieve,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Del campo a tu mesa\ncon la mejor calidad",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BlancoNieve.copy(alpha = 0.9f)
                )
            }

            Text(
                "🥑",
                style = MaterialTheme.typography.displayLarge,
                fontSize = 80.sp,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = 10.dp)
            )
        }
    }
}

@Composable
fun MenuCard(
    option: MenuOption,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .shadow(2.dp, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = BlancoNieve
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier
                    .size(64.dp),
                shape = RoundedCornerShape(32.dp),
                color = option.color.copy(alpha = 0.15f)
            ) {
                Icon(
                    imageVector = option.icon,
                    contentDescription = option.title,
                    modifier = Modifier.padding(16.dp),
                    tint = option.color
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = option.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = GrisOscuro
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = option.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = GrisMedio
            )
        }
    }
}