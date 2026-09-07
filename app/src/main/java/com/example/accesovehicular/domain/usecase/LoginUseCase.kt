package com.example.accesovehicular.domain.usecase

import com.example.accesovehicular.domain.model.Sesion
import com.example.accesovehicular.domain.repository.AuthRepository

class LoginUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(usuario: String, contrasena: String): Result<Sesion> =
        repository.login(usuario, contrasena)
}
