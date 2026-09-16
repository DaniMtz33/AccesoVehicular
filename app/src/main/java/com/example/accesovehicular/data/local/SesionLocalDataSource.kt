package com.example.accesovehicular.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.accesovehicular.AccesoVehicularApp
import com.example.accesovehicular.domain.model.Rol
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sesion")

private val TOKEN_KEY = stringPreferencesKey("token")
private val ROL_KEY = stringPreferencesKey("rol")

data class SesionGuardada(val token: String, val rol: Rol)

object SesionLocalDataSource {

    private val dataStore: DataStore<Preferences>
        get() = AccesoVehicularApp.instance.dataStore

    val sesionFlow: Flow<SesionGuardada?> = dataStore.data.map { prefs ->
        val token = prefs[TOKEN_KEY]
        val rol = prefs[ROL_KEY]?.let { nombre -> Rol.entries.find { it.name == nombre } }
        if (token != null && rol != null) SesionGuardada(token, rol) else null
    }

    suspend fun guardarSesion(token: String, rol: Rol) {
        dataStore.edit {
            it[TOKEN_KEY] = token
            it[ROL_KEY] = rol.name
        }
    }

    suspend fun borrarSesion() {
        dataStore.edit {
            it.remove(TOKEN_KEY)
            it.remove(ROL_KEY)
        }
    }
}
