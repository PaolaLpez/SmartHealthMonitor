package mx.utng.smarthealthmonitor.presentation.data

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WearDashboardViewModel : ViewModel() {

    // Valor simulado de frecuencia cardíaca
    private val _fc = MutableStateFlow(72)

    val fc: StateFlow<Int> = _fc

    // Método opcional para pruebas
    fun actualizarFC(valor: Int) {
        _fc.value = valor
    }
}