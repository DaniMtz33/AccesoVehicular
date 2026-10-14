package com.example.accesovehicular.domain.usecase

import com.example.accesovehicular.domain.model.Persona
import com.example.accesovehicular.domain.model.TipoUsuario
import com.example.accesovehicular.domain.repository.PersonaRepository

class RegistrarPersonaUseCase(
    private val repository: PersonaRepository
) {
    suspend operator fun invoke(nombre: String, tipo: TipoUsuario): Result<Persona> {
        if (nombre.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre es obligatorio"))
        }
        return repository.registrarPersona(nombre.trim(), tipo)
    }
}
