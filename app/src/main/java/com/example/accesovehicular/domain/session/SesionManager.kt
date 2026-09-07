package com.example.accesovehicular.domain.session

/**
 * Guarda la sesión activa en memoria de proceso.
 * Se pierde al matar la app; migrar a almacenamiento persistente es un paso futuro.
 */
object SesionManager {
    var token: String? = null
        private set

    fun guardarToken(token: String) {
        this.token = token
    }

    fun cerrarSesion() {
        token = null
    }
}
