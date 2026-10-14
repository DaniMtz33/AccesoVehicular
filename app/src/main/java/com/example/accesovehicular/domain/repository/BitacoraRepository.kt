package com.example.accesovehicular.domain.repository

import com.example.accesovehicular.domain.model.RegistroBitacora
import com.example.accesovehicular.domain.model.TipoAccionBitacora

interface BitacoraRepository {
    /** [tipo] null = todas las acciones. */
    suspend fun obtenerRegistros(tipo: TipoAccionBitacora?): Result<List<RegistroBitacora>>

    /** La fecha, la hora y el autor los pone el repositorio, como lo haría el servidor. */
    suspend fun registrar(
        tipo: TipoAccionBitacora,
        descripcion: String,
        permitido: Boolean? = null
    ): Result<RegistroBitacora>
}
