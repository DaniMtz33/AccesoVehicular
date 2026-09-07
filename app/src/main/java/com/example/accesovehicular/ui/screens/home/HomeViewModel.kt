package com.example.accesovehicular.ui.screens.home

import androidx.lifecycle.ViewModel
import com.example.accesovehicular.domain.session.SesionManager

class HomeViewModel : ViewModel() {
    fun cerrarSesion() {
        SesionManager.cerrarSesion()
    }
}
