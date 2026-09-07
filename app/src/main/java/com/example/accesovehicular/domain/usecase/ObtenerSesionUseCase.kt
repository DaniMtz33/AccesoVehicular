package com.example.accesovehicular.domain.usecase

import com.example.accesovehicular.domain.model.Sesion
import com.example.accesovehicular.domain.repository.AuthRepository

class ObtenerSesionUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Sesion? = repository.obtenerSesionGuardada()
}
