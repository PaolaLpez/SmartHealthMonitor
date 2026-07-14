package mx.utng.smarthealthmonitor.presentation

import android.util.Log
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService

class HistorialWearListener : WearableListenerService() {

    override fun onCreate() {
        super.onCreate()

        Log.d("WEAR_TEST", "✅ HistorialWearListener creado")
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {

        Log.d("WEAR_TEST", "🔥 onDataChanged ejecutado")

        for (event in dataEvents) {

            val item = event.dataItem

            Log.d(
                "WEAR_TEST",
                "PATH RECIBIDO: ${item.uri.path}"
            )

            if (item.uri.path == "/historial") {

                val dataMap =
                    DataMapItem.fromDataItem(item).dataMap

                val lista =
                    dataMap.getStringArrayList("historial")
                        ?: arrayListOf()

                Log.d(
                    "WEAR_TEST",
                    "📋 HISTORIAL RECIBIDO: ${lista.size}"
                )

                WearDataHolder.actualizarHistorial(lista)
            }
        }
    }
}