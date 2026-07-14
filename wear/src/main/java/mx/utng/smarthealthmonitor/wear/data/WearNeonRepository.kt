package mx.utng.smarthealthmonitor.wear.data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mx.utng.smarthealthmonitor.data.remote.NeonClient
import mx.utng.smarthealthmonitor.data.remote.NeonRequest
import mx.utng.smarthealthmonitor.data.remote.LecturaFcDto
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
 
class WearNeonRepository {
 
    /** El reloj solo PUBLICA lecturas — sin Room local */
    suspend fun publicarLectura(bpm: Int, estado: String) =
        withContext(Dispatchers.IO) {
            val hora = SimpleDateFormat("HH:mm:ss",Locale.getDefault()).format(Date())
            
            // 1. Insertar lectura
            NeonClient.api.executeQuery(
                auth    = NeonClient.AUTH_HEADER,
                connStr = NeonClient.CONN_STRING,
                request = NeonRequest(
                    query  = "INSERT INTO lecturas_fc (bpm,estado,dispositivo,hora) VALUES ($1,$2,$3,$4)",
                    params = listOf(bpm, estado, "wear", hora)
                )
            )

            // 2. Registrar/actualizar dispositivo
            NeonClient.api.executeQuery(
                auth    = NeonClient.AUTH_HEADER,
                connStr = NeonClient.CONN_STRING,
                request = NeonRequest(
                    query  = """
                        INSERT INTO dispositivos (tipo, ultimo_sync, activo)
                        VALUES ($1, NOW(), true)
                        ON CONFLICT (tipo)
                        DO UPDATE SET ultimo_sync = EXCLUDED.ultimo_sync, activo = EXCLUDED.activo
                    """.trimIndent(),
                    params = listOf("wear")
                )
            )

            // 3. Generar alerta si el estado no es normal o el bpm es anormal
            val esNormal = estado.equals("Normal", ignoreCase = true)
            if (!esNormal || bpm < 60 || bpm > 100) {
                val tipoAlerta = if (bpm > 100) "FC Alta" else "FC Baja"
                val mensajeAlerta = "Frecuencia inusual de $bpm bpm detectada en dispositivo wear"
                NeonClient.api.executeQuery(
                    auth    = NeonClient.AUTH_HEADER,
                    connStr = NeonClient.CONN_STRING,
                    request = NeonRequest(
                        query  = """
                            INSERT INTO alertas (tipo, bpm, mensaje, atendida, created_at)
                            VALUES ($1, $2, $3, false, NOW())
                        """.trimIndent(),
                        params = listOf(tipoAlerta, bpm, mensajeAlerta)
                    )
                )
            }
            android.util.Log.d("WEAR_DB","⌚ FC enviada a Neon: ${bpm} bpm")
        }
 
    /** Obtener las últimas 5 lecturas del reloj desde Neon */
    suspend fun obtenerUltimasLecturas(): List<LecturaFcDto> =
        withContext(Dispatchers.IO) {
            NeonClient.api.executeQuery(
                auth    = NeonClient.AUTH_HEADER,
                connStr = NeonClient.CONN_STRING,
                request = NeonRequest(
                    query  = "SELECT id,bpm,estado,dispositivo,hora FROM lecturas_fc WHERE dispositivo='wear' ORDER BY created_at DESC LIMIT 5",
                )
            ).rows
        }
}
