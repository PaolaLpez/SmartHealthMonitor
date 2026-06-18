package mx.utng.smarthealthmonitor.wear.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import mx.utng.smarthealthmonitor.presentation.WearDataHolder
import mx.utng.smarthealthmonitor.presentation.WearHistorialScreen

object WearScreens {

    const val DASHBOARD = "wear_dashboard"
    const val ALERTA = "wear_alerta"
    const val HISTORIAL = "wear_historial"
}

@Composable
fun SmartHealthWearNavGraph() {

    val navController = rememberSwipeDismissableNavController()

    SwipeDismissableNavHost(
        navController = navController,
        startDestination = WearScreens.DASHBOARD
    ) {

        composable(WearScreens.DASHBOARD) {

            WearDashboardScreen(
                onAlertClick = {
                    navController.navigate(WearScreens.ALERTA)
                },
                onHistorialClick = {
                    navController.navigate(WearScreens.HISTORIAL)
                }
            )
        }

        composable(WearScreens.ALERTA) {

            val fc by WearDataHolder.fc.collectAsState()

            WearAlertaScreen(
                fc = fc,
                onConfirmar = { navController.popBackStack() },
                onCancelar = { navController.popBackStack() }
            )
        }

        composable(WearScreens.HISTORIAL) {

            WearHistorialScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}