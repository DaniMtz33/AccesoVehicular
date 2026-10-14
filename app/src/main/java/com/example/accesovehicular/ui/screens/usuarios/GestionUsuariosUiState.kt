package com.example.accesovehicular.ui.screens.usuarios

import com.example.accesovehicular.domain.model.Persona

sealed interface GestionUsuariosUiState {
    data object Cargando : GestionUsuariosUiState
    data class Exito(val personas: List<Persona>) : GestionUsuariosUiState
    data class Error(val mensaje: String) : GestionUsuariosUiState
}

sealed interface AltaPersonaUiState {
    data object Oculto : AltaPersonaUiState
    data object Editando : AltaPersonaUiState
    data object Guardando : AltaPersonaUiState
    data class Error(val mensaje: String) : AltaPersonaUiState
}
