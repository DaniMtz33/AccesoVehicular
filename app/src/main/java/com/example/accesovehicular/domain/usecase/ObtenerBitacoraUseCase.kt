package com.example.accesovehicular.domain.usecase

import com.example.accesovehicular.domain.model.RegistroBitacora
import com.example.accesovehicular.domain.model.TipoAccionBitacora
import com.example.accesovehicular.domain.repository.BitacoraRepository

class ObtenerBitacoraUseCase(
    private val repository: BitacoraRepository
) {
    suspend operator fun invoke(tipo: TipoAccionBitacora?): Result<List<RegistroBitacora>> =
        repository.obtenerRegistros(tipo).map { registros -> registros.sortedByDescending { it.fechaHora } }
}
