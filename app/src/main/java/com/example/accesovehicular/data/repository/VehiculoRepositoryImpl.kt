package com.example.accesovehicular.data.repository

import com.example.accesovehicular.data.remote.NetworkModule
import com.example.accesovehicular.data.remote.api.VehiculoApiService
import com.example.accesovehicular.data.remote.dto.VehiculoDto
import com.example.accesovehicular.domain.model.EstadoAcceso
import com.example.accesovehicular.domain.model.Vehiculo
import com.example.accesovehicular.domain.repository.VehiculoRepository

class VehiculoRepositoryImpl(
    private val api: VehiculoApiService = NetworkModule.vehiculoApiService
) : VehiculoRepository {

    override suspend fun obtenerVehiculos(): Result<List<Vehiculo>> {
        return try {
            val vehiculos = api.obtenerVehiculos().map { it.toDomain() }
            Result.success(vehiculos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun VehiculoDto.toDomain(): Vehiculo {
        val estado = EstadoAcceso.entries.find { it.name.equals(estado, ignoreCase = true) }
            ?: EstadoAcceso.PENDIENTE
        return Vehiculo(
            id = id,
            placa = placa,
            propietario = propietario,
            estadoAcceso = estado
        )
    }
}
