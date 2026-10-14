package com.example.accesovehicular.data.qr

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * Formato provisional del texto dentro del QR: {"v":1,"idUsuario":"...","placa":"..."}.
 * [version] permite cambiar el formato sin romper lectores viejos. Cuando la API
 * entregue un token, el repositorio usará el token como contenido y esto dejará de usarse.
 */
data class ContenidoQr(
    @param:Json(name = "v") val version: Int = VERSION_ACTUAL,
    @param:Json(name = "idUsuario") val idUsuario: String,
    @param:Json(name = "placa") val placa: String
) {
    companion object {
        const val VERSION_ACTUAL = 1

        private val adapter = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
            .adapter(ContenidoQr::class.java)

        fun deserializar(texto: String): ContenidoQr? =
            runCatching { adapter.fromJson(texto) }.getOrNull()
    }

    fun serializar(): String = adapter.toJson(this)
}
