package com.example.tiendahuertohogar.ui.screens


import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.example.tiendahuertohogar.ui.login.LoginScreen
import com.example.tiendahuertohogar.ui.login.LoginViewModel
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    // Regla necesaria para pruebas de Compose
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun verificarElementosVisiblesEnLogin() {
        // GIVEN: Cargamos la pantalla de Login
        composeTestRule.setContent {
            val navController = rememberNavController()
            // Instanciamos un ViewModel simple (asegúrate que LoginViewModel tenga constructor vacío o mockealo)
            val viewModel = LoginViewModel()

            LoginScreen(
                navController = navController,
                vm = viewModel
            )
        }

        // THEN: Verificamos que los textos clave existen en pantalla
        // Ajusta estos textos EXACTAMENTE como aparecen en tu app
        composeTestRule.onNodeWithText("Iniciar Sesión").assertIsDisplayed()
        composeTestRule.onNodeWithText("Usuario").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contraseña").assertIsDisplayed()
    }

    @Test
    fun verificarIngresoDeTextoEnLogin() {
        // GIVEN: Pantalla cargada
        composeTestRule.setContent {
            val navController = rememberNavController()
            LoginScreen(navController = navController, vm = LoginViewModel())
        }

        // WHEN: Escribimos en los campos
        composeTestRule.onNodeWithText("Usuario").performTextInput("cliente1")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("123456")

        // THEN: Verificamos que el texto se escribió correctamente (el nodo ahora contiene ese texto)
        composeTestRule.onNodeWithText("cliente1").assertIsDisplayed()
    }
}

