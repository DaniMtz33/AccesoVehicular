package com.example.accesovehicular.ui.screens.historial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accesovehicular.data.repository.HistorialAccesosRepositoryImpl
import com.example.accesovehicular.domain.usecase.ObtenerHistorialAccesosUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistorialViewModel(
    private val obtenerHistorial: ObtenerHistorialAccesosUseCase = ObtenerHistorialAccesosUseCase(HistorialAccesosRepositoryImpl())
) : ViewModel() {

    private val _uiState = MutableStateFlow<HistorialUiState>(HistorialUiState.Cargando)
    val uiState: StateFlow<HistorialUiState> = _uiState.asStateFlow()

    init {
        cargarHistorial()
    }

    fun cargarHistorial() {
        viewModelScope.launch {
            _uiState.value = HistorialUiState.Cargando
            obtenerHistorial()
                .onSuccess { accesos -> _uiState.value = HistorialUiState.Exito(accesos) }
                .onFailure { error -> _uiState.value = HistorialUiState.Error(error.message ?: "Error desconocido") }
        }
    }
}
