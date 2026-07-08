package mx.utng.smarthealthmonitor.tv
 
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.Lifecycle
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.data.db.LecturaFC
 
class MainFragment : BrowseSupportFragment() {
 
    private val viewModel: TvViewModel by viewModels()
    private lateinit var histAdapter: ArrayObjectAdapter
 
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
 
        // Configuración del BrowseFragment
        title        = "SmartHealth TV"
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
 
        // Color de la marca en el sidebar
        brandColor = resources.getColor(R.color.sh_primary, null)
 
        cargarFilas()
        observarDatos()
    }
 
    private fun observarDatos() {
        // Observar historial de Room y actualizar la fila
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.historial.collect { lecturas ->
                    histAdapter.clear()
                    lecturas.forEach { histAdapter.add(it) }
                }
            }
        }
    }
 
    private fun cargarFilas() {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
 
        // ── Fila 1: Estado actual (FC + Pasos) ───────────
        val estadoAdapter = ArrayObjectAdapter(FCCardPresenter())
        estadoAdapter.add(LecturaFC(id=0, valorBpm=88, timestamp=System.currentTimeMillis(), hora="Ahora", esNormal=true))
        estadoAdapter.add(LecturaFC(id=1, valorBpm=4250, timestamp=System.currentTimeMillis(), hora="Pasos", esNormal=true))
        rowsAdapter.add(ListRow(HeaderItem("Estado actual"), estadoAdapter))
 
        // ── Fila 2: Historial de FC (Adaptador reactivo) ─
        histAdapter = ArrayObjectAdapter(FCCardPresenter())
        rowsAdapter.add(ListRow(HeaderItem("Historial FC"), histAdapter))
 
        // ── Fila 3: Alertas recientes (Reto adicional) ────
        val alertasAdapter = ArrayObjectAdapter(FCCardPresenter())
        alertasAdapter.add(LecturaFC(id=10, valorBpm=140, timestamp=System.currentTimeMillis(), hora="10:15 AM", esNormal=false))
        alertasAdapter.add(LecturaFC(id=11, valorBpm=45, timestamp=System.currentTimeMillis(), hora="11:30 AM", esNormal=false))
        alertasAdapter.add(LecturaFC(id=12, valorBpm=155, timestamp=System.currentTimeMillis(), hora="02:10 PM", esNormal=false))
        rowsAdapter.add(ListRow(HeaderItem("Alertas recientes"), alertasAdapter))
 
        this.adapter = rowsAdapter
    }
}
