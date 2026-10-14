package com.example.accesovehicular.domain.model

data class ResumenAccesos(
    val entradas: Int,
    val salidas: Int,
    val incidencias: Int
)

data class ReporteAccesos(
    val resumen: ResumenAccesos,
    val accesos: List<AccesoReporte>
)
