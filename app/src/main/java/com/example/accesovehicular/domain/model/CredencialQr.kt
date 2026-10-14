package com.example.accesovehicular.domain.model

/**
 * Datos del QR personal del usuario.
 * [contenido] es el texto exacto que se codifica en el QR: hoy un JSON con
 * id y placa, más adelante el token que entregue la API.
 */
data class CredencialQr(
    val idUsuario: String,
    val placa: String,
    val contenido: String
)
