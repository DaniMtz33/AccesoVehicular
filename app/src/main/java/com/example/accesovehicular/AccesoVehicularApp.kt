package com.example.accesovehicular

import android.app.Application

class AccesoVehicularApp : Application() {

    companion object {
        lateinit var instance: AccesoVehicularApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
