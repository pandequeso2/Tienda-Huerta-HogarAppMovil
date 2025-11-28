package com.example.tiendahuertohogar.repository

import com.example.tiendahuertohogar.data.dao.ProductoDao // Importa tu DAO
import com.example.tiendahuertohogar.data.repository.ProductoRepository // Importa tu Repositorio
import com.example.tiendahuertohogar.data.model.Producto
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk // Necesitas mockk para simular el DAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class ProductoRepositoryTest : StringSpec({

    val testDispatcher = StandardTestDispatcher()

    // 1. Mockeamos la única dependencia que pide el constructor (ProductoDao)
    val mockProductoDao = mockk<ProductoDao>(relaxed = true)

    // 2. Instanciamos el Repositorio, inyectando el mock. (Esto resuelve el error de compilación)
    val repository = ProductoRepository(productoDao = mockProductoDao)

    // Configuración del dispatcher para las coroutines
    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    // Data dummy de prueba
    val dummyProducto = Producto(
        id = 1, codigo = "A001", categoria = "Hogar",
        nombre = "Mesa", precio = 50000.0,
        descripcion = "Mesa de madera", personalizable = false, imagenResId = 0
    )

    // -------------------------------------------------------------------

    "insertarProducto llama al método insertarProducto del DAO" {
        runTest(testDispatcher) {

            // 📢 1. Definimos qué debe pasar cuando se llama al método del DAO
            // Usamos coEvery porque la función 'insertarProducto' es suspend.
            coEvery { mockProductoDao.insertarProducto(dummyProducto) } returns Unit

            // 📢 2. Ejecutamos la función del Repositorio
            repository.insertarProducto(dummyProducto)

            // 📢 3. Verificamos que el Repositorio haya llamado al DAO
            coVerify(exactly = 1) { mockProductoDao.insertarProducto(dummyProducto) }
        }
    }

    "obtenerProductos devuelve el Flow de la lista que entrega el DAO" {
        runTest(testDispatcher) {
            val listaProductos = listOf(dummyProducto)

            // 📢 1. Definimos qué debe devolver el DAO (un Flow)
            coEvery { mockProductoDao.obtenerProductos() } returns flowOf(listaProductos)

            // 📢 2. Ejecutamos la función y obtenemos el primer valor del Flow
            val result = repository.obtenerProductos().first()

            // 📢 3. Verificamos el resultado
            result shouldBe listaProductos
        }
    }

    // Puedes seguir con tests para obtenerProductoPorId y eliminarProducto...

})