package com.example.accesovehicular.ui.screens.reportes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accesovehicular.data.repository.ReporteAccesosRepositoryImpl
import com.example.accesovehicular.domain.model.PeriodoReporte
import com.example.accesovehicular.domain.model.TipoUsuario
import com.example.accesovehicular.domain.usecase.ObtenerReporteAccesosUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReportesAccesosViewModel(
    private val obtenerReporte: ObtenerReporteAccesosUseCase = ObtenerReporteAccesosUseCase(ReporteAccesosRepositoryImpl())
) : ViewModel() {

    private val _uiState = MutableStateFlow<ReportesAccesosUiState>(ReportesAccesosUiState.Cargando)
    val uiState: StateFlow<ReportesAccesosUiState> = _uiState.asStateFlow()

    var periodo by mutableStateOf(PeriodoReporte.HOY)
        private set

    /** null = Todos. */
    var tipoPersona by mutableStateOf<TipoUsuario?>(null)
        private set

    private var cargaActual: Job? = null

    init {
        cargarReporte()
    }

    fun onPeriodoChange(valor: PeriodoReporte) {
        if (valor == periodo) return
        periodo = valor
        cargarReporte()
    }

    fun onTipoPersonaChange(valor: TipoUsuario?) {
        if (valor == tipoPersona) return
        tipoPersona = valor
        cargarReporte()
    }

    fun cargarReporte() {
        // Si se cambia de filtro a media carga, solo cuenta la última petición.
        cargaActual?.cancel()
        cargaActual = viewModelScope.launch {
            _uiState.value = ReportesAccesosUiState.Cargando
            obtenerReporte(periodo, tipoPersona)
                .onSuccess { reporte -> _uiState.value = ReportesAccesosUiState.Exito(reporte) }
                .onFailure { error -> _uiState.value = ReportesAccesosUiState.Error(error.message ?: "Error desconocido") }
        }
    }
}
