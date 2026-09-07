package com.example.accesovehicular.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onCerrarSesion: () -> Unit = {},
    onEscanearQr: () -> Unit = {},
    viewModel: HomeViewModel = viewModel()
) {
    val sesionCerrada by viewModel.sesionCerrada.collectAsState()

    LaunchedEffect(sesionCerrada) {
        if (sesionCerrada) {
            onCerrarSesion()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(
            text = "Bienvenido",
            style = MaterialTheme.typography.headlineSmall
        )

        Button(onClick = onEscanearQr) {
            Text("Escanear QR")
        }

        Button(onClick = viewModel::onCerrarSesionClick) {
            Text("Cerrar sesión")
        }
    }
}
