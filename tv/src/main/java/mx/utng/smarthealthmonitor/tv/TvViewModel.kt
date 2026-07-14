package mx.utng.smarthealthmonitor.tv
 
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.data.db.LecturaFC
import mx.utng.smarthealthmonitor.tv.mqtt.MqttTvSubscriber
import mx.utng.smarthealthmonitor.mqtt.TvMessage
 
// Estado de UI unificado para Compose TV
data class TvState(
    val fc: Int = 72,
    val pasos: Int = 72,
    val lecturas: List<LecturaFC> = emptyList(),
    val fcActual: Int = 72,
    val fcEstado: String = "Normal",
    val ultimaHora: String = "--:--:--",
    val isLoading: Boolean = false
)
 
// Extensiones de LecturaFC para compatibilidad con la interfaz
val LecturaFC.bpm: Int get() = valorBpm
val LecturaFC.estado: String get() = if (esNormal) "Frecuencia normal" else "Frecuencia inusual"
 
class TvViewModel(
    private val repository : SmartHealthRepository,
    private val context    : Context
) : ViewModel() {
 
    private val _state = MutableStateFlow(TvState())
    val state: StateFlow<TvState> = _state.asStateFlow()
 
    // Flow de mensajes MQTT entrantes
    private val mqttFlow = MutableStateFlow<TvMessage?>(null)
    private val mqttSubscriber = MqttTvSubscriber(context, mqttFlow)
 
    init {
        mqttSubscriber.connect()
 
        // Observar mensajes MQTT e historial de Room, y actualizar el estado
        viewModelScope.launch {
            combine(
                mqttFlow,
                SmartHealthRepository.fcFlow,
                SmartHealthRepository.pasosFlow,
                SmartHealthRepository.obtenerHistorial()
            ) { tvMsg, currentFc, currentPasos, list ->
                val bpmVal = tvMsg?.bpm ?: (if (currentFc > 0) currentFc else 72)
                val estadoVal = tvMsg?.estado ?: (if (currentFc > 0) (if (currentFc in 60..100) "Normal" else "Inusual") else "Normal")
                val horaVal = tvMsg?.hora ?: "--:--:--"
                
                TvState(
                    fc = bpmVal,
                    pasos = if (currentPasos > 0) currentPasos else 72,
                    lecturas = list,
                    fcActual = bpmVal,
                    fcEstado = estadoVal,
                    ultimaHora = horaVal,
                    isLoading = false
                )
            }.collect { newState ->
                _state.value = newState
            }
        }
    }
 
    override fun onCleared() {
        super.onCleared()
        mqttSubscriber.disconnect()
    }
}
