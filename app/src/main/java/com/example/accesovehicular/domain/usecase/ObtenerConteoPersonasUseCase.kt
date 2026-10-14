package com.example.accesovehicular.domain.usecase

import com.example.accesovehicular.domain.model.ConteoPersonas
import com.example.accesovehicular.domain.repository.PersonaRepository

class ObtenerConteoPersonasUseCase(
    private val repository: PersonaRepository
) {
    suspend operator fun invoke(): Result<ConteoPersonas> =
        repository.obtenerPersonas().map { personas ->
            val activas = personas.count { it.activo }
            ConteoPersonas(activas = activas, inactivas = personas.size - activas)
        }
}
