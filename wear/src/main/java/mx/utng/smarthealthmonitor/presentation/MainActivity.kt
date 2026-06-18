package mx.utng.smarthealthmonitor.presentation

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.presentation.theme.SmartHealthMonitorTheme
import mx.utng.smarthealthmonitor.wear.presentation.SmartHealthWearNavGraph

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("MainActivity", "🚀 App iniciando...")

        // Solicitar permisos
        solicitarPermisos()

        // Iniciar servicio de pasos
        startService(Intent(this, StepCounterService::class.java))

        Log.d("MainActivity", "✅ Servicio de pasos iniciado")

        // Registrar Health Service
        lifecycleScope.launch {
            try {

                HealthDataService.registrar(applicationContext)

                Log.d(
                    "MainActivity",
                    "✅ Health Service (FC) registrado"
                )

            } catch (e: Exception) {

                Log.e(
                    "MainActivity",
                    "❌ Error en Health Service: ${e.message}"
                )
            }
        }

        // NUEVA PANTALLA PRINCIPAL
        setContent {

            SmartHealthMonitorTheme {

                SmartHealthWearNavGraph()

            }
        }
    }

    private fun solicitarPermisos() {

        val permisos = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            permisos.add(
                Manifest.permission.ACTIVITY_RECOGNITION
            )
        }

        permisos.add(
            Manifest.permission.BODY_SENSORS
        )

        val permisosNoConcedidos =
            permisos.filter { permiso ->

                ActivityCompat.checkSelfPermission(
                    this,
                    permiso
                ) != android.content.pm.PackageManager.PERMISSION_GRANTED
            }

        if (permisosNoConcedidos.isNotEmpty()) {

            ActivityCompat.requestPermissions(
                this,
                permisosNoConcedidos.toTypedArray(),
                1001
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )

        if (requestCode == 1001) {

            for (i in permissions.indices) {

                val estado =
                    if (grantResults[i] ==
                        android.content.pm.PackageManager.PERMISSION_GRANTED
                    ) {
                        "✅ Concedido"
                    } else {
                        "❌ Denegado"
                    }

                Log.d(
                    "MainActivity",
                    "Permiso ${permissions[i]}: $estado"
                )
            }
        }
    }
}