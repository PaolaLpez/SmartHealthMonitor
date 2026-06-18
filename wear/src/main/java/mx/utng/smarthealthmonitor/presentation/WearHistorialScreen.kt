package mx.utng.smarthealthmonitor.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText

@Composable
fun WearHistorialScreen(
    onBack: () -> Unit
) {

    val historial = listOf(
        "110 bpm - 01:08",
        "85 bpm - 01:08",
        "72 bpm - 01:07",
        "72 bpm - 01:06",
        "110 bpm - 19:29",
        "85 bpm - 19:29",
        "72 bpm - 19:29",
        "110 bpm - 19:22",
        "85 bpm - 19:22",
        "72 bpm - 19:22",
        "110 bpm - 18:18",
        "85 bpm - 18:18"
    )

    Scaffold(
        timeText = {
            TimeText()
        }
    ) {

        ScalingLazyColumn {

            item {

                Text(
                    text = "Historial (${historial.size})"
                )
            }

            items(historial.size) { i ->

                Chip(

                    label = {

                        Text(
                            text = historial[i].substringBefore(" - ")
                        )
                    },

                    secondaryLabel = {

                        Text(
                            text = historial[i].substringAfter(" - ")
                        )
                    },

                    onClick = { },

                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}