package com.example.accesovehicular.domain.usecase

import com.example.accesovehicular.domain.model.Persona
import com.example.accesovehicular.domain.model.TipoAccionBitacora
import com.example.accesovehicular.domain.model.TipoUsuario
import com.example.accesovehicular.domain.repository.BitacoraRepository
import com.example.accesovehicular.domain.repository.PersonaRepository

class RegistrarPersonaUseCase(
    private val repository: PersonaRepository,
    private val bitacora: BitacoraRepository
) {
    suspend operator fun invoke(nombre: String, tipo: TipoUsuario): Result<Persona> {
        if (nombre.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre es obligatorio"))
        }
        // Si la bitácora falla, el alta ya se hizo: no se reporta como error.
        return repository.registrarPersona(nombre.trim(), tipo).onSuccess { persona ->
            bitacora.registrar(
                TipoAccionBitacora.ALTA,
                "Alta de ${persona.nombre} (${persona.tipo.name.lowercase()})"
            )
        }
    }
}
