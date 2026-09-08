package com.example.accesovehicular.ui.screens.registro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.accesovehicular.domain.model.TipoUsuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroVehiculoScreen(
    modifier: Modifier = Modifier,
    viewModel: RegistroVehiculoViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var menuExpandido by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = viewModel.placa,
            onValueChange = viewModel::onPlacaChange,
            label = { Text("Placa") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.marca,
            onValueChange = viewModel::onMarcaChange,
            label = { Text("Marca") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.modelo,
            onValueChange = viewModel::onModeloChange,
            label = { Text("Modelo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.color,
            onValueChange = viewModel::onColorChange,
            label = { Text("Color") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(
            expanded = menuExpandido,
            onExpandedChange = { menuExpandido = it }
        ) {
            OutlinedTextField(
                value = viewModel.tipoUsuario.name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipo de usuario") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpandido) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
            )
            ExposedDropdownMenu(
                expanded = menuExpandido,
                onDismissRequest = { menuExpandido = false }
            ) {
                TipoUsuario.entries.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion.name) },
                        onClick = {
                            viewModel.onTipoUsuarioChange(opcion)
                            menuExpandido = false
                        }
                    )
                }
            }
        }

        when (val estado = uiState) {
            is RegistroVehiculoUiState.Exito -> {
                Text(
                    text = "Vehículo registrado correctamente",
                    color = MaterialTheme.colorScheme.primary
                )
                Button(
                    onClick = viewModel::onRegistrarOtroClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Registrar otro")
                }
            }
            else -> {
                Button(
                    onClick = viewModel::onRegistrarClick,
                    enabled = viewModel.formularioValido && uiState !is RegistroVehiculoUiState.Cargando,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Registrar")
                }

                when (estado) {
                    is RegistroVehiculoUiState.Cargando -> CircularProgressIndicator()
                    is RegistroVehiculoUiState.Error -> Text(
                        text = estado.mensaje,
                        color = MaterialTheme.colorScheme.error
                    )
                    else -> Unit
                }
            }
        }
    }
}
