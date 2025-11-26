package com.example.tiendahuertohogar.viewmodel

import com.example.tiendahuertohogar.data.model.Producto
import com.example.tiendahuertohogar.viewModel.ProductoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductoViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `estado inicial de productos es una lista vacia`() = runTest(testDispatcher) {
        // GIVEN: Instanciamos el ViewModel
        val viewModel = ProductoViewModel()

        // WHEN: Consultamos el valor inicial
        val listaActual = viewModel.productos.value

        // THEN: Debería estar vacía
        assertEquals(0, listaActual.size)
    }

    @Test
    fun `guardarProducto agrega correctamente un producto a la lista`() = runTest(testDispatcher) {
        // GIVEN: El ViewModel y un producto de prueba
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

        // WHEN: Llamamos a la función guardarProducto
        viewModel.guardarProducto(nuevoProducto)

        // Esperamos a que termine la corrutina
        advanceUntilIdle()

        // THEN: Verificamos que la lista ahora tenga 1 elemento
        val listaActual = viewModel.productos.value

        assertEquals(1, listaActual.size)
        assertEquals("Mesa", listaActual[0].nombre)
        assertEquals(nuevoProducto, listaActual[0])
    }
}