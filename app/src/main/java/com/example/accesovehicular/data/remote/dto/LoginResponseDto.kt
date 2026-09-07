package com.example.accesovehicular.data.remote.dto

import com.squareup.moshi.Json

data class LoginResponseDto(
    @param:Json(name = "token") val token: String
)
