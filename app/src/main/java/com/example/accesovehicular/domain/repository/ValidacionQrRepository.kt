package com.example.accesovehicular.domain.repository

import com.example.accesovehicular.domain.model.ResultadoValidacionQr

interface ValidacionQrRepository {
    suspend fun validar(contenido: String): Result<ResultadoValidacionQr>
}
