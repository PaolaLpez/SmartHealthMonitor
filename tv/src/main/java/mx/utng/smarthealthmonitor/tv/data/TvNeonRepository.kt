package mx.utng.smarthealthmonitor.tv.data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mx.utng.smarthealthmonitor.data.remote.NeonClient
import mx.utng.smarthealthmonitor.data.remote.NeonRequest
import mx.utng.smarthealthmonitor.data.remote.LecturaFcDto
 
class TvNeonRepository {
 
    /** Obtener historial completo de los 3 dispositivos */
    suspend fun obtenerHistorialCompleto(limite: Int = 50): List<LecturaFcDto> =
        withContext(Dispatchers.IO) {
            // Registrar/actualizar dispositivo TV
            try {
                NeonClient.api.executeQuery(
                    auth    = NeonClient.AUTH_HEADER,
                    connStr = NeonClient.CONN_STRING,
                    request = NeonRequest(
                        query  = """
                            INSERT INTO dispositivos (tipo, ultimo_sync, activo)
                            VALUES ('tv', NOW(), true)
                            ON CONFLICT (tipo)
                            DO UPDATE SET ultimo_sync = EXCLUDED.ultimo_sync, activo = EXCLUDED.activo
                        """.trimIndent()
                    )
                )
            } catch (e: Exception) {
                android.util.Log.e("TV_DB", "Error registrando dispositivo TV: ${e.message}")
            }

            NeonClient.api.executeQuery(
                auth    = NeonClient.AUTH_HEADER,
                connStr = NeonClient.CONN_STRING,
                request = NeonRequest(
                    query  = """SELECT id,bpm,estado,dispositivo,hora,created_at
                               FROM lecturas_fc
                               ORDER BY created_at DESC
                               LIMIT $1""".trimIndent(),
                    params = listOf(limite)
                )
            ).rows
        }
 
    /** Estadísticas por dispositivo */
    suspend fun obtenerEstadisticas(): List<LecturaFcDto> =
        withContext(Dispatchers.IO) {
            NeonClient.api.executeQuery(
                auth    = NeonClient.AUTH_HEADER,
                connStr = NeonClient.CONN_STRING,
                request = NeonRequest(
                    query  = """SELECT dispositivo,
                               ROUND(AVG(bpm)) AS bpm,
                               'Promedio' AS estado,
                               MAX(hora) AS hora
                               FROM lecturas_fc
                               GROUP BY dispositivo""".trimIndent()
                )
            ).rows
        }
}
