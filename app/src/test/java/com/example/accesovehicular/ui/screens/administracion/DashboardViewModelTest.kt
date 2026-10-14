package com.example.accesovehicular.ui.screens.administracion

import com.example.accesovehicular.domain.model.AccesoReporte
import com.example.accesovehicular.domain.model.ConteoPersonas
import com.example.accesovehicular.domain.model.PeriodoReporte
import com.example.accesovehicular.domain.model.Persona
import com.example.accesovehicular.domain.model.RegistroBitacora
import com.example.accesovehicular.domain.model.TipoAccionBitacora
import com.example.accesovehicular.domain.model.TipoUsuario
import com.example.accesovehicular.domain.repository.BitacoraRepository
import com.example.accesovehicular.domain.repository.PersonaRepository
import com.example.accesovehicular.domain.repository.ReporteAccesosRepository
import com.example.accesovehicular.ui.screens.usuarios.GestionUsuariosUiState
import com.example.accesovehicular.ui.screens.usuarios.GestionUsuariosViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    /** Repositorio falso, sin delay ni fallas al azar. Se comparte entre ViewModels como el mock real. */
    private class PersonasFalsas : PersonaRepository {
        val personas = mutableListOf(
            Persona(id = "1", nombre = "Ana López", tipo = TipoUsuario.RESIDENTE, activo = true),
            Persona(id = "2", nombre = "Carlos Méndez", tipo = TipoUsuario.RESIDENTE, activo = true),
            Persona(id = "4", nombre = "Jorge Ramírez", tipo = TipoUsuario.VISITA, activo = false)
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

    private class BitacoraFalsa : BitacoraRepository {
        val registros = mutableListOf<RegistroBitacora>()

        override suspend fun obtenerRegistros(tipo: TipoAccionBitacora?) =
            Result.success(registros.filter { tipo == null || it.tipo == tipo })

        override suspend fun registrar(tipo: TipoAccionBitacora, descripcion: String, permitido: Boolean?) =
            Result.success(
                RegistroBitacora(
                    id = "b${registros.size + 1}",
                    fechaHora = registros.size + 1L,
                    autor = "Administrador",
                    tipo = tipo,
                    descripcion = descripcion,
                    permitido = permitido
                ).also { registros.add(it) }
            )
    }

    private class ReportesFalsos(private val falla: Boolean = false) : ReporteAccesosRepository {
        override suspend fun obtenerAccesos(periodo: PeriodoReporte, tipoPersona: TipoUsuario?) =
            if (falla) Result.failure(Exception("Sin conexión")) else Result.success(emptyList<AccesoReporte>())
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
    fun elConteoDeInactivasSubeAlDarDeBajaAAlguien() {
        val personas = PersonasFalsas()
        val bitacora = BitacoraFalsa()
        val dashboard = DashboardViewModel(ReportesFalsos(), personas, bitacora)
        val gestion = GestionUsuariosViewModel(repository = personas, bitacoraRepository = bitacora)

        dashboard.cargar()
        assertEquals(SeccionUiState.Exito(ConteoPersonas(activas = 2, inactivas = 1)), dashboard.uiState.value.personas)

        val ana = (gestion.uiState.value as GestionUsuariosUiState.Exito).personas.first { it.id == "1" }
        gestion.onDarDeBajaClick(ana)
        gestion.onConfirmarBaja()

        // Lo que hace el panel al regresar de Gestión de usuarios.
        dashboard.cargar()
        assertEquals(SeccionUiState.Exito(ConteoPersonas(activas = 1, inactivas = 2)), dashboard.uiState.value.personas)

        // La baja también aparece como la acción más reciente.
        val acciones = (dashboard.uiState.value.ultimasAcciones as SeccionUiState.Exito).datos
        assertEquals("Baja de Ana López (residente)", acciones.first().descripcion)
    }

    @Test
    fun siFallaUnBloqueLosDemasSeMuestran() {
        val dashboard = DashboardViewModel(ReportesFalsos(falla = true), PersonasFalsas(), BitacoraFalsa())

        dashboard.cargar()

        val estado = dashboard.uiState.value
        assertEquals(SeccionUiState.Error("Sin conexión"), estado.accesosHoy)
        assertTrue(estado.personas is SeccionUiState.Exito)
        assertTrue(estado.ultimasAcciones is SeccionUiState.Exito)
    }
}
