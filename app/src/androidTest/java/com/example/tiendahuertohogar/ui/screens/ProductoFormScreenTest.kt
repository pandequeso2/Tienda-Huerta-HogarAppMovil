// ARCHIVO: ui.screens/ProductoFormScreenTest.kt (NUEVO)

package com.example.tiendahuertohogar.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController // Importación necesaria para simular
import com.example.tiendahuertohogar.data.repository.ProductoRepository // Importar el Repositorio
import com.example.tiendahuertohogar.view.ProductoFormScreen
import io.mockk.mockk // Importar MockK
import org.junit.Rule
import org.junit.Test
// No necesitamos TestCoroutineDispatcher si solo mockeamos el repositorio

class ProductoFormScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // 📢 MOCKEAR LAS DEPENDENCIAS
    // 'relaxed = true' asegura que los métodos suspend del Repositorio no fallen
    private val mockRepository = mockk<ProductoRepository>(relaxed = true)

    @Test
    fun verificarFormularioDeProductoVisible() {
        // GIVEN: Cargamos la pantalla del formulario
        composeTestRule.setContent {
            // 📢 Pasamos las dependencias mockeadas
            ProductoFormScreen(
                repository = mockRepository,
                navController = rememberNavController() // NavController simulado para el test
            )
        }

        // THEN: Verificamos que los campos principales estén ahí
        composeTestRule.onNodeWithText("Registrar Nuevo Producto").assertIsDisplayed()
        composeTestRule.onNodeWithText("Nombre del Producto").assertIsDisplayed()
        composeTestRule.onNodeWithText("Precio").assertIsDisplayed()
        composeTestRule.onNodeWithText("Código/SKU").assertIsDisplayed()
    }

    @Test
    fun verificarLlenadoDeFormulario() {
        // GIVEN: Cargamos la pantalla
        composeTestRule.setContent {
            // 📢 Pasamos las dependencias mockeadas
            ProductoFormScreen(
                repository = mockRepository,
                navController = rememberNavController()
            )
        }

        // WHEN: Simulamos que el usuario llena el formulario

        // Campos superiores
        composeTestRule.onNodeWithText("Nombre del Producto").performTextInput("Pala de Jardín")
        composeTestRule.onNodeWithText("Descripción").performTextInput("Herramienta resistente")
        composeTestRule.onNodeWithText("Precio").performTextInput("15990.50")

        // 📢 Campos inferiores: Aplicamos performScrollTo() para evitar el error de visibilidad
        composeTestRule.onNodeWithText("Categoría (ej: Tortas, Galletas)")
            .performScrollTo()
            .performTextInput("Herramientas")

        composeTestRule.onNodeWithText("Código/SKU")
            .performScrollTo()
            .performTextInput("PALA-001")

        // 📢 Campo Comentarios/Notas omitido por solicitud ❌


        // THEN: Verificamos que el botón esté visible y el texto ingresado.
        composeTestRule.onNodeWithText("Guardar Producto").assertIsDisplayed()
        composeTestRule.onNodeWithText("Pala de Jardín").assertIsDisplayed()
    }
}