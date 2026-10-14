package com.example.accesovehicular.data.repository

import com.example.accesovehicular.data.qr.ContenidoQr
import com.example.accesovehicular.domain.model.CredencialQr
import com.example.accesovehicular.domain.repository.CredencialQrRepository
import kotlinx.coroutines.delay

/**
 * Implementación mock mientras no está lista la API real.
 * Para conectarla: reemplazar el cuerpo por una llamada a un futuro endpoint
 * que devuelva el token del QR y ponerlo en CredencialQr.contenido — la firma
 * no cambia, así que ViewModel y pantalla no se tocan.
 */
class CredencialQrRepositoryImpl : CredencialQrRepository {

    override suspend fun obtenerCredencial(): Result<CredencialQr> {
        delay(800)

        if (Math.random() < 0.2) {
            return Result.failure(Exception("No se pudo obtener tu código QR"))
        }

        // El registro de vehículo todavía no persiste nada, así que se usan datos de ejemplo.
        // "1" es Ana López en la lista mock de Gestión de usuarios, para que la caseta lo reconozca.
        val idUsuario = "1"
        val placa = "ABC-123"
        val contenido = ContenidoQr(idUsuario = idUsuario, placa = placa).serializar()

        return Result.success(CredencialQr(idUsuario = idUsuario, placa = placa, contenido = contenido))
    }
}
