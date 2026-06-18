package mx.utng.smarthealthmonitor.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.data.WearSyncManager
import mx.utng.smarthealthmonitor.data.db.LecturaFC
import mx.utng.smarthealthmonitor.data.models.MockData
import mx.utng.smarthealthmonitor.SmartHealthAppHolder

class DashboardViewModel : ViewModel() {

    val fc: StateFlow<Int> = SmartHealthRepository.fcFlow
        .map { valor ->
            if (valor == 0) MockData.fcActual else valor
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MockData.fcActual
        )

    val pasos: StateFlow<Int> = SmartHealthRepository.pasosFlow
        .map { valor ->
            if (valor == 0) MockData.pasosActual else valor
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MockData.pasosActual
        )

    // Historial desde Room
    val historial: StateFlow<List<LecturaFC>> =
        SmartHealthRepository.obtenerHistorial()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    // 🔥 SINCRONIZACIÓN AUTOMÁTICA CON WEAR
    init {
        viewModelScope.launch(Dispatchers.IO) {

            SmartHealthRepository.obtenerHistorial()
                .collect { lista ->

                    // evita spam si está vacío
                    if (lista.isNotEmpty()) {

                        WearSyncManager.enviarHistorial(
                            context = SmartHealthAppHolder.context,
                            lista = lista
                        )
                    }
                }
        }
    }
}