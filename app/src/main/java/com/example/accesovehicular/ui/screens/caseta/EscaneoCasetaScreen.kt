package com.example.accesovehicular.ui.screens.caseta

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.accesovehicular.domain.model.ResultadoValidacionQr
import com.example.accesovehicular.ui.common.etiqueta
import com.example.accesovehicular.ui.common.formatearHora
import com.example.accesovehicular.ui.screens.scanner.EscanerQr

private val VerdePermitido = Color(0xFF2E7D32)
private val RojoDenegado = Color(0xFFC62828)
private val NaranjaNoValido = Color(0xFFE65100)

/**
 * [escaner] recibe el callback de lectura; por defecto es la cámara real.
 * Las pruebas lo reemplazan para simular lecturas sin cámara.
 */
@Composable
fun EscaneoCasetaScreen(
    modifier: Modifier = Modifier,
    viewModel: EscaneoCasetaViewModel = viewModel(),
    escaner: @Composable (onCodigoDetectado: (String) -> Unit) -> Unit = { onCodigoDetectado ->
        EscanerQr(onCodigoDetectado = onCodigoDetectado, modifier = Modifier.fillMaxSize())
    }
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        when (val estado = uiState) {
            is EscaneoCasetaUiState.Escaneando -> {
                escaner(viewModel::onCodigoDetectado)
                Text(
                    text = "Apunta la cámara al QR del conductor",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(16.dp)
                )
            }
            is EscaneoCasetaUiState.Validando -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CircularProgressIndicator()
                    Text("Validando código...")
                }
            }
            is EscaneoCasetaUiState.Resultado -> {
                PantallaResultado(
                    resultado = estado.resultado,
                    onEscanearOtro = viewModel::onEscanearOtro
                )
            }
            is EscaneoCasetaUiState.Error -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = estado.mensaje)
                    Button(onClick = viewModel::onReintentar) {
                        Text("Reintentar")
                    }
                    OutlinedButton(onClick = viewModel::onEscanearOtro) {
                        Text("Escanear otro")
                    }
                }
            }
        }
    }
}

@Composable
private fun PantallaResultado(
    resultado: ResultadoValidacionQr,
    onEscanearOtro: () -> Unit
) {
    val (color, simbolo, titulo) = when (resultado) {
        is ResultadoValidacionQr.Permitido -> Triple(VerdePermitido, "✓", "ACCESO PERMITIDO")
        is ResultadoValidacionQr.Denegado -> Triple(RojoDenegado, "✕", "ACCESO DENEGADO")
        is ResultadoValidacionQr.NoValido -> Triple(NaranjaNoValido, "?", "QR NO VÁLIDO")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
    ) {
        Text(text = simbolo, fontSize = 96.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(
            text = titulo,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        when (resultado) {
            is ResultadoValidacionQr.Permitido -> {
                Detalle(resultado.persona.nombre, grande = true)
                Detalle(resultado.persona.tipo.etiqueta())
                Detalle("Placa: ${resultado.placa}")
                Detalle("Hora: ${formatearHora(resultado.fechaHora)}")
            }
            is ResultadoValidacionQr.Denegado -> {
                resultado.persona?.let { persona ->
                    Detalle(persona.nombre, grande = true)
                    Detalle(persona.tipo.etiqueta())
                }
                Detalle(resultado.motivo)
                Detalle("Placa: ${resultado.placa}")
            }
            is ResultadoValidacionQr.NoValido -> {
                Detalle("El código no tiene el formato esperado.")
            }
        }

        Button(
            onClick = onEscanearOtro,
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = color),
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
                .height(64.dp)
        ) {
            Text("Escanear otro", style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
private fun Detalle(texto: String, grande: Boolean = false) {
    Text(
        text = texto,
        style = if (grande) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.titleMedium,
        color = Color.White,
        textAlign = TextAlign.Center
    )
}
