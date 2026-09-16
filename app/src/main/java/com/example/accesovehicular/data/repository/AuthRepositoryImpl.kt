package com.example.accesovehicular.data.repository

import com.example.accesovehicular.data.local.SesionLocalDataSource
import com.example.accesovehicular.domain.model.Rol
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

        val rol = when {
            usuario.equals("admin", ignoreCase = true) -> Rol.ADMINISTRACION
            usuario == "test" && contrasena == "1234" -> Rol.USUARIO
            else -> null
        }

        return if (rol != null) {
            val token = "mock-token-${System.currentTimeMillis()}"
            SesionLocalDataSource.guardarSesion(token, rol)
            Result.success(Sesion(token = token, rol = rol))
        } else {
            Result.failure(Exception("Usuario o contraseña inválidos"))
        }
    }

    override suspend fun obtenerSesionGuardada(): Sesion? {
        val guardada = SesionLocalDataSource.sesionFlow.first() ?: return null
        return Sesion(token = guardada.token, rol = guardada.rol)
    }

    override suspend fun cerrarSesion() {
        SesionLocalDataSource.borrarSesion()
    }
}
