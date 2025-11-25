package com.example.tiendahuertohogar.ui.screens



import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import com.example.tiendahuertohogar.view.ProductoFormScreen
import com.example.tiendahuertohogar.viewModel.ProductoViewModel
import org.junit.Rule
import org.junit.Test

class ProductoFormScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun verificarFormularioDeProductoVisible() {
        // GIVEN: Cargamos la pantalla del formulario
        composeTestRule.setContent {
            // Usamos el constructor vacío de ProductoViewModel para la prueba visual
            val viewModel = ProductoViewModel()
            ProductoFormScreen(viewModel = viewModel)
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
            val viewModel = ProductoViewModel()
            ProductoFormScreen(viewModel = viewModel)
        }

        // WHEN: Simulamos que el usuario llena el formulario
        composeTestRule.onNodeWithText("Nombre del Producto").performTextInput("Pala de Jardín")
        composeTestRule.onNodeWithText("Descripción").performTextInput("Herramienta resistente")

        // Aquí probamos el campo de Precio (Double)
        composeTestRule.onNodeWithText("Precio").performTextInput("15990.50")

        composeTestRule.onNodeWithText("Categoría (ej: Tortas, Galletas)").performTextInput("Herramientas")
        composeTestRule.onNodeWithText("Código/SKU").performTextInput("PALA-001")

        // THEN: Buscamos el botón de guardar.
        // A veces el botón queda abajo si el teclado sale, usamos 'performScrollTo' por seguridad
        composeTestRule.onNodeWithText("Guardar Producto").performScrollTo().assertIsDisplayed()

        // Opcional: Verificar que lo que escribimos está visible
        composeTestRule.onNodeWithText("Pala de Jardín").assertIsDisplayed()
    }
}
