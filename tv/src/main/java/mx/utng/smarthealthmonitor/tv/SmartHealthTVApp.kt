package mx.utng.smarthealthmonitor.tv

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.data.SmartHealthRepository
import kotlin.random.Random

class SmartHealthTVApp : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        SmartHealthRepository.init(this)

        // Iniciar simulación realista de lecturas en segundo plano
        iniciarSimulacion()
    }

    private fun iniciarSimulacion() {
        applicationScope.launch {
            // Espera inicial para la carga del fragmento
            delay(3000)
            while (true) {
                // Simular una nueva lectura del sensor cada 7 segundos
                delay(7000)
                val bpm = Random.nextInt(60, 115) // Variación realista
                SmartHealthRepository.actualizarFC(bpm)
            }
        }
    }
}
