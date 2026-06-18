package mx.utng.smarthealthmonitor.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object WearDataHolder {

    private val _historial = MutableStateFlow<List<String>>(emptyList())
    val historial: StateFlow<List<String>> = _historial

    private val _fc = MutableStateFlow(0)
    val fc: StateFlow<Int> = _fc

    fun actualizarHistorial(lista: List<String>) {
        _historial.value = lista
    }

    fun actualizarFC(valor: Int) {
        _fc.value = valor
    }
}