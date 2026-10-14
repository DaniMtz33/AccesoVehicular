package com.example.accesovehicular.data.repository

import com.example.accesovehicular.domain.model.Persona
import com.example.accesovehicular.domain.model.TipoUsuario
import com.example.accesovehicular.domain.repository.PersonaRepository
import kotlinx.coroutines.delay

/**
 * Implementación mock mientras no está lista la API real.
 * Para conectarla: reemplazar el cuerpo de cada método por una llamada a un futuro
 * PersonaApiService a través de NetworkModule — la firma no cambia,
 * así que ViewModel y pantalla no se tocan.
 */
class PersonaRepositoryImpl : PersonaRepository {

    override suspend fun obtenerPersonas(): Result<List<Persona>> {
        delay(1000)

        if (Math.random() < 0.2) {
            return Result.failure(Exception("No se pudo obtener la lista de personas"))
        }

        return Result.success(personas.toList())
    }

    override suspend fun registrarPersona(nombre: String, tipo: TipoUsuario): Result<Persona> {
        delay(1200)
        val nueva = Persona(
            id = (siguienteId++).toString(),
            nombre = nombre,
            tipo = tipo,
            activo = true
        )
        personas.add(0, nueva)
        return Result.success(nueva)
    }

    override suspend fun darDeBaja(id: String): Result<Persona> {
        delay(800)
        val indice = personas.indexOfFirst { it.id == id }
        if (indice == -1) {
            return Result.failure(Exception("La persona ya no existe"))
        }
        val actualizada = personas[indice].copy(activo = false)
        personas[indice] = actualizada
        return Result.success(actualizada)
    }

    override suspend fun buscarPorId(id: String): Result<Persona?> {
        delay(300)
        return Result.success(personas.firstOrNull { it.id == id })
    }

    // Vive a nivel de clase para que las altas y bajas sobrevivan al salir y volver a la pantalla.
    private companion object {
        val personas = mutableListOf(
            Persona(id = "1", nombre = "Ana López", tipo = TipoUsuario.RESIDENTE, activo = true),
            Persona(id = "2", nombre = "Carlos Méndez", tipo = TipoUsuario.RESIDENTE, activo = true),
            Persona(id = "3", nombre = "Lucía Herrera", tipo = TipoUsuario.VISITA, activo = true),
            Persona(id = "4", nombre = "Jorge Ramírez", tipo = TipoUsuario.VISITA, activo = false),
            Persona(id = "5", nombre = "Gas del Norte", tipo = TipoUsuario.PROVEEDOR, activo = true),
            Persona(id = "6", nombre = "Agua Purificada Sol", tipo = TipoUsuario.PROVEEDOR, activo = true),
            Persona(id = "7", nombre = "María Torres", tipo = TipoUsuario.RESIDENTE, activo = false)
        )
        var siguienteId = 8
    }
}
