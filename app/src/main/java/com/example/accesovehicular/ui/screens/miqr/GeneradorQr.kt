package com.example.accesovehicular.ui.screens.miqr

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

/** Codifica [contenido] como QR. Separado del Bitmap para poder probarlo en la JVM. */
fun generarMatrizQr(contenido: String, tamanoPx: Int): BitMatrix {
    val hints = mapOf(
        EncodeHintType.CHARACTER_SET to "UTF-8",
        EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.M
    )
    return QRCodeWriter().encode(contenido, BarcodeFormat.QR_CODE, tamanoPx, tamanoPx, hints)
}

fun generarImagenQr(contenido: String, tamanoPx: Int = 512): ImageBitmap {
    val matriz = generarMatrizQr(contenido, tamanoPx)
    val ancho = matriz.width
    val alto = matriz.height
    val pixeles = IntArray(ancho * alto) { i ->
        if (matriz[i % ancho, i / ancho]) Color.BLACK else Color.WHITE
    }
    return Bitmap.createBitmap(pixeles, ancho, alto, Bitmap.Config.ARGB_8888).asImageBitmap()
}
