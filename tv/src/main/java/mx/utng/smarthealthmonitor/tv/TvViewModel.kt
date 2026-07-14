package mx.utng.smarthealthmonitor.tv
 
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.data.db.LecturaFC
import mx.utng.smarthealthmonitor.tv.mqtt.MqttTvSubscriber
import mx.utng.smarthealthmonitor.mqtt.TvMessage
import mx.utng.smarthealthmonitor.tv.data.TvNeonRepository
import mx.utng.smarthealthmonitor.data.remote.LecturaFcDto
 
// Estado de UI unificado para Compose TV
data class TvState(
    val fc: Int = 72,
    val pasos: Int = 72,
    val lecturas: List<LecturaFC> = emptyList(),
    val estadisticas: List<LecturaFC> = emptyList(),
    val fcActual: Int = 72,
    val fcEstado: String = "Normal",
    val ultimaHora: String = "--:--:--",
    val isLoading: Boolean = false,
    val error: String? = null
)
 
// Extensiones de LecturaFC para compatibilidad con la interfaz
val LecturaFC.bpm: Int get() = valorBpm
val LecturaFC.estado: String get() = if (esNormal) "Frecuencia normal" else "Frecuencia inusual"

// Extension para mapear DTO de Neon a la entidad local LecturaFC
fun LecturaFcDto.toLecturaFC(): LecturaFC {
    return LecturaFC(
        id = this.id,
        valorBpm = this.bpm,
        timestamp = System.currentTimeMillis(),
        hora = this.hora,
        esNormal = this.estado.equals("Normal", ignoreCase = true)
    )
}
 
class TvViewModel(
    private val context: Context
) : ViewModel() {
 
    private val neonRepo = TvNeonRepository()
    private val _state = MutableStateFlow(TvState())
    val state: StateFlow<TvState> = _state.asStateFlow()
 
    // Flow de mensajes MQTT entrantes
    private val mqttFlow = MutableStateFlow<TvMessage?>(null)
    private val mqttSubscriber = MqttTvSubscriber(context, mqttFlow)
 
    init {
        mqttSubscriber.connect()
        cargarDatos(mostrarSpinner = true)
 
        // Observar mensajes MQTT en tiempo real
        viewModelScope.launch {
            mqttFlow.collect { tvMsg ->
                tvMsg ?: return@collect
                _state.update { it.copy(
                    fc = tvMsg.bpm,
                    fcActual = tvMsg.bpm,
                    fcEstado = tvMsg.estado,
                    ultimaHora = tvMsg.hora,
                    isLoading = false
                )}
            }
        }

        // Refrescar datos desde Neon periódicamente cada 5 segundos
        viewModelScope.launch {
            while (true) {
                kotlinx.coroutines.delay(5000)
                cargarDatos(mostrarSpinner = false)
            }
        }
    }

    fun cargarDatos(mostrarSpinner: Boolean = true) {
        viewModelScope.launch {
            if (mostrarSpinner) {
                _state.update { it.copy(isLoading = true) }
            }
            try {
                val lecturas = neonRepo.obtenerHistorialCompleto(50)
                val stats = neonRepo.obtenerEstadisticas()
                _state.update { it.copy(
                    lecturas = lecturas.map { dto -> dto.toLecturaFC() },
                    estadisticas = stats.map { dto -> dto.toLecturaFC() },
                    isLoading = false
                )}
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }
 
    fun refresh() = cargarDatos(mostrarSpinner = true)
 
    override fun onCleared() {
        super.onCleared()
        mqttSubscriber.disconnect()
    }
}
