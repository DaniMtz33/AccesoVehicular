package com.example.accesovehicular.domain.usecase

import com.example.accesovehicular.domain.model.ResultadoValidacionQr
import com.example.accesovehicular.domain.model.TipoAccionBitacora
import com.example.accesovehicular.domain.repository.BitacoraRepository
import com.example.accesovehicular.domain.repository.ValidacionQrRepository

class ValidarQrAccesoUseCase(
    private val repository: ValidacionQrRepository,
    private val bitacora: BitacoraRepository
) {
    // Si la bitácora falla, el resultado del escaneo se muestra igual.
    suspend operator fun invoke(contenido: String): Result<ResultadoValidacionQr> =
        repository.validar(contenido).onSuccess { resultado ->
            when (resultado) {
                is ResultadoValidacionQr.Permitido -> bitacora.registrar(
                    TipoAccionBitacora.ESCANEO,
                    "Escaneo de ${resultado.persona.nombre} · ${resultado.placa}",
                    permitido = true
                )
                is ResultadoValidacionQr.Denegado -> bitacora.registrar(
                    TipoAccionBitacora.ESCANEO,
                    "Escaneo de ${resultado.persona?.nombre ?: "usuario desconocido"} · ${resultado.placa} — ${resultado.motivo}",
                    permitido = false
                )
                ResultadoValidacionQr.NoValido -> bitacora.registrar(
                    TipoAccionBitacora.ESCANEO,
                    "Escaneo de QR no válido",
                    permitido = false
                )
            }
        }
}
