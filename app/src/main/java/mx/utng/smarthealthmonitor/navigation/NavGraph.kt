package mx.utng.smarthealthmonitor.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mx.utng.smarthealthmonitor.LoginScreen
import mx.utng.smarthealthmonitor.ui.screens.AlertaScreen
import mx.utng.smarthealthmonitor.ui.screens.DashboardScreen
import mx.utng.smarthealthmonitor.ui.screens.HistorialScreen
import mx.utng.smarthealthmonitor.ui.theme.SmartHealthMonitorTheme

@Composable
fun SmartHealthNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {

        // LOGIN
        composable(Screen.Login.route) {

            LoginScreen(
                onLoginSuccess = {

                    navController.navigate(Screen.Dashboard.route) {

                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // DASHBOARD
        composable(Screen.Dashboard.route) {

            DashboardScreen(

                onHistorialClick = {
                    navController.navigate(Screen.Historial.route)
                },

                onAlertClick = {
                    navController.navigate(Screen.Alerta.route)
                }
            )
        }

        // HISTORIAL
        composable(Screen.Historial.route) {

            HistorialScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // ALERTA
        composable(Screen.Alerta.route) {

            AlertaScreen(

                fc = 145,

                onDismiss = {
                    navController.popBackStack()
                },

                onConfirmar = { nota ->

                    Log.d(
                        "SmartHealth",
                        "Alerta enviada. Nota: $nota"
                    )

                    // TODO S7: enviar alerta real a contactos

                    navController.popBackStack()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEnConstruccion(
    titulo: String,
    onBack: () -> Unit
) {

    SmartHealthMonitorTheme {

        Scaffold(

            topBar = {

                TopAppBar(

                    title = {
                        Text(titulo)
                    },

                    navigationIcon = {

                        IconButton(
                            onClick = onBack
                        ) {

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Regresar"
                            )
                        }
                    }
                )
            }

        ) { pad ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(pad),

                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "Próximamente: $titulo",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}