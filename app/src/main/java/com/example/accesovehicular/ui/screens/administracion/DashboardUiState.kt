package com.example.accesovehicular.ui.screens.administracion

import com.example.accesovehicular.domain.model.ConteoPersonas
import com.example.accesovehicular.domain.model.RegistroBitacora
import com.example.accesovehicular.domain.model.ResumenAccesos

/** Estado de un bloque del dashboard. Cada bloque carga y falla por su cuenta. */
sealed interface SeccionUiState<out T> {
    data object Cargando : SeccionUiState<Nothing>
    data class Exito<T>(val datos: T) : SeccionUiState<T>
    data class Error(val mensaje: String) : SeccionUiState<Nothing>
}

data class DashboardUiState(
    val accesosHoy: SeccionUiState<ResumenAccesos> = SeccionUiState.Cargando,
    val personas: SeccionUiState<ConteoPersonas> = SeccionUiState.Cargando,
    val ultimasAcciones: SeccionUiState<List<RegistroBitacora>> = SeccionUiState.Cargando
)
