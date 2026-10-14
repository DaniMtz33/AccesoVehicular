package com.example.accesovehicular.domain.model

enum class TipoAccionBitacora {
    ALTA,
    BAJA,
    ESCANEO
}

/** Una acción de un administrador, tal como queda en la bitácora. */
data class RegistroBitacora(
    val id: String,
    val fechaHora: Long,
    val autor: String,
    val tipo: TipoAccionBitacora,
    val descripcion: String,
    /** Solo en escaneos: true = permitido, false = denegado. null en altas y bajas. */
    val permitido: Boolean? = null
)
