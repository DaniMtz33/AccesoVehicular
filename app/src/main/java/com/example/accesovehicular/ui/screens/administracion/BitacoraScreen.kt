package com.example.accesovehicular.ui.screens.administracion

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BitacoraScreen(
    modifier: Modifier = Modifier,
    onVolver: () -> Unit = {}
) {
    SeccionEnConstruccion(
        titulo = "Bitácora",
        onVolver = onVolver,
        modifier = modifier
    )
}
