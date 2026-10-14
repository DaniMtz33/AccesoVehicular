package com.example.accesovehicular.ui.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatearFechaHora(epochMillis: Long): String {
    val formato = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return formato.format(Date(epochMillis))
}

fun formatearHora(epochMillis: Long): String {
    val formato = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return formato.format(Date(epochMillis))
}
