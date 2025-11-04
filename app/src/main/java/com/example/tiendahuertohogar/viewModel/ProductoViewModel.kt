package com.example.tiendahuertohogar.viewmodel

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

// --- 1. Estado de la UI ---
data class CatalogoUiState(
    val productos: List<Producto> = emptyList(),
    val isLoading: Boolean = true // Empezamos cargando
)

// --- 2. El ViewModel ---
class ProductoViewModel(private val repository: ProductoRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogoUiState())
    val uiState: StateFlow<CatalogoUiState> = _uiState.asStateFlow()

    init {
        // Observamos el Flow de la base de datos.
        // Usamos 'repository.allProductos' que tú mismo definiste.
        viewModelScope.launch {
            repository.allProductos.collect { listaProductos ->
                _uiState.value = CatalogoUiState(
                    productos = listaProductos,
                    isLoading = false
                )
            }
        }
    }

    // Aquí puedes añadir más funciones si las necesitas (insert, delete, etc.)
}

// --- 3. La Factory ---
// (Probablemente ya tienes una, pero así debería ser)
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