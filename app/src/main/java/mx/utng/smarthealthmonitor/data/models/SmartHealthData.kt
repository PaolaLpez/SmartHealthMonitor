package mx.utng.smarthealthmonitor.data.models

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class LecturaFC(
    val id: Int,
    val valorBpm: Int,
    val hora: String,
    val esNormal: Boolean = valorBpm in 60..100
)

// Datos de prueba para desarrollo (mock data)
object MockData {

    // Generar horas para las últimas 24 horas
    private fun generarHoras(): List<String> {
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val horas = mutableListOf<String>()

        for (i in 0..23) {
            calendar.set(Calendar.HOUR_OF_DAY, i)
            calendar.set(Calendar.MINUTE, 0)
            horas.add(format.format(calendar.time))
        }
        return horas.reversed() // De más reciente a más antigua
    }

    val historialFC = listOf(
        LecturaFC(1, 72, "12:00", true),
        LecturaFC(2, 68, "11:00", true),
        LecturaFC(3, 75, "10:00", true),
        LecturaFC(4, 82, "09:00", true),
        LecturaFC(5, 88, "08:00", true),
        LecturaFC(6, 92, "07:00", true),
        LecturaFC(7, 85, "06:00", true),
        LecturaFC(8, 78, "05:00", true),
        LecturaFC(9, 70, "04:00", true),
        LecturaFC(10, 65, "03:00", true),
        LecturaFC(11, 62, "02:00", true),
        LecturaFC(12, 60, "01:00", true),
        LecturaFC(13, 58, "00:00", false),  // Baja (en reposo)
        LecturaFC(14, 120, "23:00", false), // Alta (ejercicio)
        LecturaFC(15, 105, "22:00", false), // Alta
        LecturaFC(16, 95, "21:00", false),  // Alta
        LecturaFC(17, 88, "20:00", true),
        LecturaFC(18, 85, "19:00", true),
        LecturaFC(19, 82, "18:00", true),
        LecturaFC(20, 79, "17:00", true),
        LecturaFC(21, 76, "16:00", true),
        LecturaFC(22, 74, "15:00", true),
        LecturaFC(23, 73, "14:00", true),
        LecturaFC(24, 71, "13:00", true)
    )

    // Datos actuales (simulados)
    var fcActual = 72
    var pasosActual = 5432

    // Datos adicionales para mostrar más información
    var ritmoCardiacoMin = 58
    var ritmoCardiacoMax = 120
    var ritmoCardiacoPromedio = 78

    var caloriasQuemadas = 1850
    var distanciaKm = 4.2
    var minutosActivos = 45

    // Función para simular cambio de datos cada cierto tiempo
    fun simularNuevosDatos() {
        // Cambiar FC entre 60-100
        fcActual = (60..100).random()

        // Incrementar pasos
        pasosActual += (50..200).random()

        // Actualizar promedios
        if (fcActual < ritmoCardiacoMin) ritmoCardiacoMin = fcActual
        if (fcActual > ritmoCardiacoMax) ritmoCardiacoMax = fcActual

        // Recalcular promedio
        ritmoCardiacoPromedio = (ritmoCardiacoMin + ritmoCardiacoMax) / 2
    }

    // Obtener resumen del día
    fun obtenerResumen(): String {
        return """
            Resumen del día:
            ❤️ FC: $fcActual bpm (Normal: 60-100)
            👟 Pasos: $pasosActual
            🔥 Calorías: $caloriasQuemadas
            📍 Distancia: ${distanciaKm} km
            ⏱️ Activo: $minutosActivos min
            📊 Rango FC: $ritmoCardiacoMin - $ritmoCardiacoMax bpm
            📈 Promedio FC: $ritmoCardiacoPromedio bpm
        """.trimIndent()
    }
}

// Extensión para obtener datos aleatorios
fun getRandomHeartRate(): Int = (60..100).random()
fun getRandomSteps(): Int = (3000..10000).random()