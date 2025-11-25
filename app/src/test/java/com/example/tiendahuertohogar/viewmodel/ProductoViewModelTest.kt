package com.example.tiendahuertohogar.viewModel

import com.example.tiendahuertohogar.data.model.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductoViewModelTest {

    // 1. Configuración del Dispatcher de prueba
    // Esto es necesario porque el ViewModel usa 'viewModelScope'
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        // Reemplazamos el hilo Main de Android por nuestro dispatcher de prueba
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        // Restauramos el hilo original al finalizar cada test
        Dispatchers.resetMain()
    }

    @Test
    fun `estado inicial de productos es una lista vacia`() {
        // GIVEN: Instanciamos el ViewModel
        val viewModel = ProductoViewModel()

        // WHEN: Consultamos el valor inicial (sin hacer nada más)
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
            precio = 50000.0, // Asegúrate de usar Double aquí
            descripcion = "Mesa de madera",
            personalizable = false
        )

        // WHEN: Llamamos a la función guardarProducto
        viewModel.guardarProducto(nuevoProducto)

        // IMPORTANTE: Como guardarProducto usa una corrutina (launch),
        // debemos esperar a que termine antes de verificar.
        advanceUntilIdle()

        // THEN: Verificamos que la lista ahora tenga 1 elemento y sea el correcto
        val listaActual = viewModel.productos.value

        assertEquals(1, listaActual.size)
        assertEquals("Mesa", listaActual[0].nombre)
        assertEquals(nuevoProducto, listaActual[0])
    }
}
