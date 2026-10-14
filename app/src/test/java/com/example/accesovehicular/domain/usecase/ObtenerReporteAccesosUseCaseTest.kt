package com.example.accesovehicular.domain.usecase

import com.example.accesovehicular.domain.model.Acceso
import com.example.accesovehicular.domain.model.AccesoReporte
import com.example.accesovehicular.domain.model.PeriodoReporte
import com.example.accesovehicular.domain.model.ResumenAccesos
import com.example.accesovehicular.domain.model.TipoAcceso
import com.example.accesovehicular.domain.model.TipoUsuario
import com.example.accesovehicular.domain.repository.ReporteAccesosRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ObtenerReporteAccesosUseCaseTest {

    private fun reporte(id: String, tipo: TipoAcceso, fechaHora: Long, incidencia: String? = null) =
        AccesoReporte(
            acceso = Acceso(id = id, placa = "ABC-123", tipo = tipo, fechaHora = fechaHora),
            persona = "Ana López",
            tipoPersona = TipoUsuario.RESIDENTE,
            incidencia = incidencia
        )

    private fun repositorioCon(resultado: Result<List<AccesoReporte>>) = object : ReporteAccesosRepository {
        override suspend fun obtenerAccesos(periodo: PeriodoReporte, tipoPersona: TipoUsuario?) = resultado
    }

    @Test
    fun calculaTotalesYOrdenaDelMasRecienteAlMasAntiguo() = runBlocking {
        val accesos = listOf(
            reporte("1", TipoAcceso.ENTRADA, fechaHora = 100),
            reporte("2", TipoAcceso.SALIDA, fechaHora = 300, incidencia = "QR vencido"),
            reporte("3", TipoAcceso.ENTRADA, fechaHora = 200, incidencia = "Placa no registrada")
        )
        val useCase = ObtenerReporteAccesosUseCase(repositorioCon(Result.success(accesos)))

        val resultado = useCase(PeriodoReporte.HOY, null).getOrThrow()

        assertEquals(ResumenAccesos(entradas = 2, salidas = 1, incidencias = 2), resultado.resumen)
        assertEquals(listOf("2", "3", "1"), resultado.accesos.map { it.acceso.id })
    }

    @Test
    fun listaVaciaDaTotalesEnCero() = runBlocking {
        val useCase = ObtenerReporteAccesosUseCase(repositorioCon(Result.success(emptyList())))

        val resultado = useCase(PeriodoReporte.MES, TipoUsuario.PROVEEDOR).getOrThrow()

        assertEquals(ResumenAccesos(entradas = 0, salidas = 0, incidencias = 0), resultado.resumen)
        assertTrue(resultado.accesos.isEmpty())
    }

    @Test
    fun propagaElErrorDelRepositorio() = runBlocking {
        val useCase = ObtenerReporteAccesosUseCase(repositorioCon(Result.failure(Exception("sin red"))))

        val resultado = useCase(PeriodoReporte.SIETE_DIAS, null)

        assertEquals("sin red", resultado.exceptionOrNull()?.message)
    }
}
