package com.example.accesovehicular.data.repository

import com.example.accesovehicular.data.qr.ContenidoQr
import com.example.accesovehicular.domain.model.ResultadoValidacionQr
import com.example.accesovehicular.domain.repository.PersonaRepository
import com.example.accesovehicular.domain.repository.ValidacionQrRepository
import kotlinx.coroutines.delay

/**
 * Implementación mock mientras no está lista la API real.
 * Usa la misma lista de personas que Gestión de usuarios, así que dar de baja
 * a alguien cambia el resultado del escaneo.
 * Para conectarla: reemplazar el cuerpo por una llamada a un futuro endpoint que
 * valide el contenido del QR — la firma no cambia, así que ViewModel y pantalla no se tocan.
 */
class ValidacionQrRepositoryImpl(
    private val personaRepository: PersonaRepository = PersonaRepositoryImpl()
) : ValidacionQrRepository {

    override suspend fun validar(contenido: String): Result<ResultadoValidacionQr> {
        delay(500)

        val qr = ContenidoQr.deserializar(contenido)
        if (qr == null ||
            qr.version != ContenidoQr.VERSION_ACTUAL ||
            qr.idUsuario.isBlank() ||
            qr.placa.isBlank()
        ) {
            return Result.success(ResultadoValidacionQr.NoValido)
        }

        return personaRepository.buscarPorId(qr.idUsuario).map { persona ->
            when {
                persona == null -> ResultadoValidacionQr.Denegado(
                    motivo = "Usuario no registrado",
                    placa = qr.placa,
                    persona = null
                )
                !persona.activo -> ResultadoValidacionQr.Denegado(
                    motivo = "Usuario dado de baja",
                    placa = qr.placa,
                    persona = persona
                )
                else -> ResultadoValidacionQr.Permitido(
                    persona = persona,
                    placa = qr.placa,
                    fechaHora = System.currentTimeMillis()
                )
            }
        }
    }
}
