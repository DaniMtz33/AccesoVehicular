package com.example.accesovehicular.ui.screens.administracion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accesovehicular.data.repository.BitacoraRepositoryImpl
import com.example.accesovehicular.data.repository.PersonaRepositoryImpl
import com.example.accesovehicular.data.repository.ReporteAccesosRepositoryImpl
import com.example.accesovehicular.domain.model.PeriodoReporte
import com.example.accesovehicular.domain.repository.BitacoraRepository
import com.example.accesovehicular.domain.repository.PersonaRepository
import com.example.accesovehicular.domain.repository.ReporteAccesosRepository
import com.example.accesovehicular.domain.usecase.ObtenerConteoPersonasUseCase
import com.example.accesovehicular.domain.usecase.ObtenerReporteAccesosUseCase
import com.example.accesovehicular.domain.usecase.ObtenerUltimasAccionesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    reporteRepository: ReporteAccesosRepository = ReporteAccesosRepositoryImpl(),
    personaRepository: PersonaRepository = PersonaRepositoryImpl(),
    bitacoraRepository: BitacoraRepository = BitacoraRepositoryImpl(),
    private val obtenerReporte: ObtenerReporteAccesosUseCase = ObtenerReporteAccesosUseCase(reporteRepository),
    private val obtenerConteoPersonas: ObtenerConteoPersonasUseCase = ObtenerConteoPersonasUseCase(personaRepository),
    private val obtenerUltimasAcciones: ObtenerUltimasAccionesUseCase = ObtenerUltimasAccionesUseCase(bitacoraRepository)
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private var cargaAccesos: Job? = null
    private var cargaPersonas: Job? = null
    private var cargaAcciones: Job? = null

    // Sin init: la pantalla llama a cargar() cada vez que vuelve al frente, también la primera.
    fun cargar() {
        cargarAccesosHoy()
        cargarPersonas()
        cargarUltimasAcciones()
    }

    fun cargarAccesosHoy() {
        cargaAccesos?.cancel()
        cargaAccesos = viewModelScope.launch {
            _uiState.update { it.copy(accesosHoy = it.accesosHoy.recargando()) }
            val resultado = obtenerReporte(PeriodoReporte.HOY, tipoPersona = null).map { it.resumen }
            _uiState.update { it.copy(accesosHoy = resultado.aSeccion()) }
        }
    }

    fun cargarPersonas() {
        cargaPersonas?.cancel()
        cargaPersonas = viewModelScope.launch {
            _uiState.update { it.copy(personas = it.personas.recargando()) }
            val resultado = obtenerConteoPersonas()
            _uiState.update { it.copy(personas = resultado.aSeccion()) }
        }
    }

    fun cargarUltimasAcciones() {
        cargaAcciones?.cancel()
        cargaAcciones = viewModelScope.launch {
            _uiState.update { it.copy(ultimasAcciones = it.ultimasAcciones.recargando()) }
            val resultado = obtenerUltimasAcciones()
            _uiState.update { it.copy(ultimasAcciones = resultado.aSeccion()) }
        }
    }

    /** Si ya había datos, se siguen mostrando mientras se refresca para que no parpadee. */
    private fun <T> SeccionUiState<T>.recargando(): SeccionUiState<T> =
        if (this is SeccionUiState.Exito) this else SeccionUiState.Cargando

    private fun <T> Result<T>.aSeccion(): SeccionUiState<T> = fold(
        onSuccess = { SeccionUiState.Exito(it) },
        onFailure = { SeccionUiState.Error(it.message ?: "Error desconocido") }
    )
}
