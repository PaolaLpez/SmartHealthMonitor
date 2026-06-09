package mx.utng.smarthealthmonitor.presentation

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.*
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import androidx.wear.compose.ui.tooling.preview.WearPreviewFontScales
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.wear.R
import mx.utng.smarthealthmonitor.presentation.theme.SmartHealthMonitorTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("MainActivity", "🚀 App iniciando...")

        // 1. Solicitar permisos necesarios
        solicitarPermisos()

        // 2. Iniciar servicio de conteo de pasos
        startService(Intent(this, StepCounterService::class.java))
        Log.d("MainActivity", "✅ Servicio de pasos iniciado")

        // 3. Registrar Health Service para frecuencia cardíaca
        lifecycleScope.launch {
            try {
                HealthDataService.registrar(applicationContext)
                Log.d("MainActivity", "✅ Health Service (FC) registrado")
            } catch (e: Exception) {
                Log.e("MainActivity", "❌ Error en Health Service: ${e.message}")
            }
        }

        setContent {
            SmartHealthMonitorTheme {
                WearApp("Android")
            }
        }
    }

    private fun solicitarPermisos() {
        val permisos = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permisos.add(Manifest.permission.ACTIVITY_RECOGNITION)
        }

        permisos.add(Manifest.permission.BODY_SENSORS)

        val permisosArray = permisos.toTypedArray()
        val permisosNoConcedidos = permisosArray.filter { permiso ->
            ActivityCompat.checkSelfPermission(this, permiso) != android.content.pm.PackageManager.PERMISSION_GRANTED
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            for (i in permissions.indices) {
                val estado = if (grantResults[i] == android.content.pm.PackageManager.PERMISSION_GRANTED)
                    "✅ Concedido"
                else
                    "❌ Denegado"
                Log.d("MainActivity", "Permiso ${permissions[i]}: $estado")
            }
        }
    }
}

@Composable
fun WearApp(greetingName: String) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val wearDataSender = remember { WearDataSender(context) }

    SmartHealthMonitorTheme {
        AppScaffold {
            val listState = rememberTransformingLazyColumnState()
            val transformationSpec = rememberTransformationSpec()

            ScreenScaffold(
                scrollState = listState,
                edgeButton = {
                    EdgeButton(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        ),
                    ) {
                        Text("More")
                    }
                },
            ) { contentPadding ->

                TransformingLazyColumn(
                    contentPadding = contentPadding,
                    state = listState
                ) {
                    item {
                        ListHeader(
                            modifier = Modifier.fillMaxWidth()
                                .transformedHeight(this, transformationSpec),
                            transformation = SurfaceTransformation(transformationSpec),
                        ) {
                            Text(text = stringResource(R.string.hello_world, greetingName))
                        }
                    }

                    // BOTÓN 1: Enviar FRECUENCIA CARDÍACA
                    item {
                        Button(
                            onClick = {
                                scope.launch {
                                    val bpmPrueba = (60..120).random()
                                    wearDataSender.enviarFC(bpmPrueba)
                                    Log.d("WearApp", "❤️ Enviando FC: $bpmPrueba BPM")
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                                .transformedHeight(this, transformationSpec),
                            transformation = SurfaceTransformation(transformationSpec),
                        ) {
                            Text("❤️ Enviar FC")
                        }
                    }

                    // BOTÓN 2: Enviar PASOS
                    item {
                        Button(
                            onClick = {
                                scope.launch {
                                    val pasosPrueba = (1000..10000).random()
                                    wearDataSender.enviarPasos(pasosPrueba)
                                    Log.d("WearApp", "👟 Enviando Pasos: $pasosPrueba")
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                                .transformedHeight(this, transformationSpec),
                            transformation = SurfaceTransformation(transformationSpec),
                        ) {
                            Text("👟 Enviar Pasos")
                        }
                    }

                    // BOTÓN 3: Enviar AMBOS
                    item {
                        Button(
                            onClick = {
                                scope.launch {
                                    val bpmPrueba = (60..120).random()
                                    val pasosPrueba = (1000..10000).random()
                                    wearDataSender.enviarFC(bpmPrueba)
                                    wearDataSender.enviarPasos(pasosPrueba)
                                    Log.d("WearApp", "📤 Enviando: FC=$bpmPrueba, Pasos=$pasosPrueba")
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                                .transformedHeight(this, transformationSpec),
                            transformation = SurfaceTransformation(transformationSpec),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text("📤 Enviar Ambos")
                        }
                    }
                }
            }
        }
    }
}

@WearPreviewDevices
@WearPreviewFontScales
@Composable
fun DefaultPreview() {
    WearApp("Preview Android")
}