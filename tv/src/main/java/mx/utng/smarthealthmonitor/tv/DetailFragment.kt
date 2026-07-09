package mx.utng.smarthealthmonitor.tv
 
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.leanback.app.DetailsSupportFragment
import androidx.leanback.widget.*
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.data.db.LecturaFC
import mx.utng.smarthealthmonitor.data.db.SmartHealthDB
 
class DetailFragment : DetailsSupportFragment(),
    OnActionClickedListener {
 
    private lateinit var lectura: LecturaFC

    companion object {
        const val ARG_LECTURA_ID = "lectura_id"
        const val ACTION_PLAY    = 1L
        const val ACTION_BACK    = 2L
        const val ACTION_TREND   = 3L
        
        const val TEST_AUDIO_URL = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
 
        fun newInstance(lecturaId: Int): DetailFragment {
            return DetailFragment().apply {
                arguments = Bundle().also {
                    it.putInt(ARG_LECTURA_ID, lecturaId)
                }
            }
        }
    }
 
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getInt(ARG_LECTURA_ID) ?: return
 
        viewLifecycleOwner.lifecycleScope.launch {
            // Buscar la lectura en Room por ID
            val dbLectura = SmartHealthDB.getDatabase(requireContext())
                .lecturaDao().obtenerPorId(id)
            dbLectura?.let {
                lectura = it
                construirDetalle(it)
            }
        }
    }
 
    private fun construirDetalle(lectura: LecturaFC) {
        val selector = ClassPresenterSelector()
 
        val dpPresenter = FullWidthDetailsOverviewRowPresenter(
            DetailsDescriptionPresenter()
        )
        dpPresenter.setOnActionClickedListener(this)
        selector.addClassPresenter(DetailsOverviewRow::class.java, dpPresenter)
 
        val row = DetailsOverviewRow(lectura)
        // Ícono de corazón como imagen del detalle
        val iconRes = if (lectura.esNormal) {
            android.R.drawable.ic_menu_compass  // placeholder OK
        } else {
            android.R.drawable.ic_dialog_alert  // placeholder error
        }
        row.imageDrawable = ContextCompat.getDrawable(requireContext(), iconRes)
 
        // Botones de acción
        val actions = ArrayObjectAdapter()
        actions.add(Action(ACTION_PLAY, "▶ Reproducir alerta"))
        actions.add(Action(ACTION_BACK, "← Volver al historial"))
        actions.add(Action(ACTION_TREND, "📊 Ver tendencia"))
        row.actionsAdapter = actions
 
        val adapter = ArrayObjectAdapter(selector)
        adapter.add(row)
        this.adapter = adapter
    }
 
    override fun onActionClicked(action: Action) {
        when (action.id) {
            ACTION_PLAY -> {
                val title = if (lectura.hora == "Pasos") "Alerta Pasos" else "Alerta FC ${lectura.valorBpm} bpm"
                val playback = PlaybackFragment.newInstance(
                    url = TEST_AUDIO_URL,
                    title = title
                )
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_browse_fragment, playback)
                    .addToBackStack(null)
                    .commit()
            }
            ACTION_BACK -> {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            ACTION_TREND -> {
                // Reto adicional: Consultar las últimas 5 lecturas de Room y mostrarlas
                viewLifecycleOwner.lifecycleScope.launch {
                    val ultimas = SmartHealthDB.getDatabase(requireContext())
                        .lecturaDao().obtenerUltimosCinco()
                    
                    val textList = ultimas.reversed().map { 
                        if (it.hora == "Pasos") "${it.valorBpm} pasos" else "${it.valorBpm} bpm"
                    }
                    val text = textList.joinToString(" -> ")
                    
                    Toast.makeText(
                        context,
                        "Tendencia (ultimos 5): $text",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
