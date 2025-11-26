package com.example.tiendahuertohogar.repository

import com.example.tiendahuertohogar.data.dao.UsuarioDao
import com.example.tiendahuertohogar.data.model.Usuario
import com.example.tiendahuertohogar.data.repository.UsuarioRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

class UsuarioRepositoryTest : StringSpec({

    val usuarioDao = mockk<UsuarioDao>()
    val repository = UsuarioRepository(usuarioDao)

    "insertarUsuario() debe llamar al método correcto del DAO" {
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

        coEvery { usuarioDao.insertarUsuario(any()) } returns Unit

        runTest {
            repository.insertarUsuario(nuevoUsuario)

            coVerify(exactly = 1) { usuarioDao.insertarUsuario(nuevoUsuario) }
        }
    }

    "buscarUsuario() debe retornar el usuario cuando las credenciales son correctas" {
        val correo = "juan@test.com"
        val pass = "123456"

        val usuarioEsperado = Usuario(
            id = 1,
            nombreCompleto = "Juan Perez",
            fechaNacimiento = "01/01/1990",
            correo = correo,
            contrasena = pass,
            region = "Metropolitana",
            comuna = "Santiago",
            fotoUri = null,
            telefono = null,
            codigoDescuento = null
        )

        coEvery { usuarioDao.findUserByEmailAndPassword(correo, pass) } returns usuarioEsperado

        runTest {
            val resultado = repository.buscarUsuario(correo, pass)

            resultado shouldBe usuarioEsperado
            resultado?.nombreCompleto shouldBe "Juan Perez"
        }
    }

    "buscarUsuario() debe retornar null cuando las credenciales son incorrectas" {
        val correo = "falso@test.com"
        val pass = "erronea"

        coEvery { usuarioDao.findUserByEmailAndPassword(correo, pass) } returns null

        runTest {
            val resultado = repository.buscarUsuario(correo, pass)

            resultado shouldBe null
        }
    }
})