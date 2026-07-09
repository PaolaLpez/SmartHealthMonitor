package mx.utng.smarthealthmonitor.tv

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.material3.*

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TvCatalogScreen(
    onCardClick: (Int) -> Unit,
    viewModel: TvViewModel = viewModel(factory = TvViewModelFactory(LocalContext.current))
) {
    val state by viewModel.state.collectAsState()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070B1E)) // Elegant deep dark background
            .padding(start = 48.dp, top = 32.dp, end = 48.dp, bottom = 16.dp)
    ) {
        TvLazyColumn(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // HEADER TITLE
            item {
                Text(
                    text = "SmartHealth TV",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            // ROW 1: ESTADO ACTUAL
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Estado actual",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        // Card 1: FC
                        Surface(
                            onClick = {},
                            modifier = Modifier
                                .width(180.dp)
                                .height(100.dp),
                            colors = ClickableSurfaceDefaults.colors(
                                containerColor = Color(0xFF1565C0),
                                focusedContainerColor = Color(0xFF1E88E5)
                            ),
                            shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(8.dp))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = if (state.fc > 0) "${state.fc} bpm" else "-- bpm",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Ahora",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                        }

                        // Card 2: Pasos
                        Surface(
                            onClick = {},
                            modifier = Modifier
                                .width(180.dp)
                                .height(100.dp),
                            colors = ClickableSurfaceDefaults.colors(
                                containerColor = Color(0xFFF5E6C8),
                                focusedContainerColor = Color(0xFFFFF1D6)
                            ),
                            shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(8.dp))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = if (state.pasos > 0) "${state.pasos}" else "0",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Pasos hoy",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Black.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }

            // ROW 2: HISTORIAL FC
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Historial FC",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    
                    if (state.lecturas.isEmpty()) {
                        Text(
                            text = "No hay registros disponibles",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.5f),
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    } else {
                        TvLazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            items(state.lecturas.size) { index ->
                                val lectura = state.lecturas[index]
                                val containerColor = if (lectura.esNormal) Color(0xFF0F2042) else Color(0xFF4A0E17)
                                val focusedColor = if (lectura.esNormal) Color(0xFF1D3567) else Color(0xFF721C24)
                                
                                Surface(
                                    onClick = { onCardClick(lectura.id) },
                                    modifier = Modifier
                                        .width(160.dp)
                                        .height(90.dp),
                                    colors = ClickableSurfaceDefaults.colors(
                                        containerColor = containerColor,
                                        focusedContainerColor = focusedColor
                                    ),
                                    shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(8.dp))
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(12.dp),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "${lectura.valorBpm} bpm",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = lectura.hora,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.White.copy(alpha = 0.6f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ROW 3: ALERTAS RECIENTES
            item {
                val alertas = state.lecturas.filter { !it.esNormal }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Alertas recientes",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    
                    if (alertas.isEmpty()) {
                        Text(
                            text = "No hay alertas registradas",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.5f),
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    } else {
                        TvLazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            items(alertas.size) { index ->
                                val lectura = alertas[index]
                                
                                Surface(
                                    onClick = { onCardClick(lectura.id) },
                                    modifier = Modifier
                                        .width(160.dp)
                                        .height(90.dp),
                                    colors = ClickableSurfaceDefaults.colors(
                                        containerColor = Color(0xFF4A0E17),
                                        focusedContainerColor = Color(0xFF721C24)
                                    ),
                                    shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(8.dp))
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(12.dp),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "${lectura.valorBpm} bpm",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = lectura.hora,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.White.copy(alpha = 0.6f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
