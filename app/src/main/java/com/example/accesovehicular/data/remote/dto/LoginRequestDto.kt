package com.example.accesovehicular.data.remote.dto

import com.squareup.moshi.Json

data class LoginRequestDto(
    @param:Json(name = "usuario") val usuario: String,
    @param:Json(name = "contrasena") val contrasena: String
)
