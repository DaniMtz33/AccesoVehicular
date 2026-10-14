package com.example.accesovehicular.ui.screens.miqr

import androidx.compose.ui.graphics.ImageBitmap
import com.example.accesovehicular.domain.model.CredencialQr

sealed interface MiQrUiState {
    data object Cargando : MiQrUiState
    data class Exito(val credencial: CredencialQr, val imagenQr: ImageBitmap) : MiQrUiState
    data class Error(val mensaje: String) : MiQrUiState
}
