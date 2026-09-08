package com.example.accesovehicular.data.repository

import com.example.accesovehicular.domain.model.RegistroVehiculo
import com.example.accesovehicular.domain.repository.RegistroVehiculoRepository
import kotlinx.coroutines.delay

/**
 * Implementación mock mientras no está lista la API real.
 * Para conectarla: reemplazar el cuerpo por una llamada a un futuro
 * VehiculoRegistroApiService a través de NetworkModule — la firma no cambia,
 * así que ViewModel y pantalla no se tocan.
 */
class RegistroVehiculoRepositoryImpl : RegistroVehiculoRepository {

    override suspend fun registrar(registro: RegistroVehiculo): Result<Unit> {
        delay(1200)
        return Result.success(Unit)
    }
}
