package com.example.accesovehicular.ui.screens.administracion

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun GestionUsuariosScreen(
    modifier: Modifier = Modifier,
    onVolver: () -> Unit = {}
) {
    SeccionEnConstruccion(
        titulo = "Gestión de usuarios",
        onVolver = onVolver,
        modifier = modifier
    )
}
