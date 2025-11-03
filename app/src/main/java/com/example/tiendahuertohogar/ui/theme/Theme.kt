
package com.example.tiendahuertohogar.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = VerdeEsmeralda,
    onPrimary = BlancoNieve,
    primaryContainer = VerdeEsmeraldaOscuro,
    onPrimaryContainer = VerdeEsmeraldaClaro,

    secondary = AmarilloMostaza,
    onSecondary = GrisOscuro,
    secondaryContainer = Color(0xFFFFE082),
    onSecondaryContainer = GrisOscuro,

    tertiary = MarronClaro,
    onTertiary = BlancoNieve,
    tertiaryContainer = Color(0xFF6D3410),
    onTertiaryContainer = Color(0xFFD4A574),

    error = RojoError,
    onError = BlancoNieve,
    errorContainer = Color(0xFFFFCDD2),
    onErrorContainer = Color(0xFFB71C1C),

    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE6E1E5),

    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0),
    outline = Color(0xFF938F99),
)

private val LightColorScheme = lightColorScheme(
    primary = VerdeEsmeralda,
    onPrimary = BlancoNieve,
    primaryContainer = VerdeEsmeraldaClaro,
    onPrimaryContainer = VerdeEsmeraldaOscuro,

    secondary = AmarilloMostaza,
    onSecondary = GrisOscuro,
    secondaryContainer = Color(0xFFFFE57F),
    onSecondaryContainer = GrisOscuro,

    tertiary = MarronClaro,
    onTertiary = BlancoNieve,
    tertiaryContainer = Color(0xFFD4A574),
    onTertiaryContainer = Color(0xFF4A2511),

    error = RojoError,
    onError = BlancoNieve,
    errorContainer = Color(0xFFFFCDD2),
    onErrorContainer = Color(0xFFB71C1C),

    background = BlancoSuave,
    onBackground = GrisOscuro,
    surface = BlancoNieve,
    onSurface = GrisOscuro,

    surfaceVariant = GrisClaro,
    onSurfaceVariant = GrisMedio,
    outline = Color(0xFF999999),

    inverseSurface = GrisOscuro,
    inverseOnSurface = BlancoSuave,
    inversePrimary = VerdeEsmeraldaClaro,
)

@Composable
fun TiendaHuertoHogarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Deshabilitado para usar colores personalizados
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}