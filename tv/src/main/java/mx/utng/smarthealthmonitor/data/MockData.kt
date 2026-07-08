package mx.utng.smarthealthmonitor.data

import mx.utng.smarthealthmonitor.data.db.LecturaFC

object MockData {
    val historialFC = listOf(
        LecturaFC(id = 2, valorBpm = 72, timestamp = 0L, hora = "10:00 AM", esNormal = true),
        LecturaFC(id = 3, valorBpm = 95, timestamp = 0L, hora = "11:00 AM", esNormal = true),
        LecturaFC(id = 4, valorBpm = 110, timestamp = 0L, hora = "12:00 PM", esNormal = false),
        LecturaFC(id = 5, valorBpm = 68, timestamp = 0L, hora = "01:00 PM", esNormal = true)
    )
}
