package mx.utng.smarthealthmonitor.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class TVActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            androidx.tv.material3.MaterialTheme {
                val navController = rememberNavController()
                val sharedViewModel: TvViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = TvViewModelFactory(applicationContext)
                )
                NavHost(navController, startDestination = "catalog") {
                    composable("catalog") {
                        TvCatalogScreen(
                            onCardClick = { lecturaId ->
                                navController.navigate("detail/$lecturaId")
                            },
                            viewModel = sharedViewModel
                        )
                    }
                    composable(
                        route = "detail/{lecturaId}",
                        arguments = listOf(navArgument("lecturaId") { type = NavType.IntType })
                    ) { backStack ->
                        val id = backStack.arguments?.getInt("lecturaId") ?: return@composable
                        TvDetailScreen(
                            lecturaId = id,
                            navController = navController,
                            viewModel = sharedViewModel
                        )
                    }
                    composable("playback") {
                        TvPlaybackScreen(navController = navController)
                    }
                }
            }
        }
    }
}
