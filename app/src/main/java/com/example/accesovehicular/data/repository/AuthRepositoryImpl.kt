package com.example.accesovehicular.data.repository

import com.example.accesovehicular.data.local.SesionLocalDataSource
import com.example.accesovehicular.domain.model.Sesion
import com.example.accesovehicular.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

/**
 * Implementación mock mientras no está lista la API real de Elizabeth.
 * Para volver a la llamada real: reemplaza el cuerpo de login() por una invocación a
 * NetworkModule.authApiService.login(LoginRequestDto(usuario, contrasena)) — la firma
 * de esta clase no cambia, así que ViewModel y pantalla no se tocan.
 */
class AuthRepositoryImpl : AuthRepository {

    override suspend fun login(usuario: String, contrasena: String): Result<Sesion> {
        delay(1500)
        return if (usuario == "test" && contrasena == "1234") {
            val token = "mock-token-${System.currentTimeMillis()}"
            SesionLocalDataSource.guardarToken(token)
            Result.success(Sesion(token = token))
        } else {
            Result.failure(Exception("Usuario o contraseña inválidos"))
        }
    }

    override suspend fun obtenerSesionGuardada(): Sesion? {
        val token = SesionLocalDataSource.tokenFlow.first()
        return token?.let { Sesion(token = it) }
    }

    override suspend fun cerrarSesion() {
        SesionLocalDataSource.borrarToken()
    }
}
