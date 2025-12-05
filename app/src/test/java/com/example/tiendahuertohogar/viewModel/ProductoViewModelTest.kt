package com.example.tiendahuertohogar.viewmodel

import com.example.tiendahuertohogar.data.model.Producto
import com.example.tiendahuertohogar.data.repository.ProductoRepository
import com.example.tiendahuertohogar.viewModel.ProductoViewModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic // 📢 Necesario para simular Dispatchers
import io.mockk.unmockkStatic // 📢 Necesario para limpiar el mock
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class ProductoViewModelTest : StringSpec({ // <--- ¡Esta es la llave que debe contener todo!

    val testDispatcher = StandardTestDispatcher()

    // Declaraciones sin 'private'
    @MockK
    lateinit var mockRepository: ProductoRepository

    val mockProductsFlow = MutableStateFlow<List<Producto>>(emptyList())

    beforeSpec {
        // SOLUCIÓN CRÍTICA: Aseguramos que Dispatchers.Main sea mockeado
        mockkStatic(Dispatchers::class)
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)

        // Simulación de función suspendida: coEvery en contexto seguro
        coEvery { mockRepository.insertarProducto(any()) } returns Unit

        // Simulación de función normal
        every { mockRepository.obtenerProductos() } returns mockProductsFlow
    }

    afterSpec {
        Dispatchers.resetMain()
        unmockkStatic(Dispatchers::class) // Limpiamos el mock
    }

    // --- Test 1 ---
    "estado inicial de productos es una lista vacia" {
        val viewModel = ProductoViewModel(repository = mockRepository)
        val listaActual = viewModel.productos.value
        listaActual.size shouldBe 0
    }

    // --- Test 2 ---
    "guardarProducto llama al repository y actualiza la lista" {
        runTest(testDispatcher) {

            val viewModel = ProductoViewModel(
                repository = mockRepository,
                dispatcher = testDispatcher
            )

            val nuevoProducto = Producto(
                id = 1, codigo = "A001", categoria = "Hogar", nombre = "Mesa",
                precio = 50000.0, descripcion = "Mesa de madera",
                personalizable = false, imagenResId = 0
            )

            viewModel.guardarProducto(nuevoProducto)

            advanceUntilIdle()

            // Verificamos que se llamó a la función de inserción (cualquier producto)
            // ✅ FIX: Use 'coVerify' for suspend functions
            io.mockk.coVerify(exactly = 1) { mockRepository.insertarProducto(any()) }


            // Simular actualización del Flow y verificación
            mockProductsFlow.value = listOf(nuevoProducto)
            advanceUntilIdle()

            viewModel.productos.value.size shouldBe 1
            viewModel.productos.value.first() shouldBe nuevoProducto
        }
    }
}) // <--- ¡Esta es la llave que debe cerrar el archivo!