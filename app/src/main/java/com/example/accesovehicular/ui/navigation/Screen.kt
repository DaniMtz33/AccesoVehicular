package com.example.accesovehicular.ui.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Home : Screen("home")
    data object HomeAdministracion : Screen("home_administracion")
    data object Scanner : Screen("escanear_qr")
    data object EscaneoCaseta : Screen("escaneo_caseta")
    data object RegistroVehiculo : Screen("registro_vehiculo")
    data object HistorialAccesos : Screen("historial_accesos")
    data object MiQr : Screen("mi_qr")
    data object GestionUsuarios : Screen("gestion_usuarios")
    data object ReportesAccesos : Screen("reportes_accesos")
    data object Bitacora : Screen("bitacora")
}
