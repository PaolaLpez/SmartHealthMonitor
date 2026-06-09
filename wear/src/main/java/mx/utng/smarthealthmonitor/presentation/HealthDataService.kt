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

    private val serviceJob = Job()
    private val scope = CoroutineScope(Dispatchers.IO + serviceJob)

    private lateinit var wearDataSender: WearDataSender

    override fun onCreate() {
        super.onCreate()
        wearDataSender = WearDataSender(this)
        Log.d("HealthDataService", "Servicio creado")
    }

    override fun onNewDataPointsReceived(
        dataPoints: DataPointContainer
    ) {
        // Obtener frecuencia cardíaca
        val heartRateData = dataPoints.getData(DataType.HEART_RATE_BPM)
        for (dataPoint in heartRateData) {
            val bpm = dataPoint.value.toInt()
            Log.d("HealthDataService", "FC detectada: $bpm")
            scope.launch {
                wearDataSender.enviarFC(bpm)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel() // ✅ CORREGIDO
        Log.d("HealthDataService", "Servicio destruido")
    }

    companion object {
        suspend fun registrar(context: Context) {
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
                ).await()

                Log.d("HealthDataService", "Registro exitoso")
            } catch (e: Exception) {
                Log.e("HealthDataService", "Error: ${e.message}", e)
                throw e
            }
        }
    }
}