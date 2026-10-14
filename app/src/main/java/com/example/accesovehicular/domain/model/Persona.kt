package com.example.accesovehicular.domain.model

data class Persona(
    val id: String,
    val nombre: String,
    val tipo: TipoUsuario,
    val activo: Boolean
)
