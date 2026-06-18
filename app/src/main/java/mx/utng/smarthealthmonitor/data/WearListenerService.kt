package mx.utng.smarthealthmonitor.data

import android.util.Log
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class WearListenerService : WearableListenerService() {

    companion object {
        private const val TAG = "WearListener"
    }

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)

        val data = String(messageEvent.data)
        val path = messageEvent.path

        Log.d(TAG, "📥 Recibido: $path -> $data")

        when (path) {

            "/heart_rate" -> {
                val bpm = data.toIntOrNull() ?: return

                serviceScope.launch {
                    SmartHealthRepository.actualizarFC(bpm)
                    Log.d(TAG, "❤️ Guardado FC: $bpm")
                }
            }

            "/steps" -> {
                val pasos = data.toIntOrNull() ?: return
                SmartHealthRepository.actualizarPasos(pasos)
            }
        }
    }
}