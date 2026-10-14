package com.example.accesovehicular.ui.screens.reportes

import com.example.accesovehicular.domain.model.ReporteAccesos

sealed interface ReportesAccesosUiState {
    data object Cargando : ReportesAccesosUiState
    data class Exito(val reporte: ReporteAccesos) : ReportesAccesosUiState
    data class Error(val mensaje: String) : ReportesAccesosUiState
}
