package com.example.accesovehicular.data.remote.dto

import com.squareup.moshi.Json

data class VehiculoDto(
    @param:Json(name = "id") val id: String,
    @param:Json(name = "placa") val placa: String,
    @param:Json(name = "propietario") val propietario: String,
    @param:Json(name = "estado") val estado: String
)
