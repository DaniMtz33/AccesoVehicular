package com.example.accesovehicular.domain.usecase

import com.example.accesovehicular.domain.model.Vehiculo
import com.example.accesovehicular.domain.repository.VehiculoRepository

class ObtenerVehiculosUseCase(
    private val repository: VehiculoRepository
) {
    suspend operator fun invoke(): Result<List<Vehiculo>> = repository.obtenerVehiculos()
}
