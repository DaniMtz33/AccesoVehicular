package com.example.accesovehicular.ui.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Vehiculos : Screen("vehiculos")
}
