package com.example.accesovehicular.ui.screens.usuarios

import com.example.accesovehicular.domain.model.Persona
import com.example.accesovehicular.domain.model.RegistroBitacora
import com.example.accesovehicular.domain.model.TipoAccionBitacora
import com.example.accesovehicular.domain.model.TipoUsuario
import com.example.accesovehicular.domain.repository.BitacoraRepository
import com.example.accesovehicular.domain.repository.PersonaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GestionUsuariosViewModelTest {

    /** Repositorio falso, sin delay ni fallas al azar. */
    private class PersonasFalsas : PersonaRepository {
        val personas = mutableListOf(
            Persona(id = "1", nombre = "Ana López", tipo = TipoUsuario.RESIDENTE, activo = true),
            Persona(id = "5", nombre = "Gas del Norte", tipo = TipoUsuario.PROVEEDOR, activo = true)
        )

        override suspend fun obtenerPersonas() = Result.success(personas.toList())
        override suspend fun registrarPersona(nombre: String, tipo: TipoUsuario) = error("no se usa")
        override suspend fun buscarPorId(id: String) = Result.success(personas.firstOrNull { it.id == id })

        override suspend fun darDeBaja(id: String): Result<Persona> {
            val indice = personas.indexOfFirst { it.id == id }
            personas[indice] = personas[indice].copy(activo = false)
            return Result.success(personas[indice])
        }
    }

    /** Bitácora falsa que solo guarda lo que se registra. */
    private class BitacoraFalsa : BitacoraRepository {
        val registros = mutableListOf<RegistroBitacora>()

        override suspend fun obtenerRegistros(tipo: TipoAccionBitacora?) =
            Result.success(registros.filter { tipo == null || it.tipo == tipo })

        override suspend fun registrar(tipo: TipoAccionBitacora, descripcion: String, permitido: Boolean?) =
            Result.success(
                RegistroBitacora(
                    id = "b${registros.size + 1}",
                    fechaHora = 0L,
                    autor = "Administrador",
                    tipo = tipo,
                    descripcion = descripcion,
                    permitido = permitido
                ).also { registros.add(it) }
            )
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun darDeBajaAgregaUnRegistroALaBitacora() {
        val bitacora = BitacoraFalsa()
        val viewModel = GestionUsuariosViewModel(repository = PersonasFalsas(), bitacoraRepository = bitacora)
        val ana = (viewModel.uiState.value as GestionUsuariosUiState.Exito).personas.first { it.id == "1" }

        viewModel.onDarDeBajaClick(ana)
        viewModel.onConfirmarBaja()

        assertEquals(1, bitacora.registros.size)
        val registro = bitacora.registros.single()
        assertEquals(TipoAccionBitacora.BAJA, registro.tipo)
        assertEquals("Baja de Ana López (residente)", registro.descripcion)
        assertNull(registro.permitido)

        // La lista también refleja la baja.
        val actualizada = (viewModel.uiState.value as GestionUsuariosUiState.Exito).personas.first { it.id == "1" }
        assertEquals(false, actualizada.activo)
    }
}
