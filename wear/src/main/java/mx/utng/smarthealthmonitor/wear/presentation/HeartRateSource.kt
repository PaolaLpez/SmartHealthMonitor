package mx.utng.smarthealthmonitor.wear.presentation

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class HeartRateSource {
    val heartRate: Flow<Int> = flow {
        while (true) {
            val bpm = Random.nextInt(60, 110)
            emit(bpm)
            delay(5000) // Emit every 5 seconds
        }
    }
}
