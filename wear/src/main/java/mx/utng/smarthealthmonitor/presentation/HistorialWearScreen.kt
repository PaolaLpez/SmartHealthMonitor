package mx.utng.smarthealthmonitor.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HistorialWearScreen() {

    val historial by WearDataHolder.historial.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(10.dp)
    ) {

        items(historial) { item ->

            val parts = item.split("|")

            val bpm = parts.getOrNull(0) ?: ""
            val hora = parts.getOrNull(1) ?: ""

            Text("💓 $bpm bpm  🕒 $hora")
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}