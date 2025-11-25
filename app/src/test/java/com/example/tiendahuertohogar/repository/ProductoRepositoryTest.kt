package com.example.tiendahuertohogar.repository

import com.example.tiendahuertohogar.data.dao.ProductoDao
import com.example.tiendahuertohogar.data.model.Producto
import com.example.tiendahuertohogar.data.repository.ProductoRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ProductoRepositoryTest {

    // 1. Mockeamos el DAO (Base de datos local)
    private val productoDao = mockk<ProductoDao>()

    // 2. Instanciamos el repositorio pasándole el DAO simulado
    private val repository = ProductoRepository(productoDao)

    @Test
    fun `obtenerProductos devuelve flujo de lista vacia`() = runTest {
        // GIVEN: El DAO devuelve un Flow con una lista vacía
        coEvery { productoDao.obtenerProductos() } returns flowOf(emptyList())

        // WHEN: Llamamos al repositorio y recolectamos el primer valor del Flow
        val resultado = repository.obtenerProductos().first()

        // THEN: La lista debe estar vacía
        assertEquals(0, resultado.size)

        // Verificamos que se llamó al DAO
        coVerify(exactly = 1) { productoDao.obtenerProductos() }
    }

    @Test
    fun `obtenerProductos devuelve flujo con datos correctamente`() = runTest {
        // GIVEN: Datos ficticios
        val listaFicticia = listOf(
            Producto(1, "COD1", "Frutas", "Manzana", 1500.0, "Roja", false),
            Producto(2, "COD2", "Frutas", "Pera", 1200.0, "Verde", false)
        )

        // El DAO devuelve un Flow que emite esta lista
        coEvery { productoDao.obtenerProductos() } returns flowOf(listaFicticia)

        // WHEN: Obtenemos los productos del repositorio
        val resultado = repository.obtenerProductos().first()

        // THEN: Verificamos los datos
        assertEquals(2, resultado.size)
        assertEquals("Manzana", resultado[0].nombre)
        assertEquals(1500.0, resultado[0].precio, 0.0)

        coVerify(exactly = 1) { productoDao.obtenerProductos() }
    }

    @Test
    fun `insertarProducto llama al DAO correctamente`() = runTest {
        // GIVEN: Un producto nuevo
        val nuevoProducto = Producto(3, "COD3", "Verdura", "Lechuga", 800.0, "Fresca", false)

        // Le decimos al mock que 'insertarProducto' no hace nada (solo corre)
        coEvery { productoDao.insertarProducto(any()) } returns Unit

        // WHEN: Llamamos a insertar en el repositorio
        repository.insertarProducto(nuevoProducto)

        // THEN: Verificamos que el DAO recibió la orden de insertar ese producto específico
        coVerify(exactly = 1) { productoDao.insertarProducto(nuevoProducto) }
    }
}
