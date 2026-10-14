package com.example.accesovehicular.ui.screens.administracion

import com.example.accesovehicular.domain.model.RegistroBitacora

sealed interface BitacoraUiState {
    data object Cargando : BitacoraUiState
    data class Exito(val registros: List<RegistroBitacora>) : BitacoraUiState
    data class Error(val mensaje: String) : BitacoraUiState
}
