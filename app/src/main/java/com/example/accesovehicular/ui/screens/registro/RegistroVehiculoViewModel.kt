package com.example.accesovehicular.ui.screens.registro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accesovehicular.data.repository.RegistroVehiculoRepositoryImpl
import com.example.accesovehicular.domain.model.RegistroVehiculo
import com.example.accesovehicular.domain.model.TipoUsuario
import com.example.accesovehicular.domain.usecase.RegistrarVehiculoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegistroVehiculoViewModel(
    private val registrar: RegistrarVehiculoUseCase = RegistrarVehiculoUseCase(RegistroVehiculoRepositoryImpl())
) : ViewModel() {

    var placa by mutableStateOf("")
        private set

    var marca by mutableStateOf("")
        private set

    var modelo by mutableStateOf("")
        private set

    var color by mutableStateOf("")
        private set

    var tipoUsuario by mutableStateOf(TipoUsuario.RESIDENTE)
        private set

    private val _uiState = MutableStateFlow<RegistroVehiculoUiState>(RegistroVehiculoUiState.Inactivo)
    val uiState: StateFlow<RegistroVehiculoUiState> = _uiState.asStateFlow()

    val formularioValido: Boolean
        get() = placa.isNotBlank() && marca.isNotBlank() && modelo.isNotBlank() && color.isNotBlank()

    fun onPlacaChange(valor: String) {
        placa = valor
    }

    fun onMarcaChange(valor: String) {
        marca = valor
    }

    fun onModeloChange(valor: String) {
        modelo = valor
    }

    fun onColorChange(valor: String) {
        color = valor
    }

    fun onTipoUsuarioChange(valor: TipoUsuario) {
        tipoUsuario = valor
    }

    fun onRegistrarClick() {
        if (!formularioValido) return

        viewModelScope.launch {
            _uiState.value = RegistroVehiculoUiState.Cargando
            val registro = RegistroVehiculo(
                placa = placa.trim(),
                marca = marca.trim(),
                modelo = modelo.trim(),
                color = color.trim(),
                tipoUsuario = tipoUsuario
            )
            registrar(registro)
                .onSuccess { _uiState.value = RegistroVehiculoUiState.Exito }
                .onFailure { error -> _uiState.value = RegistroVehiculoUiState.Error(error.message ?: "Error desconocido") }
        }
    }

    fun onRegistrarOtroClick() {
        placa = ""
        marca = ""
        modelo = ""
        color = ""
        tipoUsuario = TipoUsuario.RESIDENTE
        _uiState.value = RegistroVehiculoUiState.Inactivo
    }
}
