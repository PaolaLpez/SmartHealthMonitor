package mx.utng.smarthealthmonitor.wear.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WearDashboardViewModel : ViewModel() {

    private val _fc = MutableStateFlow(72)

    val fc: StateFlow<Int> = _fc

    fun actualizarFC(valor: Int) {
        _fc.value = valor
    }
}