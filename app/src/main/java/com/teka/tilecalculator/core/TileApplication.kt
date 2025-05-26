package com.teka.tilecalculator.core

import android.app.Application
import com.teka.tilecalculator.data.AppContainer
import com.teka.tilecalculator.data.AppDataContainer

class TileApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
