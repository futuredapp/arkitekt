package app.futured.arkitekt.sample.ui.compose

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Composable
fun ExampleApp() {
    val navController = rememberNavController()
    MaterialTheme {
        ExampleNavGraph(
            navController = navController,
        )
    }
}

@Composable
private fun ExampleNavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = ExampleRoute.Main,
    ) {
        composable<ExampleRoute.Main> {
            MainScreen(
                navController = navController,
            )
        }
        composable<ExampleRoute.Detail> {
            DetailScreen(
                navController = navController
            )
        }
        composable<ExampleRoute.Form> {
            FormScreen(
                navController = navController,
            )
        }
        composable<ExampleRoute.Login> {
            LoginScreen(
                navController = navController,
            )
        }
        composable<ExampleRoute.Coroutines> {
            CoroutinesResultScreen(
                navController = navController,
            )
        }
        composable<ExampleRoute.BottomSheet> {
            BottomSheetScreen(
                navController = navController,
            )
        }
    }
}


sealed interface ExampleRoute {
    @Serializable
    data object Main : ExampleRoute

    @Serializable
    data object Detail : ExampleRoute

    @Serializable
    data object Form : ExampleRoute

    @Serializable
    data object Login : ExampleRoute

    @Serializable
    data object Coroutines : ExampleRoute

    @Serializable
    data object BottomSheet : ExampleRoute
}
