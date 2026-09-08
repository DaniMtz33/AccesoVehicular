package com.example.accesovehicular.ui.screens.historial

import com.example.accesovehicular.domain.model.Acceso

sealed interface HistorialUiState {
    data object Cargando : HistorialUiState
    data class Exito(val accesos: List<Acceso>) : HistorialUiState
    data class Error(val mensaje: String) : HistorialUiState
}
