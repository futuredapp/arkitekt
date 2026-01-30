package app.futured.arkitekt.sample.ui.compose

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.futured.arkitekt.sample.ui.main.MainViewModelFactory

@Composable
fun MinimalApp(viewModelFactory: MainViewModelFactory) {
    val navController = rememberNavController()
    MaterialTheme {
        MinimalNavGraph(navController = navController, viewModelFactory = viewModelFactory)
    }
}

@Composable
private fun MinimalNavGraph(
    navController: NavHostController,
    viewModelFactory: MainViewModelFactory,
) {
    NavHost(
        navController = navController,
        startDestination = MinimalRoute.Main.route,
    ) {
        composable(MinimalRoute.Main.route) {
            MainScreen(
                viewModelFactory = viewModelFactory,
            )
        }
    }
}

private sealed class MinimalRoute(val route: String) {
    data object Main : MinimalRoute("main")
}
