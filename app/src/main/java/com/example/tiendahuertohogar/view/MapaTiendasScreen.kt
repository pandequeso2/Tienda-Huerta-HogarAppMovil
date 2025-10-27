// ui/view/MapaTiendasScreen.kt
package com.example.tiendahuertohogar.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MapaTiendasScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // AQUÍ IRÍA EL COMPOSABLE DE GOOGLE MAPS
        // Por ejemplo:
        // GoogleMap(
        //     modifier = Modifier.fillMaxSize(),
        //     cameraPositionState = rememberCameraPositionState {
        //         position = CameraPosition.fromLatLngZoom(santiago, 10f)
        //     }
        // ) {
        //     Marker(state = MarkerState(position = concepcion), title = "Concepción")
        //     Marker(state = MarkerState(position = valparaiso), title = "Valparaíso")
        //     // ... añadir más marcadores para las otras ciudades
        // }
        Text("Aquí se mostrará el mapa con las ubicaciones.")
    }
}