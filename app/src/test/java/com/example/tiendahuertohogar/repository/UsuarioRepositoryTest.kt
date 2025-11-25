package com.example.tiendahuertohogar.repository

import com.example.tiendahuertohogar.data.dao.UsuarioDao
import com.example.tiendahuertohogar.data.model.Usuario
import com.example.tiendahuertohogar.data.repository.UsuarioRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class UsuarioRepositoryTest {

    // 1. Mockeamos el DAO
    private val usuarioDao = mockk<UsuarioDao>()

    // 2. Instanciamos el repositorio inyectando el mock
    private val repository = UsuarioRepository(usuarioDao)

    @Test
    fun `insertarUsuario llama al metodo correcto del DAO`() = runTest {
        // GIVEN: Un usuario de prueba con TUS campos reales
        val nuevoUsuario = Usuario(
            id = 1,
            nombreCompleto = "Juan Perez",
            fechaNacimiento = "01/01/1990",
            correo = "juan@test.com",
            contrasena = "123456",
            region = "Metropolitana",
            comuna = "Santiago",
            fotoUri = null,
            telefono = "912345678",
            codigoDescuento = null
        )

        // Simulamos que el DAO no hace nada al insertar (retorna Unit)
        coEvery { usuarioDao.insertarUsuario(any()) } returns Unit

        // WHEN: Llamamos al repositorio
        repository.insertarUsuario(nuevoUsuario)

        // THEN: Verificamos que el repositorio le pasó el usuario al DAO
        coVerify(exactly = 1) { usuarioDao.insertarUsuario(nuevoUsuario) }
    }

    @Test
    fun `buscarUsuario devuelve el usuario cuando las credenciales son correctas`() = runTest {
        // GIVEN: Datos de prueba
        val correo = "juan@test.com"
        val pass = "123456"

        val usuarioEsperado = Usuario(
            id = 1,
            nombreCompleto = "Juan Perez", // Usando nombreCompleto
            fechaNacimiento = "01/01/1990",
            correo = correo,
            contrasena = pass,
            region = "Metropolitana",
            comuna = "Santiago",
            fotoUri = null,
            telefono = null,
            codigoDescuento = null
        )

        // Simulamos que el DAO encuentra al usuario
        // Nota: Verifica que tu DAO tenga un método con esta firma. Si se llama 'login', cámbialo aquí.
        coEvery { usuarioDao.findUserByEmailAndPassword(correo, pass) } returns usuarioEsperado

        // WHEN: Buscamos en el repositorio
        val resultado = repository.buscarUsuario(correo, pass)

        // THEN: El resultado debe ser el usuario esperado
        assertEquals(usuarioEsperado, resultado)
        assertEquals("Juan Perez", resultado?.nombreCompleto)
    }

    @Test
    fun `buscarUsuario devuelve null cuando las credenciales son incorrectas`() = runTest {
        // GIVEN: Credenciales que no existen
        val correo = "falso@test.com"
        val pass = "erronea"

        // Simulamos que el DAO devuelve null (no encontró nada)
        coEvery { usuarioDao.findUserByEmailAndPassword(correo, pass) } returns null

        // WHEN: Buscamos en el repositorio
        val resultado = repository.buscarUsuario(correo, pass)

        // THEN: El resultado debe ser null
        assertNull(resultado)
    }
}
