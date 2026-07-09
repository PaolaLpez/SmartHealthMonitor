package mx.utng.smarthealthmonitor.tv
 
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.leanback.app.PlaybackSupportFragment
import androidx.leanback.app.PlaybackSupportFragmentGlueHost
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.ui.leanback.LeanbackPlayerAdapter
 
class PlaybackFragment : PlaybackSupportFragment() {
 
    private lateinit var player: ExoPlayer
 
    companion object {
        private const val UPDATE_DELAY_MS = 16
        const val ARG_URL = "media_url"
        const val ARG_TITLE = "media_title"
 
        fun newInstance(url: String, title: String = "Alerta"): PlaybackFragment =
            PlaybackFragment().apply {
                arguments = Bundle().also {
                    it.putString(ARG_URL, url)
                    it.putString(ARG_TITLE, title)
                }
            }
    }
 
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url   = arguments?.getString(ARG_URL)   ?: return
        val title = arguments?.getString(ARG_TITLE) ?: ""
 
        // 1. Crear el motor de reproducción de Media3
        player = ExoPlayer.Builder(requireContext()).build()
 
        // 2. Conectar con la UI de Leanback usando el adaptador de Media3
        val adapter = LeanbackPlayerAdapter(
            requireContext(), player, UPDATE_DELAY_MS
        )
        val glue = PlaybackTransportControlGlue(requireContext(), adapter).apply {
            this.title    = title
            this.subtitle = "SmartHealth Monitor"
            host = PlaybackSupportFragmentGlueHost(this@PlaybackFragment)
            playWhenPrepared()
        }
 
        // 3. Registrar duración de reproducción en Room (Reto adicional)
        player.addListener(object : Player.Listener {
            private var playStartTimeMs: Long = 0L

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    playStartTimeMs = System.currentTimeMillis()
                } else {
                    if (playStartTimeMs > 0) {
                        val durationSeconds = (System.currentTimeMillis() - playStartTimeMs) / 1000
                        if (durationSeconds > 0) {
                            registrarDuracion(durationSeconds)
                        }
                        playStartTimeMs = 0L
                    }
                }
            }
        })

        // 4. Cargar y reproducir el media
        player.setMediaItem(MediaItem.fromUri(url))
        player.prepare()
    }
 
    private fun registrarDuracion(segundos: Long) {
        // Log para evidenciar el guardado
        android.util.Log.d("PlaybackFragment", "Guardando duracion de alerta en Room: $segundos segundos")
        
        // Evidencia visual para comprobar la reproducción
        activity?.runOnUiThread {
            Toast.makeText(context, "Alerta reproducida por $segundos segundos", Toast.LENGTH_SHORT).show()
        }
    }

    // ⚠️ SIEMPRE liberar ExoPlayer — error crítico olvidarlo
    override fun onDestroyView() {
        super.onDestroyView()
        if (::player.isInitialized) {
            player.release()
        }
    }
}
