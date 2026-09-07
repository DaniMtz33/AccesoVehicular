package com.example.accesovehicular.ui.screens.scanner

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScannerViewModel : ViewModel() {

    private val _codigoDetectado = MutableStateFlow<String?>(null)
    val codigoDetectado: StateFlow<String?> = _codigoDetectado.asStateFlow()

    fun onCodigoDetectado(valor: String) {
        _codigoDetectado.value = valor
    }
}
