package com.example.accesovehicular.domain.model

/** Lo que decide la caseta al leer un QR. */
sealed interface ResultadoValidacionQr {
    data class Permitido(
        val persona: Persona,
        val placa: String,
        val fechaHora: Long
    ) : ResultadoValidacionQr

    /** [persona] es null cuando el idUsuario del QR no existe. */
    data class Denegado(
        val motivo: String,
        val placa: String,
        val persona: Persona?
    ) : ResultadoValidacionQr

    /** El contenido no tiene el formato esperado. */
    data object NoValido : ResultadoValidacionQr
}
