package mx.utng.smarthealthmonitor.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text

@Composable
fun WearFilaHistorial(
    lectura: LecturaFC
) {

    val color = if (lectura.esNormal)
        MaterialTheme.colors.primary
    else
        MaterialTheme.colors.error

    Chip(
        label = {
            Text(
                text = "${lectura.valorBpm} bpm",
                color = color
            )
        },

        secondaryLabel = {
            Text(
                text = lectura.hora
            )
        },

        onClick = { },

        colors = ChipDefaults.secondaryChipColors(),

        modifier = Modifier.fillMaxWidth()
    )
}