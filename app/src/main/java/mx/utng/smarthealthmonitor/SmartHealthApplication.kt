package mx.utng.smarthealthmonitor

import android.app.Application
import mx.utng.smarthealthmonitor.mqtt.MqttAppService
import mx.utng.smarthealthmonitor.data.SmartHealthRepository

class SmartHealthApplication : Application() {
    lateinit var mqttService: MqttAppService
 
    override fun onCreate() {
        super.onCreate()
        // Inicializar MQTT con el StateFlow del Repository
        mqttService = MqttAppService(
            context = this,
            fcFlow  = SmartHealthRepository.fcFlow
        )
        mqttService.connect()
    }
}
