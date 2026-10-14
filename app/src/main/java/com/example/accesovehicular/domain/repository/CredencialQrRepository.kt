package com.example.accesovehicular.domain.repository

import com.example.accesovehicular.domain.model.CredencialQr

interface CredencialQrRepository {
    suspend fun obtenerCredencial(): Result<CredencialQr>
}
