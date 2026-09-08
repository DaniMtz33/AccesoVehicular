package com.example.accesovehicular.ui.screens.registro

sealed interface RegistroVehiculoUiState {
    data object Inactivo : RegistroVehiculoUiState
    data object Cargando : RegistroVehiculoUiState
    data object Exito : RegistroVehiculoUiState
    data class Error(val mensaje: String) : RegistroVehiculoUiState
}
