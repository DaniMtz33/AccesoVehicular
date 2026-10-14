package com.example.accesovehicular.data.repository

import com.example.accesovehicular.data.qr.ContenidoQr
import com.example.accesovehicular.domain.model.Persona
import com.example.accesovehicular.domain.model.ResultadoValidacionQr
import com.example.accesovehicular.domain.model.TipoUsuario
import com.example.accesovehicular.domain.repository.PersonaRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidacionQrRepositoryImplTest {

    private val activa = Persona(id = "1", nombre = "Ana López", tipo = TipoUsuario.RESIDENTE, activo = true)
    private val inactiva = Persona(id = "4", nombre = "Jorge Ramírez", tipo = TipoUsuario.VISITA, activo = false)

    private val repository = ValidacionQrRepositoryImpl(object : PersonaRepository {
        override suspend fun obtenerPersonas() = Result.success(listOf(activa, inactiva))
        override suspend fun registrarPersona(nombre: String, tipo: TipoUsuario) = error("no se usa")
        override suspend fun darDeBaja(id: String) = error("no se usa")
        override suspend fun buscarPorId(id: String) = Result.success(listOf(activa, inactiva).firstOrNull { it.id == id })
    })

    private fun validar(contenido: String) = runBlocking { repository.validar(contenido).getOrThrow() }

    @Test
    fun personaActiva_permitido() {
        val resultado = validar(ContenidoQr(idUsuario = "1", placa = "ABC-123").serializar())

        assertTrue(resultado is ResultadoValidacionQr.Permitido)
        resultado as ResultadoValidacionQr.Permitido
        assertEquals(activa, resultado.persona)
        assertEquals("ABC-123", resultado.placa)
    }

    @Test
    fun personaInactiva_denegado() {
        val resultado = validar(ContenidoQr(idUsuario = "4", placa = "ABC-123").serializar())

        assertEquals(ResultadoValidacionQr.Denegado("Usuario dado de baja", "ABC-123", inactiva), resultado)
    }

    @Test
    fun idInexistente_denegado() {
        val resultado = validar(ContenidoQr(idUsuario = "999", placa = "ABC-123").serializar())

        assertEquals(ResultadoValidacionQr.Denegado("Usuario no registrado", "ABC-123", null), resultado)
    }

    @Test
    fun contenidosSinFormato_noValido() {
        listOf(
            "hola",
            "",
            """{"v":1,"placa":"ABC-123"}""",
            """{"v":2,"idUsuario":"1","placa":"ABC-123"}""",
            """{"v":1,"idUsuario":"","placa":"ABC-123"}""",
            "[1,2,3]"
        ).forEach { contenido ->
            assertEquals("Contenido: $contenido", ResultadoValidacionQr.NoValido, validar(contenido))
        }
    }
}
