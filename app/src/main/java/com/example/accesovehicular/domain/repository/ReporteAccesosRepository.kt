package com.example.accesovehicular.domain.repository

import com.example.accesovehicular.domain.model.AccesoReporte
import com.example.accesovehicular.domain.model.PeriodoReporte
import com.example.accesovehicular.domain.model.TipoUsuario

interface ReporteAccesosRepository {
    /** [tipoPersona] null = todos los tipos. */
    suspend fun obtenerAccesos(periodo: PeriodoReporte, tipoPersona: TipoUsuario?): Result<List<AccesoReporte>>
}
