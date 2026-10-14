package com.example.accesovehicular.ui.screens.administracion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accesovehicular.data.repository.BitacoraRepositoryImpl
import com.example.accesovehicular.domain.model.TipoAccionBitacora
import com.example.accesovehicular.domain.usecase.ObtenerBitacoraUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BitacoraViewModel(
    private val obtenerBitacora: ObtenerBitacoraUseCase = ObtenerBitacoraUseCase(BitacoraRepositoryImpl())
) : ViewModel() {

    private val _uiState = MutableStateFlow<BitacoraUiState>(BitacoraUiState.Cargando)
    val uiState: StateFlow<BitacoraUiState> = _uiState.asStateFlow()

    /** null = Todas. */
    var tipo by mutableStateOf<TipoAccionBitacora?>(null)
        private set

    private var cargaActual: Job? = null

    init {
        cargarRegistros()
    }

    fun onTipoChange(valor: TipoAccionBitacora?) {
        if (valor == tipo) return
        tipo = valor
        cargarRegistros()
    }

    fun cargarRegistros() {
        // Si se cambia de filtro a media carga, solo cuenta la última petición.
        cargaActual?.cancel()
        cargaActual = viewModelScope.launch {
            _uiState.value = BitacoraUiState.Cargando
            obtenerBitacora(tipo)
                .onSuccess { registros -> _uiState.value = BitacoraUiState.Exito(registros) }
                .onFailure { error -> _uiState.value = BitacoraUiState.Error(error.message ?: "Error desconocido") }
        }
    }
}
