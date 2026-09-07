package com.example.accesovehicular.data.remote.api

import com.example.accesovehicular.data.remote.dto.VehiculoDto
import retrofit2.http.GET

interface VehiculoApiService {
    @GET("vehiculos")
    suspend fun obtenerVehiculos(): List<VehiculoDto>
}
