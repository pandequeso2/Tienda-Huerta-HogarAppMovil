package com.example.tiendahuertohogar.viewModel

import android.app.Application
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistroViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UsuarioRepository

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()

    val regiones: List<String> = regionesDeChile
    val comunas: StateFlow<List<String>> = MutableStateFlow(emptyList())

    init {
        val usuarioDao = AppDataBase.getDatabase(application).usuarioDao()
        repository = UsuarioRepository(usuarioDao)
    }

    // --- Eventos de la UI ---
    fun onNombreChange(nuevoNombre: String) {
        _uiState.update { it.copy(nombreCompleto = nuevoNombre) }
        validarNombre()
    }

    fun onFechaNacimientoChange(nuevaFecha: String) {
        _uiState.update { it.copy(fechaNacimiento = nuevaFecha) }
    }

    fun onCorreoChange(nuevoCorreo: String) {
        _uiState.update { it.copy(correo = nuevoCorreo) }
        validarCorreo()
    }

    fun onContrasenaChange(nuevaContrasena: String) {
        _uiState.update { it.copy(contrasena = nuevaContrasena) }
        validarContrasena()
        validarConfirmarContrasena()
    }

    fun onConfirmarContrasenaChange(nuevaConfirmacion: String) {
        _uiState.update { it.copy(confirmarContrasena = nuevaConfirmacion) }
        validarConfirmarContrasena()
    }

    fun onTelefonoChange(nuevoTelefono: String) {
        _uiState.update { it.copy(telefono = nuevoTelefono) }
    }

    fun onRegionSelected(nuevaRegion: String) {
        _uiState.update { it.copy(region = nuevaRegion, comuna = "") }
        (comunas as MutableStateFlow).value = obtenerComunas(nuevaRegion)
    }

    fun onComunaSelected(nuevaComuna: String) {
        _uiState.update { it.copy(comuna = nuevaComuna) }
    }

    fun onCodigoDescuentoChange(nuevoCodigo: String) {
        _uiState.update { it.copy(codigoDescuento = nuevoCodigo) }
    }

    // --- Lógica de Validación ---
    private fun validarNombre() {
        val nombre = _uiState.value.nombreCompleto
        _uiState.update {
            it.copy(errorNombre = if (nombre.length < 3) "El nombre debe tener al menos 3 caracteres." else null)
        }
    }

    private fun validarCorreo() {
        val correo = _uiState.value.correo
        val allowedDomains = listOf("@gmail.com", "@duocuc.cl", "@profesorduocuc.cl")
        val error = when {
            correo.isBlank() -> "El correo no puede estar vacío."
            !Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> "El formato del correo no es válido."
            allowedDomains.none { correo.endsWith(it) } -> "Dominio no permitido. Use @gmail.com, @duocuc.cl o @profesorduocuc.cl"
            else -> null
        }
        _uiState.update { it.copy(errorCorreo = error) }
    }

    private fun validarContrasena() {
        val contrasena = _uiState.value.contrasena
        val error = when {
            contrasena.length < 8 -> "La contraseña debe tener al menos 8 caracteres."
            !contrasena.any { it.isDigit() } -> "La contraseña debe contener al menos un número."
            !contrasena.any { it.isUpperCase() } -> "La contraseña debe contener al menos una mayúscula."
            else -> null
        }
        _uiState.update { it.copy(errorContrasena = error) }
    }

    private fun validarConfirmarContrasena() {
        val contrasena = _uiState.value.contrasena
        val confirmar = _uiState.value.confirmarContrasena
        _uiState.update {
            it.copy(errorConfirmarContrasena = if (contrasena != confirmar) "Las contraseñas no coinciden." else null)
        }
    }

    private fun validarFormulario(): Boolean {
        validarNombre()
        validarCorreo()
        validarContrasena()
        validarConfirmarContrasena()

        val currentState = _uiState.value
        return currentState.errorNombre == null &&
                currentState.errorCorreo == null &&
                currentState.errorContrasena == null &&
                currentState.errorConfirmarContrasena == null &&
                currentState.nombreCompleto.isNotBlank() &&
                currentState.correo.isNotBlank() &&
                currentState.contrasena.isNotBlank() &&
                currentState.region.isNotBlank() &&
                currentState.comuna.isNotBlank()
    }

    fun registrarUsuario() {
        if (!validarFormulario()) {
            Toast.makeText(getApplication(), "Por favor, corrige los errores.", Toast.LENGTH_SHORT).show()
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        val estadoActual = _uiState.value
        val nuevoUsuario = Usuario(
            nombreCompleto = estadoActual.nombreCompleto,
            fechaNacimiento = estadoActual.fechaNacimiento,
            correo = estadoActual.correo,
            contrasena = estadoActual.contrasena,
            region = estadoActual.region,
            comuna = estadoActual.comuna,
            telefono = estadoActual.telefono.ifBlank { null },
            codigoDescuento = estadoActual.codigoDescuento.ifBlank { null }
        )

        viewModelScope.launch(Dispatchers.IO) {
            repository.insertarUsuario(nuevoUsuario)
            launch(Dispatchers.Main) {
                _uiState.update { it.copy(isLoading = false, registroExitoso = true) }
                Toast.makeText(getApplication(), "¡Usuario registrado con éxito!", Toast.LENGTH_LONG).show()
            }
        }
    }
}