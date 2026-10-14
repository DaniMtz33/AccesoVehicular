package com.example.accesovehicular.data.repository

import com.example.accesovehicular.domain.model.Acceso
import com.example.accesovehicular.domain.model.AccesoReporte
import com.example.accesovehicular.domain.model.PeriodoReporte
import com.example.accesovehicular.domain.model.TipoAcceso
import com.example.accesovehicular.domain.model.TipoAcceso.ENTRADA
import com.example.accesovehicular.domain.model.TipoAcceso.SALIDA
import com.example.accesovehicular.domain.model.TipoUsuario
import com.example.accesovehicular.domain.model.TipoUsuario.PROVEEDOR
import com.example.accesovehicular.domain.model.TipoUsuario.RESIDENTE
import com.example.accesovehicular.domain.model.TipoUsuario.VISITA
import com.example.accesovehicular.domain.repository.ReporteAccesosRepository
import kotlinx.coroutines.delay
import java.util.Calendar

/**
 * Implementación mock mientras no está lista la API real.
 * Para conectarla: reemplazar el cuerpo por una llamada a un futuro
 * ReporteAccesosApiService a través de NetworkModule (periodo y tipo como parámetros
 * de consulta) — la firma no cambia, así que UseCase, ViewModel y pantalla no se tocan.
 */
class ReporteAccesosRepositoryImpl : ReporteAccesosRepository {

    override suspend fun obtenerAccesos(
        periodo: PeriodoReporte,
        tipoPersona: TipoUsuario?
    ): Result<List<AccesoReporte>> {
        delay(1000)

        if (Math.random() < 0.2) {
            return Result.failure(Exception("No se pudo obtener el reporte de accesos"))
        }

        val ahora = System.currentTimeMillis()
        val desde = when (periodo) {
            PeriodoReporte.HOY -> inicioDelDia(ahora)
            PeriodoReporte.SIETE_DIAS -> ahora - 7 * DIA
            PeriodoReporte.MES -> ahora - 30 * DIA
        }

        val accesos = generarAccesos(ahora).filter { reporte ->
            reporte.acceso.fechaHora >= desde &&
                (tipoPersona == null || reporte.tipoPersona == tipoPersona)
        }
        return Result.success(accesos)
    }

    private fun generarAccesos(ahora: Long): List<AccesoReporte> {
        val inicioHoy = inicioDelDia(ahora)

        // Momento a una fracción del tiempo transcurrido desde medianoche: siempre cae en "Hoy".
        fun hoy(fraccion: Double) = inicioHoy + ((ahora - inicioHoy) * fraccion).toLong()
        fun haceDias(dias: Int, horas: Int) = ahora - dias * DIA - horas * HORA

        val registros = listOf(
            // Hoy (sin proveedores, para poder ver la lista vacía con ese filtro)
            Registro("Ana López", RESIDENTE, "ABC-123", ENTRADA, hoy(0.2)),
            Registro("Carlos Méndez", RESIDENTE, "XYZ-789", ENTRADA, hoy(0.4)),
            Registro("Lucía Herrera", VISITA, "QRS-321", ENTRADA, hoy(0.5), "QR vencido"),
            Registro("Ana López", RESIDENTE, "ABC-123", SALIDA, hoy(0.6)),
            Registro("Lucía Herrera", VISITA, "QRS-321", SALIDA, hoy(0.8)),
            // Últimos 7 días
            Registro("Gas del Norte", PROVEEDOR, "GAS-001", ENTRADA, haceDias(1, 3)),
            Registro("Gas del Norte", PROVEEDOR, "GAS-001", SALIDA, haceDias(1, 2)),
            Registro("Jorge Ramírez", VISITA, "TUV-654", ENTRADA, haceDias(2, 5), "Placa no registrada"),
            Registro("Jorge Ramírez", VISITA, "TUV-654", SALIDA, haceDias(2, 3)),
            Registro("María Torres", RESIDENTE, "LMN-456", ENTRADA, haceDias(3, 8)),
            Registro("María Torres", RESIDENTE, "LMN-456", SALIDA, haceDias(3, 1)),
            Registro("Agua Purificada Sol", PROVEEDOR, "AGU-202", ENTRADA, haceDias(5, 4)),
            Registro("Agua Purificada Sol", PROVEEDOR, "AGU-202", SALIDA, haceDias(5, 3)),
            Registro("Ana López", RESIDENTE, "ABC-123", ENTRADA, haceDias(6, 2)),
            // Último mes
            Registro("Carlos Méndez", RESIDENTE, "XYZ-789", ENTRADA, haceDias(9, 6)),
            Registro("Carlos Méndez", RESIDENTE, "XYZ-789", SALIDA, haceDias(9, 1)),
            Registro("Gas del Norte", PROVEEDOR, "GAS-001", ENTRADA, haceDias(12, 4), "Proveedor sin cita"),
            Registro("Gas del Norte", PROVEEDOR, "GAS-001", SALIDA, haceDias(12, 3)),
            Registro("Lucía Herrera", VISITA, "QRS-321", ENTRADA, haceDias(15, 7)),
            Registro("Lucía Herrera", VISITA, "QRS-321", SALIDA, haceDias(15, 5)),
            Registro("María Torres", RESIDENTE, "LMN-456", ENTRADA, haceDias(20, 2)),
            Registro("Agua Purificada Sol", PROVEEDOR, "AGU-202", ENTRADA, haceDias(25, 5)),
            Registro("Agua Purificada Sol", PROVEEDOR, "AGU-202", SALIDA, haceDias(25, 4)),
            Registro("Jorge Ramírez", VISITA, "TUV-654", ENTRADA, haceDias(28, 3), "QR vencido")
        )

        return registros.mapIndexed { indice, r ->
            AccesoReporte(
                acceso = Acceso(id = (indice + 1).toString(), placa = r.placa, tipo = r.tipo, fechaHora = r.fechaHora),
                persona = r.persona,
                tipoPersona = r.tipoPersona,
                incidencia = r.incidencia
            )
        }
    }

    private fun inicioDelDia(epochMillis: Long): Long =
        Calendar.getInstance().apply {
            timeInMillis = epochMillis
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

    private data class Registro(
        val persona: String,
        val tipoPersona: TipoUsuario,
        val placa: String,
        val tipo: TipoAcceso,
        val fechaHora: Long,
        val incidencia: String? = null
    )

    private companion object {
        const val HORA = 60 * 60_000L
        const val DIA = 24 * HORA
    }
}
