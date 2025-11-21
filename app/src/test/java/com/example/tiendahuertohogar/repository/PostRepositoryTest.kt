//--------------------------------------------------------------------------------
// Este código implementa una prueba unitaria para verificar que el PostRepository retorna correctamente una lista simulada de posts.
// PROPÓSITO PRINCIPAL
// Validar que el método getPosts() del repositorio devuelve los datos esperados usando objetos mock para aislar la prueba de dependencias // externas.
//
// Mock del Repository: Crea un PostRepository falso que simula el comportamiento real
// Datos de Prueba: Lista predefinida con 2 posts ficticios
// Verificación: Confirma que el resultado coincide exactamente con los datos esperados
//--------------------------------------------------------------------------------


package com.example.tiendahuertohogar.repository


// Importa la clase Post del modelo de datos
import com.example.tiendahuertohogar.data.model.Post
import com.example.tiendahuertohogar.data.repository.PostRepository
// Importa la interfaz ApiService para llamadas a la API
// Importa el repositorio que se va a testear
// Importa el estilo StringSpec de Kotest para escribir tests
import io.kotest.core.spec.style.StringSpec
//import io.kotest.core.spec.style.scopes.StringSpecRootScope.invoke
// Importa el matcher para verificar que una colección contiene exactamente los elementos esperados
import io.kotest.matchers.collections.shouldContainExactly
// Importa coEvery de MockK para definir comportamiento de funciones suspendidas
import io.mockk.coEvery
// Importa mockk para crear objetos mock
import io.mockk.mockk
// Importa runTest para ejecutar tests con corrutinas
import kotlinx.coroutines.test.runTest

// Clase de test para PostRepository usando Kotest con estilo StringSpec
class PostRepositoryTest : StringSpec({
    // Define un test case con descripción en lenguaje natural
    "getPosts() debe retornar una lista de posts simulada: Benjamin Araya" {
        // Crea una lista falsa de posts para usar en el test
        val fakePosts = listOf(
            // Crea el primer post de prueba con datos simulados
            Post(userId = 1, id = 1, title = "Título 1", body = "Cuerpo 1"),
            // Crea el segundo post de prueba con datos simulados
            Post(userId = 2, id = 2, title = "Título 2", body = "Cuerpo 2")
        )

        // Mock del PostRepository completo
        // Crea un objeto mock de PostRepository usando MockK
        val repo = mockk<PostRepository>()
        // Define el comportamiento del mock: cuando se llame a getPosts(), retornar fakePosts
        coEvery { repo.getPosts() } returns fakePosts

        // Ejecuta el test en un contexto de corrutinas
        runTest {
            // Llama al método getPosts() del repositorio mockeado
            val result = repo.getPosts()
            // Verifica que el resultado contenga exactamente los mismos posts que fakePosts
            result shouldContainExactly fakePosts
        }
    }
})