package com.example.accesovehicular.ui.screens.administracion

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ReportesAccesosScreen(
    modifier: Modifier = Modifier,
    onVolver: () -> Unit = {}
) {
    SeccionEnConstruccion(
        titulo = "Reportes de accesos",
        onVolver = onVolver,
        modifier = modifier
    )
}
