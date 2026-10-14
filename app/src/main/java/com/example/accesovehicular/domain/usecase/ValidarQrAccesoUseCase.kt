package com.example.accesovehicular.domain.usecase

import com.example.accesovehicular.domain.model.ResultadoValidacionQr
import com.example.accesovehicular.domain.repository.ValidacionQrRepository

class ValidarQrAccesoUseCase(
    private val repository: ValidacionQrRepository
) {
    suspend operator fun invoke(contenido: String): Result<ResultadoValidacionQr> = repository.validar(contenido)
}
