package com.example.accesovehicular.domain.usecase

import com.example.accesovehicular.domain.model.Persona
import com.example.accesovehicular.domain.repository.PersonaRepository

class DarDeBajaPersonaUseCase(
    private val repository: PersonaRepository
) {
    suspend operator fun invoke(id: String): Result<Persona> = repository.darDeBaja(id)
}
