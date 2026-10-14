package com.example.accesovehicular.domain.usecase

import com.example.accesovehicular.domain.model.PeriodoReporte
import com.example.accesovehicular.domain.model.ReporteAccesos
import com.example.accesovehicular.domain.model.ResumenAccesos
import com.example.accesovehicular.domain.model.TipoAcceso
import com.example.accesovehicular.domain.model.TipoUsuario
import com.example.accesovehicular.domain.repository.ReporteAccesosRepository

class ObtenerReporteAccesosUseCase(
    private val repository: ReporteAccesosRepository
) {
    suspend operator fun invoke(periodo: PeriodoReporte, tipoPersona: TipoUsuario?): Result<ReporteAccesos> =
        repository.obtenerAccesos(periodo, tipoPersona).map { accesos ->
            ReporteAccesos(
                resumen = ResumenAccesos(
                    entradas = accesos.count { it.acceso.tipo == TipoAcceso.ENTRADA },
                    salidas = accesos.count { it.acceso.tipo == TipoAcceso.SALIDA },
                    incidencias = accesos.count { it.incidencia != null }
                ),
                accesos = accesos.sortedByDescending { it.acceso.fechaHora }
            )
        }
}
