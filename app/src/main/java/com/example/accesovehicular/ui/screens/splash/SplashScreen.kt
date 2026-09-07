package com.example.accesovehicular.ui.screens.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onSesionActiva: () -> Unit = {},
    onSinSesion: () -> Unit = {},
    viewModel: SplashViewModel = viewModel()
) {
    val destino by viewModel.destino.collectAsState()

    LaunchedEffect(destino) {
        when (destino) {
            DestinoInicial.IrAHome -> onSesionActiva()
            DestinoInicial.IrALogin -> onSinSesion()
            DestinoInicial.Verificando -> Unit
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}
