package com.example.accesovehicular.ui.screens.caseta

import com.example.accesovehicular.domain.model.ResultadoValidacionQr

sealed interface EscaneoCasetaUiState {
    data object Escaneando : EscaneoCasetaUiState
    data object Validando : EscaneoCasetaUiState
    data class Resultado(val resultado: ResultadoValidacionQr) : EscaneoCasetaUiState
    data class Error(val mensaje: String) : EscaneoCasetaUiState
}
