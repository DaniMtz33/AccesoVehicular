package com.example.accesovehicular.domain.model

data class Vehiculo(
    val id: String,
    val placa: String,
    val propietario: String,
    val estadoAcceso: EstadoAcceso
)

enum class EstadoAcceso {
    AUTORIZADO,
    DENEGADO,
    PENDIENTE
}
