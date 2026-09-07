package com.example.accesovehicular.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accesovehicular.data.repository.AuthRepositoryImpl
import com.example.accesovehicular.domain.usecase.CerrarSesionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val cerrarSesion: CerrarSesionUseCase = CerrarSesionUseCase(AuthRepositoryImpl())
) : ViewModel() {

    private val _sesionCerrada = MutableStateFlow(false)
    val sesionCerrada: StateFlow<Boolean> = _sesionCerrada.asStateFlow()

    fun onCerrarSesionClick() {
        viewModelScope.launch {
            cerrarSesion()
            _sesionCerrada.value = true
        }
    }
}
