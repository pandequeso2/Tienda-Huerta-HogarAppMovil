package com.example.tiendahuertohogar.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendahuertohogar.data.model.Producto
import kotlinx.coroutines.CoroutineDispatcher // <-- ¡Nueva importación!
import kotlinx.coroutines.Dispatchers // <-- ¡Nueva importación!
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductoViewModel(
    // 📢 CAMBIO CLAVE: Permite inyectar un dispatcher, por defecto usa Dispatchers.IO
    // (o el que sea apropiado para operaciones de datos) o Dispatchers.Main.
    // Usaremos Main como fallback, pero IO sería mejor si fuera una BD real.
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>> (emptyList())

    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    fun guardarProducto(producto: Producto){
        // 📢 Ahora usamos el dispatcher inyectado para lanzar la coroutine.
        viewModelScope.launch(dispatcher){
            // Esto garantiza que en el test, esta coroutine usará el testDispatcher.
            val nuevaLista  = _productos.value + producto
            _productos.value = nuevaLista
        }
    }
}