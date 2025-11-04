package com.example.tiendahuertohogar.viewmodel // O 'viewModel' si es minúscula

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tiendahuertohogar.data.model.Producto
import com.example.tiendahuertohogar.data.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

// --- 1. Estado de la UI (para el catálogo) ---
data class CatalogoUiState(
    val productos: List<Producto> = emptyList(),
    val isLoading: Boolean = true
)

// --- 2. El ViewModel ---
class ProductoViewModel(private val repository: ProductoRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogoUiState())
    val uiState: StateFlow<CatalogoUiState> = _uiState.asStateFlow()

    init {
        // Esto se encarga de cargar la lista para el CatalogoScreen
        viewModelScope.launch {
            repository.allProductos.collect { listaProductos ->
                _uiState.value = CatalogoUiState(
                    productos = listaProductos,
                    isLoading = false
                )
            }
        }
    }

    // --- !!! FUNCIÓN AÑADIDA !!! ---
    /**
     * Guarda un nuevo producto en la base de datos.
     * Es llamada por ProductoFormScreen.
     */
    fun guardarProducto(producto: Producto) {
        viewModelScope.launch {
            repository.insert(producto)
        }
    }
}

// --- 3. La Factory ---
class ProductoViewModelFactory(
    private val repository: ProductoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}