package com.example.accesovehicular.ui.screens.miqr

import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.accesovehicular.data.qr.ContenidoQr
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

/** Comprueba que el QR de "Mi QR" lo lee el mismo cliente de ML Kit que usa ScannerScreen. */
@RunWith(AndroidJUnit4::class)
class MiQrLecturaMlKitTest {

    @Test
    fun qrGenerado_loLeeMlKit() {
        val contenido = ContenidoQr(idUsuario = "USR-0001", placa = "ABC-123").serializar()
        val bitmap = generarImagenQr(contenido).asAndroidBitmap()

        val scanner = BarcodeScanning.getClient()
        val codigos = Tasks.await(scanner.process(InputImage.fromBitmap(bitmap, 0)), 30, TimeUnit.SECONDS)
        scanner.close()

        assertEquals(1, codigos.size)
        assertEquals(Barcode.FORMAT_QR_CODE, codigos[0].format)
        assertEquals(contenido, codigos[0].rawValue)
    }
}
