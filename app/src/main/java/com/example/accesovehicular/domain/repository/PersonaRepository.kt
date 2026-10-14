package com.example.accesovehicular.domain.repository

import com.example.accesovehicular.domain.model.Persona
import com.example.accesovehicular.domain.model.TipoUsuario

interface PersonaRepository {
    suspend fun obtenerPersonas(): Result<List<Persona>>
    suspend fun registrarPersona(nombre: String, tipo: TipoUsuario): Result<Persona>
    suspend fun darDeBaja(id: String): Result<Persona>
}
