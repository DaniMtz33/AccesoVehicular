package com.example.accesovehicular.domain.model

/**
 * Un [Acceso] visto desde Administración: agrega quién pasó y si hubo incidencia.
 */
data class AccesoReporte(
    val acceso: Acceso,
    val persona: String,
    val tipoPersona: TipoUsuario,
    /** Descripción de la incidencia (p. ej. "QR vencido"), o null si el acceso fue normal. */
    val incidencia: String? = null
)
