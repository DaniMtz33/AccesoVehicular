package com.example.accesovehicular.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.accesovehicular.ui.screens.historial.HistorialScreen
import com.example.accesovehicular.ui.screens.home.HomeScreen
import com.example.accesovehicular.ui.screens.login.LoginScreen
import com.example.accesovehicular.ui.screens.registro.RegistroVehiculoScreen
import com.example.accesovehicular.ui.screens.scanner.ScannerScreen
import com.example.accesovehicular.ui.screens.splash.SplashScreen

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onSesionActiva = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onSinSesion = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginExitoso = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onCerrarSesion = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onEscanearQr = {
                    navController.navigate(Screen.Scanner.route)
                },
                onRegistrarVehiculo = {
                    navController.navigate(Screen.RegistroVehiculo.route)
                },
                onVerHistorial = {
                    navController.navigate(Screen.HistorialAccesos.route)
                }
            )
        }
        composable(Screen.Scanner.route) {
            ScannerScreen()
        }
        composable(Screen.RegistroVehiculo.route) {
            RegistroVehiculoScreen()
        }
        composable(Screen.HistorialAccesos.route) {
            HistorialScreen()
        }
    }
}
