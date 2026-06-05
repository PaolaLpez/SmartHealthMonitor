package mx.utng.smarthealthmonitor.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import mx.utng.smarthealthmonitor.data.db.LecturaFC
import mx.utng.smarthealthmonitor.data.db.LecturaFCDao
import mx.utng.smarthealthmonitor.data.db.SmartHealthDB
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Repositorio singleton que centraliza los datos de salud.
 * El WearListenerService escribe aquí.
 * El ViewModel lee de aquí.
 */
object SmartHealthRepository {

    // FC actual del wearable (bpm)
    private val _fcFlow = MutableStateFlow(0)
    val fcFlow: StateFlow<Int> = _fcFlow.asStateFlow()

    // Pasos del día actual
    private val _pasosFlow = MutableStateFlow(0)
    val pasosFlow: StateFlow<Int> = _pasosFlow.asStateFlow()

    private var dao: LecturaFCDao? = null

    fun init(context: Context) {
        dao = SmartHealthDB.getDatabase(context).lecturaDao()
    }

    suspend fun actualizarFC(bpm: Int) {

        _fcFlow.value = bpm

        val ahora = System.currentTimeMillis()

        dao?.insertar(
            LecturaFC(
                valorBpm = bpm,
                timestamp = ahora,
                hora = SimpleDateFormat(
                    "HH:mm",
                    Locale.getDefault()
                ).format(Date(ahora)),
                esNormal = bpm in 60..100
            )
        )
    }

    fun actualizarPasos(pasos: Int) {
        _pasosFlow.value = pasos
    }

    fun obtenerHistorial(): Flow<List<LecturaFC>> {
        return dao?.obtenerUltimas() ?: emptyFlow()
    }

    suspend fun limpiarHistorialAntiguo() {

        val sieteDias = 7L * 24 * 60 * 60 * 1000
        val limite = System.currentTimeMillis() - sieteDias

        dao?.limpiarViejos(limite)
    }
}