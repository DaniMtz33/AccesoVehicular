package com.example.accesovehicular.domain.usecase

import com.example.accesovehicular.domain.model.CredencialQr
import com.example.accesovehicular.domain.repository.CredencialQrRepository

class ObtenerCredencialQrUseCase(
    private val repository: CredencialQrRepository
) {
    suspend operator fun invoke(): Result<CredencialQr> = repository.obtenerCredencial()
}
