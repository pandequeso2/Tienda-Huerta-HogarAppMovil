package com.example.tiendahuertohogar.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendahuertohogar.data.EstadoDataStore // Ajusta la importación según tu estructura
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// ViewModel extendido de AndroidViewModel para usar el contexto [cite: 63, 64]
class EstadoViewModel(application: Application): AndroidViewModel(application) {

    // DataStore creado con contexto de aplicación [cite: 65, 66]
    private val estadoDataStore = EstadoDataStore(context = application)

    // Estado principal: si está activado o no (observable). Null para indicar que está cargando. [cite: 69, 70, 71, 72]
    private val _activo = MutableStateFlow<Boolean?>(value = null)
    val activo: StateFlow<Boolean?> = _activo

    // Estado para mostrar u ocultar el mensaje animado [cite: 75, 77]
    private val _mostrarMensaje = MutableStateFlow(value = false)
    val mostrarMensaje: StateFlow<Boolean> = _mostrarMensaje

    init {
        // Al iniciar el ViewModel, cargamos el estado desde DataStore [cite: 78, 79]
        cargarEstado()
    }

    fun cargarEstado() {
        viewModelScope.launch {
            // Simula demora para mostrar Loader (opcional) [cite: 83, 84]
            delay(timeMillis = 1500)

            // Obtenemos el valor guardado, si es null, asumimos 'false' [cite: 85, 86]
            _activo.value = estadoDataStore.obtenerEstado().first() ?: false
        }
    }

    fun alternarEstado() {
        viewModelScope.launch {
            // Alternamos el valor actual [cite: 92]
            val nuevoValor = !(_activo.value ?: false)

            // Guardamos en DataStore [cite: 94, 95]
            estadoDataStore.guardarEstado(nuevoValor)

            // Actualizamos el flujo [cite: 102, 103]
            _activo.value = nuevoValor

            // Mostramos el mensaje visual animado [cite: 104]
            _mostrarMensaje.value = true

            // Ocultamos después de 2 segundos [cite: 105]
            delay(timeMillis = 2000)
            _mostrarMensaje.value = false
        }
    }
}