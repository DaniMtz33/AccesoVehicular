package com.example.accesovehicular.ui.screens.miqr

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MiQrScreen(
    modifier: Modifier = Modifier,
    viewModel: MiQrViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        when (val estado = uiState) {
            is MiQrUiState.Cargando -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is MiQrUiState.Error -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = estado.mensaje)
                    Button(onClick = viewModel::cargarQr) {
                        Text("Reintentar")
                    }
                }
            }
            is MiQrUiState.Exito -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "Mi QR", style = MaterialTheme.typography.headlineSmall)
                    Image(
                        bitmap = estado.imagenQr,
                        contentDescription = "Código QR de acceso",
                        filterQuality = FilterQuality.None,
                        modifier = Modifier
                            .size(260.dp)
                            .background(Color.White)
                    )
                    Text(text = estado.credencial.placa, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "Usuario: ${estado.credencial.idUsuario}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Muestra este código en la caseta de acceso.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
