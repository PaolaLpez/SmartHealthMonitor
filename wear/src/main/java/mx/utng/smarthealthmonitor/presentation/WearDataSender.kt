package mx.utng.smarthealthmonitor.presentation

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.tasks.await

class WearDataSender(
    private val context: Context
) {
    companion object {
        private const val PATH_FC = "/smarthealthmonitor/fc"      // ← Coincide con celular
        private const val PATH_PASOS = "/smarthealthmonitor/pasos" // ← Coincide con celular
    }

    suspend fun enviarFC(bpm: Int) {
        try {
            val nodes = Wearable.getNodeClient(context).connectedNodes.await()
            val phoneNode = nodes.firstOrNull()

            if (phoneNode != null) {
                Wearable.getMessageClient(context)
                    .sendMessage(phoneNode.id, PATH_FC, bpm.toString().toByteArray())
                    .await()
                Log.d("WearDataSender", "❤️ FC $bpm enviada")
            } else {
                Log.d("WearDataSender", "⚠️ Sin teléfono conectado")
            }
        } catch (e: Exception) {
            Log.e("WearDataSender", "Error enviando FC: ${e.message}")
        }
    }

    suspend fun enviarPasos(pasos: Int) {
        try {
            val nodes = Wearable.getNodeClient(context).connectedNodes.await()
            val phoneNode = nodes.firstOrNull()

            if (phoneNode != null) {
                Wearable.getMessageClient(context)
                    .sendMessage(phoneNode.id, PATH_PASOS, pasos.toString().toByteArray())
                    .await()
                Log.d("WearDataSender", "👟 Pasos $pasos enviados")
            } else {
                Log.d("WearDataSender", "⚠️ Sin teléfono conectado")
            }
        } catch (e: Exception) {
            Log.e("WearDataSender", "Error enviando pasos: ${e.message}")
        }
    }
}