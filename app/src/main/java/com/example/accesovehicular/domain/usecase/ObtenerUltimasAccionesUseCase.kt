package com.example.accesovehicular.domain.usecase

import com.example.accesovehicular.domain.model.RegistroBitacora
import com.example.accesovehicular.domain.repository.BitacoraRepository

class ObtenerUltimasAccionesUseCase(
    repository: BitacoraRepository,
    private val obtenerBitacora: ObtenerBitacoraUseCase = ObtenerBitacoraUseCase(repository)
) {
    // ObtenerBitacoraUseCase ya las ordena de la más reciente a la más antigua.
    suspend operator fun invoke(limite: Int = 3): Result<List<RegistroBitacora>> =
        obtenerBitacora(tipo = null).map { registros -> registros.take(limite) }
}
