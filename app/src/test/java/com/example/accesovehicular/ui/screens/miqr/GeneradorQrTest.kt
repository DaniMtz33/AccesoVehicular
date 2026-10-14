package com.example.accesovehicular.ui.screens.miqr

import com.example.accesovehicular.data.qr.ContenidoQr
import com.google.zxing.BinaryBitmap
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader
import org.junit.Assert.assertEquals
import org.junit.Test

class GeneradorQrTest {

    @Test
    fun contenidoQr_serializaYDeserializa() {
        val original = ContenidoQr(idUsuario = "USR-0001", placa = "ABC-123")

        val json = original.serializar()

        assertEquals("""{"v":1,"idUsuario":"USR-0001","placa":"ABC-123"}""", json)
        assertEquals(original, ContenidoQr.deserializar(json))
    }

    @Test
    fun qrGenerado_seLeeConElMismoContenido() {
        val contenido = ContenidoQr(idUsuario = "USR-0001", placa = "ABC-123").serializar()

        val matriz = generarMatrizQr(contenido, 512)
        val pixeles = IntArray(matriz.width * matriz.height) { i ->
            if (matriz[i % matriz.width, i / matriz.width]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt()
        }
        val fuente = RGBLuminanceSource(matriz.width, matriz.height, pixeles)
        val leido = QRCodeReader().decode(BinaryBitmap(HybridBinarizer(fuente))).text

        assertEquals(contenido, leido)
        assertEquals("ABC-123", ContenidoQr.deserializar(leido)?.placa)
    }
}
