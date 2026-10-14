package com.example.accesovehicular.data.repository

import com.example.accesovehicular.domain.model.RegistroBitacora
import com.example.accesovehicular.domain.model.TipoAccionBitacora
import com.example.accesovehicular.domain.repository.BitacoraRepository
import kotlinx.coroutines.delay

/**
 * Implementación mock mientras no está lista la API real.
 * Para conectarla: reemplazar el cuerpo de cada método por una llamada a un futuro
 * BitacoraApiService a través de NetworkModule — la firma no cambia,
 * así que use cases, ViewModel y pantalla no se tocan.
 */
class BitacoraRepositoryImpl : BitacoraRepository {

    override suspend fun obtenerRegistros(tipo: TipoAccionBitacora?): Result<List<RegistroBitacora>> {
        delay(800)

        if (Math.random() < 0.2) {
            return Result.failure(Exception("No se pudo obtener la bitácora"))
        }

        return Result.success(registros.filter { tipo == null || it.tipo == tipo })
    }

    override suspend fun registrar(
        tipo: TipoAccionBitacora,
        descripcion: String,
        permitido: Boolean?
    ): Result<RegistroBitacora> {
        val nuevo = RegistroBitacora(
            id = "b${siguienteId++}",
            fechaHora = System.currentTimeMillis(),
            // La sesión aún no guarda el nombre de usuario; cuando lo haga, se toma de ahí.
            autor = AUTOR_SESION,
            tipo = tipo,
            descripcion = descripcion,
            permitido = permitido
        )
        registros.add(0, nuevo)
        return Result.success(nuevo)
    }

    // Vive a nivel de clase para que los registros sobrevivan al salir y volver a la pantalla.
    private companion object {
        const val AUTOR_SESION = "Administrador"
        const val HORA = 60 * 60 * 1000L

        private val ahora = System.currentTimeMillis()

        val registros = mutableListOf(
            RegistroBitacora(
                id = "b8", fechaHora = ahora - 2 * HORA, autor = "Caseta principal",
                tipo = TipoAccionBitacora.ESCANEO, descripcion = "Escaneo de Gas del Norte · GDN-450", permitido = true
            ),
            RegistroBitacora(
                id = "b7", fechaHora = ahora - 5 * HORA, autor = "Administrador",
                tipo = TipoAccionBitacora.ALTA, descripcion = "Alta de Lucía Herrera (visita)"
            ),
            RegistroBitacora(
                id = "b6", fechaHora = ahora - 20 * HORA, autor = "Caseta principal",
                tipo = TipoAccionBitacora.ESCANEO,
                descripcion = "Escaneo de Jorge Ramírez · XYZ-987 — Usuario dado de baja", permitido = false
            ),
            RegistroBitacora(
                id = "b5", fechaHora = ahora - 26 * HORA, autor = "Administrador",
                tipo = TipoAccionBitacora.BAJA, descripcion = "Baja de Jorge Ramírez (visita)"
            ),
            RegistroBitacora(
                id = "b4", fechaHora = ahora - 50 * HORA, autor = "Caseta principal",
                tipo = TipoAccionBitacora.ESCANEO, descripcion = "Escaneo de Ana López · ABC-123", permitido = true
            ),
            RegistroBitacora(
                id = "b3", fechaHora = ahora - 74 * HORA, autor = "Administrador",
                tipo = TipoAccionBitacora.BAJA, descripcion = "Baja de María Torres (residente)"
            ),
            RegistroBitacora(
                id = "b2", fechaHora = ahora - 122 * HORA, autor = "Administrador",
                tipo = TipoAccionBitacora.ALTA, descripcion = "Alta de Jorge Ramírez (visita)"
            ),
            RegistroBitacora(
                id = "b1", fechaHora = ahora - 146 * HORA, autor = "Administrador",
                tipo = TipoAccionBitacora.ALTA, descripcion = "Alta de María Torres (residente)"
            )
        )
        var siguienteId = 9
    }
}
