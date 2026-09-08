package com.example.accesovehicular.domain.usecase

import com.example.accesovehicular.domain.model.RegistroVehiculo
import com.example.accesovehicular.domain.repository.RegistroVehiculoRepository

class RegistrarVehiculoUseCase(
    private val repository: RegistroVehiculoRepository
) {
    suspend operator fun invoke(registro: RegistroVehiculo): Result<Unit> =
        repository.registrar(registro)
}
