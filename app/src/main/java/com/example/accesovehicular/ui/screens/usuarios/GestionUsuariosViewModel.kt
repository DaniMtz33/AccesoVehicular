package com.example.accesovehicular.ui.screens.usuarios

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accesovehicular.data.repository.PersonaRepositoryImpl
import com.example.accesovehicular.domain.model.Persona
import com.example.accesovehicular.domain.model.TipoUsuario
import com.example.accesovehicular.domain.repository.PersonaRepository
import com.example.accesovehicular.domain.usecase.DarDeBajaPersonaUseCase
import com.example.accesovehicular.domain.usecase.ObtenerPersonasUseCase
import com.example.accesovehicular.domain.usecase.RegistrarPersonaUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GestionUsuariosViewModel(
    repository: PersonaRepository = PersonaRepositoryImpl(),
    private val obtenerPersonas: ObtenerPersonasUseCase = ObtenerPersonasUseCase(repository),
    private val registrarPersona: RegistrarPersonaUseCase = RegistrarPersonaUseCase(repository),
    private val darDeBaja: DarDeBajaPersonaUseCase = DarDeBajaPersonaUseCase(repository)
) : ViewModel() {

    private val _uiState = MutableStateFlow<GestionUsuariosUiState>(GestionUsuariosUiState.Cargando)
    val uiState: StateFlow<GestionUsuariosUiState> = _uiState.asStateFlow()

    private val _altaUiState = MutableStateFlow<AltaPersonaUiState>(AltaPersonaUiState.Oculto)
    val altaUiState: StateFlow<AltaPersonaUiState> = _altaUiState.asStateFlow()

    private var todas: List<Persona> = emptyList()

    /** null = Todos. */
    var filtro by mutableStateOf<TipoUsuario?>(null)
        private set

    var nombreNuevo by mutableStateOf("")
        private set

    var tipoNuevo by mutableStateOf(TipoUsuario.RESIDENTE)
        private set

    var mostrarErrorNombre by mutableStateOf(false)
        private set

    var personaPorDarDeBaja by mutableStateOf<Persona?>(null)
        private set

    var bajaEnCurso by mutableStateOf(false)
        private set

    /** Aviso de un solo uso para el Snackbar. */
    var mensaje by mutableStateOf<String?>(null)
        private set

    init {
        cargarPersonas()
    }

    fun cargarPersonas() {
        viewModelScope.launch {
            _uiState.value = GestionUsuariosUiState.Cargando
            obtenerPersonas()
                .onSuccess { personas ->
                    todas = personas
                    publicarLista()
                }
                .onFailure { error -> _uiState.value = GestionUsuariosUiState.Error(error.message ?: "Error desconocido") }
        }
    }

    fun onFiltroChange(valor: TipoUsuario?) {
        filtro = valor
        if (_uiState.value is GestionUsuariosUiState.Exito) publicarLista()
    }

    // --- Alta ---

    fun onAgregarClick() {
        nombreNuevo = ""
        tipoNuevo = TipoUsuario.RESIDENTE
        mostrarErrorNombre = false
        _altaUiState.value = AltaPersonaUiState.Editando
    }

    fun onNombreNuevoChange(valor: String) {
        nombreNuevo = valor
        if (valor.isNotBlank()) mostrarErrorNombre = false
    }

    fun onTipoNuevoChange(valor: TipoUsuario) {
        tipoNuevo = valor
    }

    fun onGuardarAltaClick() {
        if (nombreNuevo.isBlank()) {
            mostrarErrorNombre = true
            return
        }

        viewModelScope.launch {
            _altaUiState.value = AltaPersonaUiState.Guardando
            registrarPersona(nombreNuevo, tipoNuevo)
                .onSuccess { persona ->
                    todas = listOf(persona) + todas
                    publicarLista()
                    _altaUiState.value = AltaPersonaUiState.Oculto
                    mensaje = "${persona.nombre} registrado"
                }
                .onFailure { error -> _altaUiState.value = AltaPersonaUiState.Error(error.message ?: "Error desconocido") }
        }
    }

    fun onCancelarAlta() {
        if (_altaUiState.value is AltaPersonaUiState.Guardando) return
        _altaUiState.value = AltaPersonaUiState.Oculto
    }

    // --- Baja ---

    fun onDarDeBajaClick(persona: Persona) {
        personaPorDarDeBaja = persona
    }

    fun onCancelarBaja() {
        if (bajaEnCurso) return
        personaPorDarDeBaja = null
    }

    fun onConfirmarBaja() {
        val persona = personaPorDarDeBaja ?: return

        viewModelScope.launch {
            bajaEnCurso = true
            darDeBaja(persona.id)
                .onSuccess { actualizada ->
                    todas = todas.map { if (it.id == actualizada.id) actualizada else it }
                    publicarLista()
                    mensaje = "${actualizada.nombre} dado de baja"
                }
                .onFailure { error -> mensaje = error.message ?: "No se pudo dar de baja" }
            bajaEnCurso = false
            personaPorDarDeBaja = null
        }
    }

    fun onMensajeMostrado() {
        mensaje = null
    }

    private fun publicarLista() {
        val visibles = filtro?.let { tipo -> todas.filter { it.tipo == tipo } } ?: todas
        _uiState.value = GestionUsuariosUiState.Exito(visibles)
    }
}
