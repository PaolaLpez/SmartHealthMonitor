package mx.utng.smarthealthmonitor.presentation

import android.content.Context
import android.util.Log
import androidx.health.services.client.HealthServices
import androidx.health.services.client.PassiveListenerService
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.PassiveListenerConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch

class HealthDataService : PassiveListenerService() {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {

        val heartRateData = dataPoints.getData(DataType.HEART_RATE_BPM)

        for (dataPoint in heartRateData) {

            val bpm = dataPoint.value.toInt()

            Log.d("HealthService", "FC detectada: $bpm")

            scope.launch {
                // aquí puedes mandar a DataLayer si quieres
                Log.d("HealthService", "Procesado BPM: $bpm")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    companion object {

        // ✅ ESTA ES LA FUNCIÓN QUE TE FALTABA
        fun registrar(context: Context) {

            try {

                val healthClient = HealthServices.getClient(context)
                val passiveClient = healthClient.passiveMonitoringClient

                val config = PassiveListenerConfig.builder()
                    .setDataTypes(
                        setOf(DataType.HEART_RATE_BPM)
                    )
                    .setShouldUserActivityInfoBeRequested(true)
                    .build()

                passiveClient.setPassiveListenerServiceAsync(
                    HealthDataService::class.java,
                    config
                )

                Log.d("HealthDataService", "✅ Servicio registrado correctamente")

            } catch (e: Exception) {
                Log.e("HealthDataService", "❌ Error registrando servicio", e)
            }
        }
    }
}