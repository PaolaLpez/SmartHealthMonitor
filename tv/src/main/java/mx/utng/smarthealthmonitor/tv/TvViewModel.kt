package mx.utng.smarthealthmonitor.tv
 
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import mx.utng.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.data.db.LecturaFC
 
// Estado de UI unificado para Compose TV
data class TvState(
    val fc: Int = 72,
    val pasos: Int = 72,
    val lecturas: List<LecturaFC> = emptyList()
)

// Extensiones de LecturaFC para compatibilidad con la interfaz
val LecturaFC.bpm: Int get() = valorBpm
val LecturaFC.estado: String get() = if (esNormal) "Frecuencia normal" else "Frecuencia inusual"

class TvViewModel : ViewModel() {
 
    private val _state = MutableStateFlow(TvState())
    val state: StateFlow<TvState> = _state.asStateFlow()

    init {
        combine(
            SmartHealthRepository.fcFlow,
            SmartHealthRepository.pasosFlow,
            SmartHealthRepository.obtenerHistorial()
        ) { fc, pasos, lecturas ->
            TvState(
                fc = if (fc > 0) fc else 72,
                pasos = if (pasos > 0) pasos else 72,
                lecturas = lecturas
            )
        }.onEach { newState ->
            _state.value = newState
        }.launchIn(viewModelScope)
    }
}
