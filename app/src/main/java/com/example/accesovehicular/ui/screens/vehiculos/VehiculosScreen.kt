package com.example.accesovehicular.ui.screens.vehiculos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.accesovehicular.domain.model.Vehiculo

@Composable
fun VehiculosScreen(
    modifier: Modifier = Modifier,
    viewModel: VehiculosViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        when (val estado = uiState) {
            is VehiculosUiState.Cargando -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is VehiculosUiState.Error -> {
                Text(
                    text = "No se pudo cargar la lista: ${estado.mensaje}",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }
            is VehiculosUiState.Exito -> {
                ListaVehiculos(vehiculos = estado.vehiculos)
            }
        }
    }
}

@Composable
private fun ListaVehiculos(vehiculos: List<Vehiculo>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(vehiculos) { vehiculo ->
            Card(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = vehiculo.placa, style = MaterialTheme.typography.titleMedium)
                    Text(text = vehiculo.propietario, style = MaterialTheme.typography.bodyMedium)
                    Text(text = vehiculo.estadoAcceso.name, style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}
