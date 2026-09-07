package com.example.accesovehicular.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.accesovehicular.AccesoVehicularApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sesion")

private val TOKEN_KEY = stringPreferencesKey("token")

object SesionLocalDataSource {

    private val dataStore: DataStore<Preferences>
        get() = AccesoVehicularApp.instance.dataStore

    val tokenFlow: Flow<String?> = dataStore.data.map { it[TOKEN_KEY] }

    suspend fun guardarToken(token: String) {
        dataStore.edit { it[TOKEN_KEY] = token }
    }

    suspend fun borrarToken() {
        dataStore.edit { it.remove(TOKEN_KEY) }
    }
}
