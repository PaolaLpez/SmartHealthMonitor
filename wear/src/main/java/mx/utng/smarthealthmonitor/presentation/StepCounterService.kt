package mx.utng.smarthealthmonitor.presentation

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StepCounterService : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null
    private var totalSteps = 0

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate() {
        super.onCreate()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor != null) {

            sensorManager.registerListener(
                this,
                stepSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )

            Log.d("StepCounter", "✅ Sensor activo")

        } else {

            Log.e("StepCounter", "❌ Sin sensor, simulando")

            scope.launch {

                while (true) {

                    delay(30000)

                    totalSteps += (1..3).random()

                    Log.d("StepCounter", "👟 Simulado: $totalSteps")
                }
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {

        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {

            totalSteps = event.values[0].toInt()

            Log.d("StepCounter", "👟 Pasos: $totalSteps")

            // ❌ YA NO DEPENDE DE NINGUNA CLASE EXTERNA
            // aquí después conectamos DataLayer si quieres
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()

        sensorManager.unregisterListener(this)
        job.cancel()

        Log.d("StepCounter", "Servicio detenido")
    }
}