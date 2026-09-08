package com.example.accesovehicular.data.repository

import com.example.accesovehicular.domain.model.Acceso
import com.example.accesovehicular.domain.model.TipoAcceso
import com.example.accesovehicular.domain.repository.HistorialAccesosRepository
import kotlinx.coroutines.delay

/**
 * Implementación mock mientras no está lista la API real.
 * Para conectarla: reemplazar el cuerpo por una llamada a un futuro
 * HistorialApiService a través de NetworkModule — la firma no cambia,
 * así que ViewModel y pantalla no se tocan.
 */
class HistorialAccesosRepositoryImpl : HistorialAccesosRepository {

    override suspend fun obtenerHistorial(): Result<List<Acceso>> {
        delay(1000)

        if (Math.random() < 0.2) {
            return Result.failure(Exception("No se pudo obtener el historial de accesos"))
        }

        val ahora = System.currentTimeMillis()
        val minuto = 60_000L
        val hora = 60 * minuto

        val historial = listOf(
            Acceso(id = "1", placa = "ABC-123", tipo = TipoAcceso.ENTRADA, fechaHora = ahora - 10 * minuto),
            Acceso(id = "2", placa = "XYZ-789", tipo = TipoAcceso.SALIDA, fechaHora = ahora - 45 * minuto),
            Acceso(id = "3", placa = "XYZ-789", tipo = TipoAcceso.ENTRADA, fechaHora = ahora - 2 * hora),
            Acceso(id = "4", placa = "LMN-456", tipo = TipoAcceso.ENTRADA, fechaHora = ahora - 3 * hora),
            Acceso(id = "5", placa = "ABC-123", tipo = TipoAcceso.SALIDA, fechaHora = ahora - 26 * hora),
            Acceso(id = "6", placa = "QRS-321", tipo = TipoAcceso.ENTRADA, fechaHora = ahora - 30 * hora)
        )

        return Result.success(historial.sortedByDescending { it.fechaHora })
    }
}
