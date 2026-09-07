package com.example.accesovehicular.ui.screens.login

import com.example.accesovehicular.domain.model.Sesion

sealed interface LoginUiState {
    data object Inactivo : LoginUiState
    data object Cargando : LoginUiState
    data class Exito(val sesion: Sesion) : LoginUiState
    data class Error(val mensaje: String) : LoginUiState
}
