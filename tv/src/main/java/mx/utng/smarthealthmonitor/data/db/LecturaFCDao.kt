package mx.utng.smarthealthmonitor.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
@JvmSuppressWildcards
interface LecturaFCDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(lectura: LecturaFC): Long

    @Query("""
        SELECT * FROM lecturas_fc
        ORDER BY timestamp DESC
        LIMIT 50
    """)
    fun obtenerUltimas(): Flow<List<LecturaFC>>

    @Query("SELECT COUNT(*) FROM lecturas_fc")
    suspend fun contarRegistros(): Int

    @Query("""
        DELETE FROM lecturas_fc
        WHERE timestamp < :limite
    """)
    suspend fun limpiarViejos(limite: Long): Int

    // Consulta de detalles por ID para DetailsFragment
    @Query("SELECT * FROM lecturas_fc WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): LecturaFC?

    // Consulta de las últimas 5 lecturas para el Reto de Ver Tendencia
    @JvmSuppressWildcards
    @Query("""
        SELECT * FROM lecturas_fc
        ORDER BY timestamp DESC
        LIMIT 5
    """)
    suspend fun obtenerUltimosCinco(): List<LecturaFC>
}
