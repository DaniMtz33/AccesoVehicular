package com.example.accesovehicular.domain.repository

import com.example.accesovehicular.domain.model.RegistroVehiculo

interface RegistroVehiculoRepository {
    suspend fun registrar(registro: RegistroVehiculo): Result<Unit>
}
