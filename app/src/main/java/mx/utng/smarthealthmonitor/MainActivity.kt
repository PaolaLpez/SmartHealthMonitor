package mx.utng.smarthealthmonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.navigation.SmartHealthNavGraph
import mx.utng.smarthealthmonitor.ui.theme.SmartHealthMonitorTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // PRUEBA OCULTA (NO UI, NO BOTÓN)
        lifecycleScope.launch(Dispatchers.IO) {
            SmartHealthRepository.actualizarFC(72)
            SmartHealthRepository.actualizarFC(85)
            SmartHealthRepository.actualizarFC(110)
        }

        setContent {
            SmartHealthMonitorTheme {
                SmartHealthNavGraph()
            }
        }
    }
}