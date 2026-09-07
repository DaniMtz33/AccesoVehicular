package com.example.accesovehicular.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accesovehicular.data.repository.AuthRepositoryImpl
import com.example.accesovehicular.domain.usecase.ObtenerSesionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface DestinoInicial {
    data object Verificando : DestinoInicial
    data object IrALogin : DestinoInicial
    data object IrAHome : DestinoInicial
}

class SplashViewModel(
    private val obtenerSesion: ObtenerSesionUseCase = ObtenerSesionUseCase(AuthRepositoryImpl())
) : ViewModel() {

    private val _destino = MutableStateFlow<DestinoInicial>(DestinoInicial.Verificando)
    val destino: StateFlow<DestinoInicial> = _destino.asStateFlow()

    init {
        viewModelScope.launch {
            val sesion = obtenerSesion()
            _destino.value = if (sesion != null) DestinoInicial.IrAHome else DestinoInicial.IrALogin
        }
    }
}
