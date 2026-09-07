package com.example.accesovehicular.domain.repository

import com.example.accesovehicular.domain.model.Sesion

interface AuthRepository {
    suspend fun login(usuario: String, contrasena: String): Result<Sesion>
    suspend fun obtenerSesionGuardada(): Sesion?
    suspend fun cerrarSesion()
}
