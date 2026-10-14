package com.example.accesovehicular.ui.screens.miqr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accesovehicular.data.repository.CredencialQrRepositoryImpl
import com.example.accesovehicular.domain.usecase.ObtenerCredencialQrUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MiQrViewModel(
    private val obtenerCredencial: ObtenerCredencialQrUseCase = ObtenerCredencialQrUseCase(CredencialQrRepositoryImpl())
) : ViewModel() {

    private val _uiState = MutableStateFlow<MiQrUiState>(MiQrUiState.Cargando)
    val uiState: StateFlow<MiQrUiState> = _uiState.asStateFlow()

    init {
        cargarQr()
    }

    fun cargarQr() {
        viewModelScope.launch {
            _uiState.value = MiQrUiState.Cargando
            obtenerCredencial()
                .mapCatching { credencial ->
                    val imagen = withContext(Dispatchers.Default) { generarImagenQr(credencial.contenido) }
                    MiQrUiState.Exito(credencial, imagen)
                }
                .onSuccess { exito -> _uiState.value = exito }
                .onFailure { error -> _uiState.value = MiQrUiState.Error(error.message ?: "Error desconocido") }
        }
    }
}
