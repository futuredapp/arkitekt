package app.futured.arkitekt.examplehilt.ui.compose

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.compose.rememberNavController

@Composable
fun ExampleHiltApp() {
    val navController = rememberNavController()
    MaterialTheme {
        ExampleHiltNavGraph(navController = navController)
    }
}

@Composable
private fun ExampleHiltNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = ExampleHiltRoute.First.route,
    ) {
        composable(ExampleHiltRoute.First.route) {
            FirstScreen(navController = navController)
        }
        composable(
            route = ExampleHiltRoute.Second.route,
            arguments = listOf(navArgument("number") { type = NavType.IntType })
        ) {
            SecondScreen(navController = navController)
        }
        composable(
            route = ExampleHiltRoute.BottomSheet.route,
            arguments = listOf(navArgument("number") { type = NavType.IntType })
        ) {
            BottomSheetScreen(navController = navController)
        }
    }
}

private sealed class ExampleHiltRoute(val route: String) {
    data object First : ExampleHiltRoute("first")
    data object Second : ExampleHiltRoute("second/{number}")
    data object BottomSheet : ExampleHiltRoute("bottomsheet/{number}")
}
