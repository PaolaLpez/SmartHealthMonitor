package mx.utng.smarthealthmonitor.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import mx.utng.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.data.models.MockData

class DashboardViewModel : ViewModel() {

    // FC del wearable real
    // Si no hay datos aún → usar MockData
    val fc: StateFlow<Int> = SmartHealthRepository.fcFlow

        .map { valor ->

            if (valor == 0)
                MockData.fcActual
            else
                valor
        }

        .stateIn(
            scope = viewModelScope,

            started = SharingStarted.WhileSubscribed(5_000),

            initialValue = MockData.fcActual
        )

    // Pasos reales
    val pasos: StateFlow<Int> = SmartHealthRepository.pasosFlow

        .map { valor ->

            if (valor == 0)
                MockData.pasosActual
            else
                valor
        }

        .stateIn(
            scope = viewModelScope,

            started = SharingStarted.WhileSubscribed(5_000),

            initialValue = MockData.pasosActual
        )

    // Historial simulado
    val historial = MockData.historialFC
}