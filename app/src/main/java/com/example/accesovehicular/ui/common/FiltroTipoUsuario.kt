package com.example.accesovehicular.ui.common

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.accesovehicular.domain.model.TipoUsuario

/** Chips Todos / Residentes / Visitas / Proveedores. `null` = Todos. */
@Composable
fun FiltroTipoUsuario(
    seleccionado: TipoUsuario?,
    onSeleccionar: (TipoUsuario?) -> Unit,
    modifier: Modifier = Modifier
) {
    val opciones = listOf(
        null to "Todos",
        TipoUsuario.RESIDENTE to "Residentes",
        TipoUsuario.VISITA to "Visitas",
        TipoUsuario.PROVEEDOR to "Proveedores"
    )

    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
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

fun TipoUsuario.etiqueta(): String = when (this) {
    TipoUsuario.RESIDENTE -> "Residente"
    TipoUsuario.VISITA -> "Visita"
    TipoUsuario.PROVEEDOR -> "Proveedor"
}
