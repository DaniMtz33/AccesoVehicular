package com.example.accesovehicular.ui.screens.vehiculos

import com.example.accesovehicular.domain.model.Vehiculo

sealed interface VehiculosUiState {
    data object Cargando : VehiculosUiState
    data class Exito(val vehiculos: List<Vehiculo>) : VehiculosUiState
    data class Error(val mensaje: String) : VehiculosUiState
}
