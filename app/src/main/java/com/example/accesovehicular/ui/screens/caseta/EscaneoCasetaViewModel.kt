package com.example.accesovehicular.ui.screens.caseta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accesovehicular.data.repository.ValidacionQrRepositoryImpl
import com.example.accesovehicular.domain.usecase.ValidarQrAccesoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EscaneoCasetaViewModel(
    private val validarQr: ValidarQrAccesoUseCase = ValidarQrAccesoUseCase(ValidacionQrRepositoryImpl())
) : ViewModel() {

    private val _uiState = MutableStateFlow<EscaneoCasetaUiState>(EscaneoCasetaUiState.Escaneando)
    val uiState: StateFlow<EscaneoCasetaUiState> = _uiState.asStateFlow()

    private var ultimoContenido: String? = null
    private var reanudadoEn = 0L

    fun onCodigoDetectado(contenido: String) {
        // La cámara entrega el mismo código varias veces por segundo: solo cuenta la primera
        // lectura y se ignora todo mientras se valida o se muestra el resultado.
        if (_uiState.value !is EscaneoCasetaUiState.Escaneando) return
        // Tras "Escanear otro" el mismo QR suele seguir frente a la cámara; se le da un margen.
        if (contenido == ultimoContenido &&
            System.currentTimeMillis() - reanudadoEn < MARGEN_MISMO_QR_MS
        ) return

        ultimoContenido = contenido
        validar(contenido)
    }

    fun onEscanearOtro() {
        reanudadoEn = System.currentTimeMillis()
        _uiState.value = EscaneoCasetaUiState.Escaneando
    }

    fun onReintentar() {
        ultimoContenido?.let(::validar)
    }

    private fun validar(contenido: String) {
        _uiState.value = EscaneoCasetaUiState.Validando
        viewModelScope.launch {
            validarQr(contenido)
                .onSuccess { resultado -> _uiState.value = EscaneoCasetaUiState.Resultado(resultado) }
                .onFailure { error ->
                    _uiState.value = EscaneoCasetaUiState.Error(error.message ?: "No se pudo validar el código")
                }
        }
    }

    private companion object {
        const val MARGEN_MISMO_QR_MS = 3000L
    }
}
