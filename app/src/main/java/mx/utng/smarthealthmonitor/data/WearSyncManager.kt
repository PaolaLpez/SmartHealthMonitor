package mx.utng.smarthealthmonitor.data

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.tasks.await
import mx.utng.smarthealthmonitor.data.db.LecturaFC

object WearSyncManager {

    suspend fun enviarHistorial(
        context: Context,
        lista: List<LecturaFC>
    ) {

        val dataClient = Wearable.getDataClient(context)

        val request = PutDataMapRequest.create("/historial")

        val datos = ArrayList<String>()

        lista.forEach {
            datos.add("${it.valorBpm}|${it.hora}|${it.esNormal}")
        }

        request.dataMap.putStringArrayList("historial", datos)

        // 🔥 FORZAR CAMBIO REAL
        request.dataMap.putLong("timestamp", System.currentTimeMillis())

        Log.d("SYNC", "ENVIANDO HISTORIAL: ${datos.size}")

        dataClient.putDataItem(
            request.asPutDataRequest().setUrgent()
        ).await()
    }
}