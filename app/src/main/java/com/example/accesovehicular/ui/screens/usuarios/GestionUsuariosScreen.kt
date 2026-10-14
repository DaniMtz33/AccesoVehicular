package com.example.accesovehicular.ui.screens.usuarios

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.accesovehicular.domain.model.Persona
import com.example.accesovehicular.domain.model.TipoUsuario
import com.example.accesovehicular.ui.common.FiltroTipoUsuario
import com.example.accesovehicular.ui.common.etiqueta

@Composable
fun GestionUsuariosScreen(
    modifier: Modifier = Modifier,
    viewModel: GestionUsuariosViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val altaUiState by viewModel.altaUiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.mensaje) {
        viewModel.mensaje?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onMensajeMostrado()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Gestión de usuarios",
                style = MaterialTheme.typography.titleLarge
            )

            Button(
                onClick = viewModel::onAgregarClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar persona")
            }

            FiltroTipoUsuario(
                seleccionado = viewModel.filtro,
                onSeleccionar = viewModel::onFiltroChange
            )

            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                when (val estado = uiState) {
                    is GestionUsuariosUiState.Cargando -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    is GestionUsuariosUiState.Error -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(text = "No se pudo cargar la lista: ${estado.mensaje}")
                            Button(onClick = viewModel::cargarPersonas) {
                                Text("Reintentar")
                            }
                        }
                    }
                    is GestionUsuariosUiState.Exito -> {
                        if (estado.personas.isEmpty()) {
                            Text(
                                text = "No hay personas registradas en esta categoría.",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        } else {
                            ListaPersonas(
                                personas = estado.personas,
                                onDarDeBaja = viewModel::onDarDeBajaClick
                            )
                        }
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    if (altaUiState !is AltaPersonaUiState.Oculto) {
        DialogoAlta(
            estado = altaUiState,
            nombre = viewModel.nombreNuevo,
            tipo = viewModel.tipoNuevo,
            mostrarErrorNombre = viewModel.mostrarErrorNombre,
            onNombreChange = viewModel::onNombreNuevoChange,
            onTipoChange = viewModel::onTipoNuevoChange,
            onGuardar = viewModel::onGuardarAltaClick,
            onCancelar = viewModel::onCancelarAlta
        )
    }

    viewModel.personaPorDarDeBaja?.let { persona ->
        DialogoBaja(
            persona = persona,
            enCurso = viewModel.bajaEnCurso,
            onConfirmar = viewModel::onConfirmarBaja,
            onCancelar = viewModel::onCancelarBaja
        )
    }
}

@Composable
private fun ListaPersonas(
    personas: List<Persona>,
    onDarDeBaja: (Persona) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(personas, key = { it.id }) { persona ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = persona.nombre, style = MaterialTheme.typography.titleMedium)
                        Text(text = persona.tipo.etiqueta(), style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = if (persona.activo) "Activo" else "Inactivo",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (persona.activo) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.error
                            }
                        )
                    }
                    if (persona.activo) {
                        OutlinedButton(onClick = { onDarDeBaja(persona) }) {
                            Text("Dar de baja")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogoAlta(
    estado: AltaPersonaUiState,
    nombre: String,
    tipo: TipoUsuario,
    mostrarErrorNombre: Boolean,
    onNombreChange: (String) -> Unit,
    onTipoChange: (TipoUsuario) -> Unit,
    onGuardar: () -> Unit,
    onCancelar: () -> Unit
) {
    val guardando = estado is AltaPersonaUiState.Guardando
    var menuExpandido by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Agregar persona") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = onNombreChange,
                    label = { Text("Nombre") },
                    singleLine = true,
                    enabled = !guardando,
                    isError = mostrarErrorNombre,
                    supportingText = if (mostrarErrorNombre) {
                        { Text("El nombre es obligatorio") }
                    } else {
                        null
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = menuExpandido,
                    onExpandedChange = { if (!guardando) menuExpandido = it }
                ) {
                    OutlinedTextField(
                        value = tipo.etiqueta(),
                        onValueChange = {},
                        readOnly = true,
                        enabled = !guardando,
                        label = { Text("Tipo") },
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
                                text = { Text(opcion.etiqueta()) },
                                onClick = {
                                    onTipoChange(opcion)
                                    menuExpandido = false
                                }
                            )
                        }
                    }
                }

                when (estado) {
                    is AltaPersonaUiState.Guardando -> CircularProgressIndicator()
                    is AltaPersonaUiState.Error -> Text(
                        text = estado.mensaje,
                        color = MaterialTheme.colorScheme.error
                    )
                    else -> Unit
                }
            }
        },
        confirmButton = {
            Button(onClick = onGuardar, enabled = !guardando) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar, enabled = !guardando) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun DialogoBaja(
    persona: Persona,
    enCurso: Boolean,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Dar de baja") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("¿Dar de baja a ${persona.nombre}? Quedará como inactivo.")
                if (enCurso) CircularProgressIndicator()
            }
        },
        confirmButton = {
            Button(onClick = onConfirmar, enabled = !enCurso) {
                Text("Dar de baja")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar, enabled = !enCurso) {
                Text("Cancelar")
            }
        }
    )
}
