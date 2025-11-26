package com.example.tiendahuertohogar.ui.screens

import android.app.Application // <--- IMPORT ADDED
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider // <--- IMPORT ADDED
import com.example.tiendahuertohogar.ui.login.LoginScreen
import com.example.tiendahuertohogar.ui.login.LoginViewModel
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun verificarElementosVisiblesEnLogin() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            val viewModel = LoginViewModel(ApplicationProvider.getApplicationContext() as Application)

            LoginScreen(navController, viewModel)
        }

        composeTestRule.onNodeWithText("Iniciar sesión").assertIsDisplayed()
        composeTestRule.onNodeWithText("Correo").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contraseña").assertIsDisplayed()
    }

    @Test
    fun verificarIngresoDeTextoEnLogin() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            val viewModel = LoginViewModel(ApplicationProvider.getApplicationContext() as Application)

            LoginScreen(navController, viewModel)
        }

        composeTestRule.onNodeWithTag("inputCorreo").performTextInput("cliente1@correo.com")
        composeTestRule.onNodeWithTag("inputClave").performTextInput("123456")

        composeTestRule.onNodeWithText("cliente1@correo.com").assertIsDisplayed()
    }

}
