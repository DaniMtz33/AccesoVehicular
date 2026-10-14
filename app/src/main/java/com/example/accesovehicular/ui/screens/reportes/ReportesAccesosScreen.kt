package com.example.accesovehicular.ui.screens.reportes

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.accesovehicular.domain.model.AccesoReporte
import com.example.accesovehicular.domain.model.PeriodoReporte
import com.example.accesovehicular.domain.model.ResumenAccesos
import com.example.accesovehicular.domain.model.TipoAcceso
import com.example.accesovehicular.ui.common.FiltroTipoUsuario
import com.example.accesovehicular.ui.common.etiqueta
import com.example.accesovehicular.ui.common.formatearFechaHora

@Composable
fun ReportesAccesosScreen(
    modifier: Modifier = Modifier,
    viewModel: ReportesAccesosViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Reportes de accesos",
            style = MaterialTheme.typography.titleLarge
        )

        FiltroPeriodo(
            seleccionado = viewModel.periodo,
            onSeleccionar = viewModel::onPeriodoChange
        )

        FiltroTipoUsuario(
            seleccionado = viewModel.tipoPersona,
            onSeleccionar = viewModel::onTipoPersonaChange
        )

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (val estado = uiState) {
                is ReportesAccesosUiState.Cargando -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ReportesAccesosUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(text = "No se pudo cargar el reporte: ${estado.mensaje}")
                        Button(onClick = viewModel::cargarReporte) {
                            Text("Reintentar")
                        }
                    }
                }
                is ReportesAccesosUiState.Exito -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Resumen(resumen = estado.reporte.resumen)

                        if (estado.reporte.accesos.isEmpty()) {
                            Box(
                                modifier = Modifier.weight(1f).fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No hay accesos para estos filtros.")
                            }
                        } else {
                            ListaAccesos(accesos = estado.reporte.accesos)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FiltroPeriodo(
    seleccionado: PeriodoReporte,
    onSeleccionar: (PeriodoReporte) -> Unit
) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PeriodoReporte.entries.forEach { periodo ->
            FilterChip(
                selected = seleccionado == periodo,
                onClick = { onSeleccionar(periodo) },
                label = { Text(periodo.etiqueta()) }
            )
        }
    }
}

@Composable
private fun Resumen(resumen: ResumenAccesos) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TarjetaTotal(etiqueta = "Entradas", valor = resumen.entradas, modifier = Modifier.weight(1f))
        TarjetaTotal(etiqueta = "Salidas", valor = resumen.salidas, modifier = Modifier.weight(1f))
        TarjetaTotal(
            etiqueta = "Incidencias",
            valor = resumen.incidencias,
            color = if (resumen.incidencias > 0) MaterialTheme.colorScheme.error else Color.Unspecified,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TarjetaTotal(
    etiqueta: String,
    valor: Int,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = valor.toString(), style = MaterialTheme.typography.headlineMedium, color = color)
            Text(text = etiqueta, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun ListaAccesos(accesos: List<AccesoReporte>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(accesos, key = { it.acceso.id }) { reporte ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = reporte.acceso.placa,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = if (reporte.acceso.tipo == TipoAcceso.ENTRADA) "Entrada" else "Salida",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = "${reporte.persona} · ${reporte.tipoPersona.etiqueta()}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = formatearFechaHora(reporte.acceso.fechaHora),
                        style = MaterialTheme.typography.labelMedium
                    )
                    reporte.incidencia?.let { incidencia ->
                        Text(
                            text = "Incidencia: $incidencia",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

private fun PeriodoReporte.etiqueta(): String = when (this) {
    PeriodoReporte.HOY -> "Hoy"
    PeriodoReporte.SIETE_DIAS -> "Últimos 7 días"
    PeriodoReporte.MES -> "Último mes"
}
