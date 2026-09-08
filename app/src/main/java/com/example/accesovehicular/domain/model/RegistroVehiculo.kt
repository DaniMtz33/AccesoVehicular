package com.example.accesovehicular.domain.model

data class RegistroVehiculo(
    val placa: String,
    val marca: String,
    val modelo: String,
    val color: String,
    val tipoUsuario: TipoUsuario
)
