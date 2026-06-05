package mx.utng.smarthealthmonitor.presentation


import android.content.Context

class WearDataSender(
    private val context: Context
) {

    suspend fun enviarFC(bpm: Int) {
        println("Frecuencia cardíaca enviada: $bpm")
    }
}