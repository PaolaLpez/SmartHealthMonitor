package mx.utng.smarthealthmonitor

import android.app.Application
import mx.utng.smarthealthmonitor.data.SmartHealthRepository

class SmartHealthApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // 1. inicializa Room
        SmartHealthRepository.init(this)

        // 2. guarda contexto global (PARA WEAR SYNC)
        SmartHealthAppHolder.context = this
    }
}