package com.example.accesovehicular.domain.usecase

import com.example.accesovehicular.domain.model.Persona
import com.example.accesovehicular.domain.repository.PersonaRepository

class ObtenerPersonasUseCase(
    private val repository: PersonaRepository
) {
    suspend operator fun invoke(): Result<List<Persona>> = repository.obtenerPersonas()
}
