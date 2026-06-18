package mx.utng.smarthealthmonitor.wear.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import mx.utng.smarthealthmonitor.presentation.WearDataHolder

@Composable
fun WearDashboardScreen(
    onAlertClick: () -> Unit = {},
    onHistorialClick: () -> Unit = {}
) {

    val historial by WearDataHolder.historial.collectAsState()

    val pasos = 72
    val fc = 72

    Scaffold(
        timeText = { TimeText() }
    ) {

        ScalingLazyColumn {

            // PASOS
            item {

                Chip(

                    label = {

                        Text(
                            text = "👟 $pasos",
                            color = Color.Black,
                            style = MaterialTheme.typography.title2
                        )
                    },

                    secondaryLabel = {

                        Text(
                            text = "Pasos hoy",
                            color = Color.Black
                        )
                    },

                    onClick = {},

                    colors = ChipDefaults.primaryChipColors(
                        backgroundColor = Color(0xFFF5E6C8),
                        contentColor = Color.Black
                    ),

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                )
            }

            // ALERTA
            item {

                Chip(

                    label = {
                        Text("⚠ Alerta")
                    },

                    secondaryLabel = {
                        Text("$fc bpm")
                    },

                    onClick = onAlertClick,

                    colors = ChipDefaults.primaryChipColors(
                        backgroundColor = Color.Red
                    ),

                    modifier = Modifier.fillMaxWidth()
                )
            }

            // HISTORIAL
            item {

                Chip(

                    label = {
                        Text("📋 Historial (12)")
                    },

                    secondaryLabel = {
                        Text("Registros guardados: 12")
                    },
                    onClick = onHistorialClick,

                    colors = ChipDefaults.secondaryChipColors(),

                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}