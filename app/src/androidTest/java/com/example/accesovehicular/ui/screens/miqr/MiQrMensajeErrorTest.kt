package com.example.accesovehicular.ui.screens.miqr

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.accesovehicular.domain.model.CredencialQr
import com.example.accesovehicular.domain.repository.CredencialQrRepository
import com.example.accesovehicular.domain.usecase.ObtenerCredencialQrUseCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MiQrMensajeErrorTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun viewModelQueFalla(error: Throwable) = MiQrViewModel(
        ObtenerCredencialQrUseCase(object : CredencialQrRepository {
            override suspend fun obtenerCredencial(): Result<CredencialQr> = Result.failure(error)
        })
    )

    @Test
    fun muestraSoloElMensajeDelError() {
        composeRule.setContent { MiQrScreen(viewModel = viewModelQueFalla(Exception("No se pudo obtener tu código QR"))) }

        composeRule.onNodeWithText("No se pudo obtener tu código QR").assertExists()
        composeRule.onNodeWithText("No se pudo generar tu QR:", substring = true).assertDoesNotExist()
        composeRule.onNodeWithText("Reintentar").assertExists()
    }

    @Test
    fun errorSinMensajeUsaElTextoPorDefecto() {
        composeRule.setContent { MiQrScreen(viewModel = viewModelQueFalla(Exception())) }

        composeRule.onNodeWithText("No se pudo generar tu código QR").assertExists()
        composeRule.onNodeWithText("Error desconocido").assertDoesNotExist()
    }
}
