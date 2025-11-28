package com.example.tiendahuertohogar.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendahuertohogar.data.model.Producto
import com.example.tiendahuertohogar.data.repository.ProductoRepository
import kotlinx.coroutines.CoroutineDispatcher // <-- ¡Nueva importación!
import kotlinx.coroutines.Dispatchers // <-- ¡Nueva importación!
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.stateIn

class ProductoViewModel(
    // 📢 1. Ponemos el Repositorio como argumento OBLIGATORIO al inicio.
    private val repository: ProductoRepository,
    // El Dispatcher como argumento opcional al final para tests.
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>> (emptyList())

    val productos: StateFlow<List<Producto>> = repository.obtenerProductos()
        .stateIn(
            scope = viewModelScope,
            // Deja que Room maneje la subscripción de forma eficiente.
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun guardarProducto(producto: Producto) {
        viewModelScope.launch(dispatcher) { // Usamos el dispatcher inyectado
            // Esta llamada guarda en Room.
            repository.insertarProducto(producto)


        }
    }
}