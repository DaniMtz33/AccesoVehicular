package com.example.accesovehicular.ui.screens.reportes

import com.example.accesovehicular.domain.model.Acceso
import com.example.accesovehicular.domain.model.AccesoReporte
import com.example.accesovehicular.domain.model.PeriodoReporte
import com.example.accesovehicular.domain.model.ResumenAccesos
import com.example.accesovehicular.domain.model.TipoAcceso
import com.example.accesovehicular.domain.model.TipoAcceso.ENTRADA
import com.example.accesovehicular.domain.model.TipoAcceso.SALIDA
import com.example.accesovehicular.domain.model.TipoUsuario
import com.example.accesovehicular.domain.model.TipoUsuario.PROVEEDOR
import com.example.accesovehicular.domain.model.TipoUsuario.RESIDENTE
import com.example.accesovehicular.domain.model.TipoUsuario.VISITA
import com.example.accesovehicular.domain.repository.ReporteAccesosRepository
import com.example.accesovehicular.domain.usecase.ObtenerReporteAccesosUseCase
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
class ReportesAccesosViewModelTest {

    /** Repositorio falso, sin delay ni fallas al azar, que filtra igual que el real. */
    private class RepositorioFalso : ReporteAccesosRepository {

        private data class Registro(val reporte: AccesoReporte, val periodos: Set<PeriodoReporte>)

        private val hoy = PeriodoReporte.entries.toSet()
        private val semana = setOf(PeriodoReporte.SIETE_DIAS, PeriodoReporte.MES)
        private val mes = setOf(PeriodoReporte.MES)

        private val registros = listOf(
            registro("1", "Ana López", RESIDENTE, ENTRADA, 10, hoy),
            registro("2", "Lucía Herrera", VISITA, ENTRADA, 9, hoy, "QR vencido"),
            registro("3", "Ana López", RESIDENTE, SALIDA, 8, hoy),
            registro("4", "Gas del Norte", PROVEEDOR, ENTRADA, 7, semana),
            registro("5", "Gas del Norte", PROVEEDOR, SALIDA, 6, semana),
            registro("6", "Agua Purificada Sol", PROVEEDOR, ENTRADA, 5, mes, "Proveedor sin cita")
        )

        private fun registro(
            id: String,
            persona: String,
            tipoPersona: TipoUsuario,
            tipo: TipoAcceso,
            fechaHora: Long,
            periodos: Set<PeriodoReporte>,
            incidencia: String? = null
        ) = Registro(
            AccesoReporte(Acceso(id = id, placa = "ABC-123", tipo = tipo, fechaHora = fechaHora), persona, tipoPersona, incidencia),
            periodos
        )

        override suspend fun obtenerAccesos(periodo: PeriodoReporte, tipoPersona: TipoUsuario?) =
            Result.success(
                registros
                    .filter { periodo in it.periodos && (tipoPersona == null || it.reporte.tipoPersona == tipoPersona) }
                    .map { it.reporte }
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

    private fun ReportesAccesosViewModel.reporteActual() =
        (uiState.value as ReportesAccesosUiState.Exito).reporte

    @Test
    fun losTotalesYLaListaCambianAlCambiarLosFiltros() {
        val viewModel = ReportesAccesosViewModel(ObtenerReporteAccesosUseCase(RepositorioFalso()))

        // Valores iniciales: Hoy + Todos.
        assertEquals(ResumenAccesos(entradas = 2, salidas = 1, incidencias = 1), viewModel.reporteActual().resumen)
        assertEquals(3, viewModel.reporteActual().accesos.size)

        viewModel.onPeriodoChange(PeriodoReporte.MES)
        assertEquals(ResumenAccesos(entradas = 4, salidas = 2, incidencias = 2), viewModel.reporteActual().resumen)
        assertEquals(6, viewModel.reporteActual().accesos.size)

        viewModel.onTipoPersonaChange(PROVEEDOR)
        assertEquals(ResumenAccesos(entradas = 2, salidas = 1, incidencias = 1), viewModel.reporteActual().resumen)
        assertTrue(viewModel.reporteActual().accesos.all { it.tipoPersona == PROVEEDOR })

        viewModel.onPeriodoChange(PeriodoReporte.SIETE_DIAS)
        assertEquals(ResumenAccesos(entradas = 1, salidas = 1, incidencias = 0), viewModel.reporteActual().resumen)

        // Hoy + Proveedores: lista vacía y totales en cero.
        viewModel.onPeriodoChange(PeriodoReporte.HOY)
        assertEquals(ResumenAccesos(entradas = 0, salidas = 0, incidencias = 0), viewModel.reporteActual().resumen)
        assertTrue(viewModel.reporteActual().accesos.isEmpty())
    }
}
