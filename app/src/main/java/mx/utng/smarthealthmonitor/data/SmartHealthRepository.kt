package mx.utng.smarthealthmonitor.data

import android.content.Context
import kotlinx.coroutines.flow.*
import mx.utng.smarthealthmonitor.data.db.LecturaFC
import mx.utng.smarthealthmonitor.data.db.LecturaFCDao
import mx.utng.smarthealthmonitor.data.db.SmartHealthDB
import java.text.SimpleDateFormat
import java.util.*

object SmartHealthRepository {

    private val _fcFlow = MutableStateFlow(0)
    val fcFlow: StateFlow<Int> = _fcFlow.asStateFlow()

    private val _pasosFlow = MutableStateFlow(0)
    val pasosFlow: StateFlow<Int> = _pasosFlow.asStateFlow()

    private var dao: LecturaFCDao? = null
    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
        dao = SmartHealthDB.getDatabase(context).lecturaDao()
    }

    suspend fun actualizarFC(bpm: Int) {

        _fcFlow.value = bpm

        val ahora = System.currentTimeMillis()

        val lectura = LecturaFC(
            valorBpm = bpm,
            timestamp = ahora,
            hora = SimpleDateFormat("HH:mm", Locale.getDefault())
                .format(Date(ahora)),
            esNormal = bpm in 60..100
        )

        dao?.insertar(lectura)

        // OBTENER HISTORIAL REAL DE ROOM
        val lista = dao?.obtenerUltimas()
            ?.firstOrNull()
            ?: emptyList()

        // ENVIAR AL WEAR
        appContext?.let {
            WearSyncManager.enviarHistorial(it, lista)
        }
    }

    fun actualizarPasos(pasos: Int) {
        _pasosFlow.value = pasos
    }

    fun obtenerHistorial(): Flow<List<LecturaFC>> {
        return dao?.obtenerUltimas() ?: emptyFlow()
    }
}