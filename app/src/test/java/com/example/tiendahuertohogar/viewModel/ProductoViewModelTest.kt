package com.example.tiendahuertohogar.viewModel  // 'viewmodel' en minúscula

import com.example.tiendahuertohogar.data.model.Producto
import com.example.tiendahuertohogar.viewModel.ProductoViewModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class ProductoViewModelTest : StringSpec({

    val testDispatcher = StandardTestDispatcher()

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    "estado inicial de productos es una lista vacia" {
        val viewModel = ProductoViewModel()
        val listaActual = viewModel.productos.value
        listaActual.size shouldBe 0
    }

    "guardarProducto agrega correctamente un producto a la lista" {
        runTest(testDispatcher) {
            val viewModel = ProductoViewModel()

            val nuevoProducto = Producto(
                id = 1,
                codigo = "A001",
                categoria = "Hogar",
                nombre = "Mesa",
                precio = 50000.0,
                descripcion = "Mesa de madera",
                personalizable = false,
                imagenResId = 0
            )

            viewModel.guardarProducto(nuevoProducto)
            advanceUntilIdle()

            val listaActual = viewModel.productos.value
            listaActual.size shouldBe 1
            listaActual[0].nombre shouldBe "Mesa"
            listaActual[0] shouldBe nuevoProducto
        }
    }
})