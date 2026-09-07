package com.example.accesovehicular.ui.screens.vehiculos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accesovehicular.data.repository.VehiculoRepositoryImpl
import com.example.accesovehicular.domain.usecase.ObtenerVehiculosUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VehiculosViewModel(
    private val obtenerVehiculos: ObtenerVehiculosUseCase = ObtenerVehiculosUseCase(VehiculoRepositoryImpl())
) : ViewModel() {

    private val _uiState = MutableStateFlow<VehiculosUiState>(VehiculosUiState.Cargando)
    val uiState: StateFlow<VehiculosUiState> = _uiState.asStateFlow()

    init {
        cargarVehiculos()
    }

    fun cargarVehiculos() {
        viewModelScope.launch {
            _uiState.value = VehiculosUiState.Cargando
            obtenerVehiculos()
                .onSuccess { vehiculos -> _uiState.value = VehiculosUiState.Exito(vehiculos) }
                .onFailure { error -> _uiState.value = VehiculosUiState.Error(error.message ?: "Error desconocido") }
        }
    }
}
