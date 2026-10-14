package com.example.accesovehicular.ui.screens.caseta

import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.accesovehicular.data.qr.ContenidoQr
import com.example.accesovehicular.data.repository.PersonaRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Simula lecturas de QR sin cámara: la pantalla usa los repositorios mock reales
 * (misma lista que Gestión de usuarios) y el escáner se reemplaza por uno falso.
 */
@RunWith(AndroidJUnit4::class)
class EscaneoCasetaSimulacionTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var leerQr: (String) -> Unit

    @Before
    fun mostrarPantalla() {
        val viewModel = EscaneoCasetaViewModel()
        composeRule.setContent {
            EscaneoCasetaScreen(
                viewModel = viewModel,
                escaner = { onCodigoDetectado ->
                    leerQr = onCodigoDetectado
                    Text("Cámara simulada")
                }
            )
        }
    }

    private fun qr(idUsuario: String, placa: String = "ABC-123") =
        ContenidoQr(idUsuario = idUsuario, placa = placa).serializar()

    private fun simularLectura(contenido: String) {
        composeRule.runOnIdle { leerQr(contenido) }
    }

    private fun esperarTexto(texto: String) {
        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText(texto).fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun personaActiva_accesoPermitido() {
        simularLectura(qr(idUsuario = "1", placa = "XYZ-987"))

        esperarTexto("ACCESO PERMITIDO")
        composeRule.onNodeWithText("Ana López").assertExists()
        composeRule.onNodeWithText("Residente").assertExists()
        composeRule.onNodeWithText("Placa: XYZ-987").assertExists()
        composeRule.onNodeWithText("Hora:", substring = true).assertExists()
    }

    @Test
    fun personaInactiva_accesoDenegado() {
        simularLectura(qr(idUsuario = "4"))

        esperarTexto("ACCESO DENEGADO")
        composeRule.onNodeWithText("Jorge Ramírez").assertExists()
        composeRule.onNodeWithText("Usuario dado de baja").assertExists()
    }

    @Test
    fun idInexistente_accesoDenegado() {
        simularLectura(qr(idUsuario = "999"))

        esperarTexto("ACCESO DENEGADO")
        composeRule.onNodeWithText("Usuario no registrado").assertExists()
    }

    @Test
    fun contenidoSinFormato_qrNoValido() {
        simularLectura("hola, no soy un QR de acceso")

        esperarTexto("QR NO VÁLIDO")
    }

    @Test
    fun darDeBajaEnGestionUsuarios_cambiaElResultado() {
        runBlocking { PersonaRepositoryImpl().darDeBaja("2") }

        simularLectura(qr(idUsuario = "2"))

        esperarTexto("ACCESO DENEGADO")
        composeRule.onNodeWithText("Carlos Méndez").assertExists()
    }

    @Test
    fun ignoraLecturasMientrasHayResultado_yEscanearOtroVuelveALaCamara() {
        simularLectura(qr(idUsuario = "1"))
        esperarTexto("ACCESO PERMITIDO")

        simularLectura("hola")
        simularLectura(qr(idUsuario = "4"))
        composeRule.waitForIdle()
        composeRule.onNodeWithText("ACCESO PERMITIDO").assertExists()
        composeRule.onNodeWithText("QR NO VÁLIDO").assertDoesNotExist()

        composeRule.onNodeWithText("Escanear otro").performClick()
        composeRule.onNodeWithText("Cámara simulada").assertExists()
    }
}
