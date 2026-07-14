package mx.utng.smarthealthmonitor.wear.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import mx.utng.smarthealthmonitor.wear.mqtt.MqttWearPublisher
import mx.utng.smarthealthmonitor.wear.data.WearNeonRepository

data class WearUiState(
    val fcActual: Int = 72,
    val pasos: Int = 72
)

class WearViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(WearUiState())
    val state: StateFlow<WearUiState> = _state

    private val mqttPublisher = MqttWearPublisher(application)
    val heartRateSource = HeartRateSource()
    private val neonRepo = WearNeonRepository()

    init {
        mqttPublisher.connect()
        viewModelScope.launch {
            heartRateSource.heartRate.collect { bpm ->
                _state.update { it.copy(fcActual = bpm) }
                // Actualizar el holder global para que las pantallas reactivas existentes lo muestren
                mx.utng.smarthealthmonitor.presentation.WearDataHolder.actualizarFC(bpm)
                // Publicar FC vía MQTT cada vez que cambia
                val estado = when { bpm < 60 -> "FC Baja"; bpm > 100 -> "FC Alta"; else -> "Normal" }
                mqttPublisher.publishFC(bpm, estado)

                // Publicar a Neon en IO thread
                launch(Dispatchers.IO) {
                    runCatching { neonRepo.publicarLectura(bpm, estado) }
                        .onFailure { android.util.Log.w("WEAR","Sin red: ${it.message}") }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mqttPublisher.disconnect()
    }
}
