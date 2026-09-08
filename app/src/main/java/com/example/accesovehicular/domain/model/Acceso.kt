package com.example.accesovehicular.domain.model

data class Acceso(
    val id: String,
    val placa: String,
    val tipo: TipoAcceso,
    val fechaHora: Long
)
