package com.example.accesovehicular.ui.screens.administracion

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.accesovehicular.domain.model.RegistroBitacora
import com.example.accesovehicular.domain.model.TipoAccionBitacora
import com.example.accesovehicular.ui.common.formatearFechaHora

@Composable
fun BitacoraScreen(
    modifier: Modifier = Modifier,
    viewModel: BitacoraViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Bitácora",
            style = MaterialTheme.typography.titleLarge
        )

        FiltroTipoAccion(
            seleccionado = viewModel.tipo,
            onSeleccionar = viewModel::onTipoChange
        )

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (val estado = uiState) {
                is BitacoraUiState.Cargando -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is BitacoraUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(text = "No se pudo cargar la bitácora: ${estado.mensaje}")
                        Button(onClick = viewModel::cargarRegistros) {
                            Text("Reintentar")
                        }
                    }
                }
                is BitacoraUiState.Exito -> {
                    if (estado.registros.isEmpty()) {
                        Text(
                            text = "No hay acciones registradas para este filtro.",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        ListaRegistros(registros = estado.registros)
                    }
                }
            }
        }
    }
}

/** Chips Todas / Altas / Bajas / Escaneos. `null` = Todas. */
@Composable
private fun FiltroTipoAccion(
    seleccionado: TipoAccionBitacora?,
    onSeleccionar: (TipoAccionBitacora?) -> Unit
) {
    val opciones = listOf(
        null to "Todas",
        TipoAccionBitacora.ALTA to "Altas",
        TipoAccionBitacora.BAJA to "Bajas",
        TipoAccionBitacora.ESCANEO to "Escaneos"
    )

    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        opciones.forEach { (tipo, etiqueta) ->
            FilterChip(
                selected = seleccionado == tipo,
                onClick = { onSeleccionar(tipo) },
                label = { Text(etiqueta) }
            )
        }
    }
}

@Composable
private fun ListaRegistros(registros: List<RegistroBitacora>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(registros, key = { it.id }) { registro ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = registro.tipo.etiqueta(),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )
                        registro.permitido?.let { permitido ->
                            Text(
                                text = if (permitido) "Permitido" else "Denegado",
                                style = MaterialTheme.typography.labelLarge,
                                color = if (permitido) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.error
                                }
                            )
                        }
                    }
                    Text(
                        text = registro.descripcion,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "${registro.autor} · ${formatearFechaHora(registro.fechaHora)}",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

internal fun TipoAccionBitacora.etiqueta(): String = when (this) {
    TipoAccionBitacora.ALTA -> "Alta de persona"
    TipoAccionBitacora.BAJA -> "Baja de persona"
    TipoAccionBitacora.ESCANEO -> "Escaneo en caseta"
}
