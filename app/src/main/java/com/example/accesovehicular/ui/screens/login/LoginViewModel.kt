package com.example.accesovehicular.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accesovehicular.data.repository.AuthRepositoryImpl
import com.example.accesovehicular.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val login: LoginUseCase = LoginUseCase(AuthRepositoryImpl())
) : ViewModel() {

    var usuario by mutableStateOf("")
        private set

    var contrasena by mutableStateOf("")
        private set

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Inactivo)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onUsuarioChange(valor: String) {
        usuario = valor
    }

    fun onContrasenaChange(valor: String) {
        contrasena = valor
    }

    fun onLoginClick() {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Cargando
            login(usuario, contrasena)
                .onSuccess { sesion -> _uiState.value = LoginUiState.Exito(sesion) }
                .onFailure { error -> _uiState.value = LoginUiState.Error(error.message ?: "Error desconocido") }
        }
    }
}
