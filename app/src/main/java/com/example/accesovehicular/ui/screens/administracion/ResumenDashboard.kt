package com.example.accesovehicular.ui.screens.administracion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.accesovehicular.domain.model.ConteoPersonas
import com.example.accesovehicular.domain.model.RegistroBitacora
import com.example.accesovehicular.domain.model.ResumenAccesos
import com.example.accesovehicular.ui.common.formatearFechaHora

/** Resumen general del panel: cada bloque muestra su propio cargando / error. */
@Composable
fun ResumenDashboard(
    uiState: DashboardUiState,
    onReintentarAccesos: () -> Unit,
    onReintentarPersonas: () -> Unit,
    onReintentarAcciones: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Seccion(titulo = "Accesos de hoy", estado = uiState.accesosHoy, onReintentar = onReintentarAccesos) {
            TarjetasAccesos(it)
        }
        Seccion(titulo = "Personas", estado = uiState.personas, onReintentar = onReintentarPersonas) {
            TarjetasPersonas(it)
        }
        Seccion(titulo = "Últimas acciones", estado = uiState.ultimasAcciones, onReintentar = onReintentarAcciones) {
            UltimasAcciones(it)
        }
    }
}

@Composable
private fun <T> Seccion(
    titulo: String,
    estado: SeccionUiState<T>,
    onReintentar: () -> Unit,
    contenido: @Composable (T) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = titulo, style = MaterialTheme.typography.titleMedium)
        when (estado) {
            is SeccionUiState.Cargando -> {
                Box(
                    modifier = Modifier.fillMaxWidth().height(72.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is SeccionUiState.Error -> {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(start = 12.dp, end = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = estado.mensaje,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = onReintentar) {
                            Text("Reintentar")
                        }
                    }
                }
            }
            is SeccionUiState.Exito -> contenido(estado.datos)
        }
    }
}

@Composable
private fun TarjetasAccesos(resumen: ResumenAccesos) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        TarjetaNumero("Entradas", resumen.entradas, Modifier.weight(1f))
        TarjetaNumero("Salidas", resumen.salidas, Modifier.weight(1f))
        TarjetaNumero(
            "Incidencias",
            resumen.incidencias,
            Modifier.weight(1f),
            color = if (resumen.incidencias > 0) MaterialTheme.colorScheme.error else Color.Unspecified
        )
    }
}

@Composable
private fun TarjetasPersonas(conteo: ConteoPersonas) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        TarjetaNumero("Activas", conteo.activas, Modifier.weight(1f))
        TarjetaNumero("Inactivas", conteo.inactivas, Modifier.weight(1f))
    }
}

@Composable
private fun TarjetaNumero(
    etiqueta: String,
    valor: Int,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified
) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = valor.toString(), style = MaterialTheme.typography.headlineMedium, color = color)
            Text(text = etiqueta, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun UltimasAcciones(registros: List<RegistroBitacora>) {
    if (registros.isEmpty()) {
        Text(text = "Aún no hay acciones registradas.", style = MaterialTheme.typography.bodyMedium)
        return
    }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        registros.forEach { registro ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = registro.tipo.etiqueta(), style = MaterialTheme.typography.labelLarge)
                    Text(text = registro.descripcion, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "${registro.autor} · ${formatearFechaHora(registro.fechaHora)}",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}
