package com.example.accesovehicular.domain.repository

import com.example.accesovehicular.domain.model.Acceso

interface HistorialAccesosRepository {
    suspend fun obtenerHistorial(): Result<List<Acceso>>
}
