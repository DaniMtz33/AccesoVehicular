package com.example.accesovehicular.ui.screens.administracion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeAdministracionScreen(
    modifier: Modifier = Modifier,
    onCerrarSesion: () -> Unit = {},
    onEscanearQr: () -> Unit = {},
    onGestionUsuarios: () -> Unit = {},
    onReportesAccesos: () -> Unit = {},
    onBitacora: () -> Unit = {},
    viewModel: HomeAdministracionViewModel = viewModel(),
    dashboardViewModel: DashboardViewModel = viewModel()
) {
    val sesionCerrada by viewModel.sesionCerrada.collectAsState()
    val dashboard by dashboardViewModel.uiState.collectAsState()

    LaunchedEffect(sesionCerrada) {
        if (sesionCerrada) {
            onCerrarSesion()
        }
    }

    // Al entrar y al regresar de cualquier sección, para reflejar altas, bajas y escaneos.
    LifecycleResumeEffect(dashboardViewModel) {
        dashboardViewModel.cargar()
        onPauseOrDispose { }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Panel de Administración",
            style = MaterialTheme.typography.headlineSmall
        )

        ResumenDashboard(
            uiState = dashboard,
            onReintentarAccesos = dashboardViewModel::cargarAccesosHoy,
            onReintentarPersonas = dashboardViewModel::cargarPersonas,
            onReintentarAcciones = dashboardViewModel::cargarUltimasAcciones
        )

        HorizontalDivider(modifier = Modifier.fillMaxWidth())

        Button(onClick = onEscanearQr) {
            Text("Escanear QR en caseta")
        }

        Button(onClick = onGestionUsuarios) {
            Text("Gestión de usuarios")
        }

        Button(onClick = onReportesAccesos) {
            Text("Reportes de accesos")
        }

        Button(onClick = onBitacora) {
            Text("Bitácora")
        }

        Button(onClick = viewModel::onCerrarSesionClick) {
            Text("Cerrar sesión")
        }
    }
}
