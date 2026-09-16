package com.example.accesovehicular.ui.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Home : Screen("home")
    data object HomeAdministracion : Screen("home_administracion")
    data object Scanner : Screen("escanear_qr")
    data object RegistroVehiculo : Screen("registro_vehiculo")
    data object HistorialAccesos : Screen("historial_accesos")
}
