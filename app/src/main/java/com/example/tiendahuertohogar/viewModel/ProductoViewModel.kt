// viewModel/ProductoViewModel.kt
package com.example.tiendahuertohogar.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendahuertohogar.data.model.Producto
import com.example.tiendahuertohogar.data.repository.ProductoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductoViewModel @Inject constructor(
    private val productoRepository: ProductoRepository
) : ViewModel() {

    // La función que tu formulario necesita
    fun addProducto(producto: Producto) {
        viewModelScope.launch {
            productoRepository.insert(producto)
        }
    }
}
