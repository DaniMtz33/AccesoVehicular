package com.example.accesovehicular.domain.usecase

import com.example.accesovehicular.domain.model.Acceso
import com.example.accesovehicular.domain.repository.HistorialAccesosRepository

class ObtenerHistorialAccesosUseCase(
    private val repository: HistorialAccesosRepository
) {
    suspend operator fun invoke(): Result<List<Acceso>> = repository.obtenerHistorial()
}
