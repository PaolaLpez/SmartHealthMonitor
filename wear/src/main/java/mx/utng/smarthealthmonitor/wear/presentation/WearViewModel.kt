package mx.utng.smarthealthmonitor.wear.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.wear.mqtt.MqttWearPublisher

data class WearUiState(
    val fcActual: Int = 72,
    val pasos: Int = 72
)

class WearViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(WearUiState())
    val state: StateFlow<WearUiState> = _state

    private val mqttPublisher = MqttWearPublisher(application)
    val heartRateSource = HeartRateSource()

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
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mqttPublisher.disconnect()
    }
}
