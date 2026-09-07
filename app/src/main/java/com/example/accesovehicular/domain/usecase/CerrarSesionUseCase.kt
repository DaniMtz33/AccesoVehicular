package com.example.accesovehicular.domain.usecase

import com.example.accesovehicular.domain.repository.AuthRepository

class CerrarSesionUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() = repository.cerrarSesion()
}
