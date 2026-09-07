package com.example.accesovehicular.domain.repository

import com.example.accesovehicular.domain.model.Vehiculo

interface VehiculoRepository {
    suspend fun obtenerVehiculos(): Result<List<Vehiculo>>
}
