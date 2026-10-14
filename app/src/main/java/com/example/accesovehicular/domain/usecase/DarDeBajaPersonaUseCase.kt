package com.example.accesovehicular.domain.usecase

import com.example.accesovehicular.domain.model.Persona
import com.example.accesovehicular.domain.model.TipoAccionBitacora
import com.example.accesovehicular.domain.repository.BitacoraRepository
import com.example.accesovehicular.domain.repository.PersonaRepository

class DarDeBajaPersonaUseCase(
    private val repository: PersonaRepository,
    private val bitacora: BitacoraRepository
) {
    // Si la bitácora falla, la baja ya se hizo: no se reporta como error.
    suspend operator fun invoke(id: String): Result<Persona> =
        repository.darDeBaja(id).onSuccess { persona ->
            bitacora.registrar(
                TipoAccionBitacora.BAJA,
                "Baja de ${persona.nombre} (${persona.tipo.name.lowercase()})"
            )
        }
}
