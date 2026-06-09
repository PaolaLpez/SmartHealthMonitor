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

        Log.d(TAG, "📥 Mensaje recibido: path=$path, data=$data")

        when (path) {
            "/heart_rate" -> {
                val bpm = data.toIntOrNull() ?: return
                Log.d(TAG, "❤️ FC recibida: $bpm")
                serviceScope.launch {
                    SmartHealthRepository.actualizarFC(bpm)
                    Log.d(TAG, "✅ FC guardada: $bpm")
                }
            }
            "/steps" -> {
                val pasos = data.toIntOrNull() ?: return
                Log.d(TAG, "👟 Pasos recibidos: $pasos")
                serviceScope.launch {
                    SmartHealthRepository.actualizarPasos(pasos)
                    Log.d(TAG, "✅ Pasos guardados: $pasos")
                }
            }
        }
    }
}