package app.futured.arkitekt.sample.ui.compose

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

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
        startDestination = ExampleRoute.Main.route,
    ) {
        composable(ExampleRoute.Main.route) {
            MainScreen(
                navController = navController,
            )
        }
        composable(ExampleRoute.Detail.route) {
            DetailScreen(
                navController = navController
            )
        }
        composable(ExampleRoute.Form.route) {
            FormScreen(
                navController = navController,
            )
        }
        composable(ExampleRoute.Login.route) {
            LoginScreen(
                navController = navController,
            )
        }
        composable(ExampleRoute.Coroutines.route) {
            CoroutinesResultScreen(
                navController = navController,
            )
        }
        composable(ExampleRoute.BottomSheet.route) {
            BottomSheetScreen(
                navController = navController,
            )
        }
    }
}


private sealed class ExampleRoute(val route: String) {
    data object Main : ExampleRoute("main")
    data object Detail : ExampleRoute("detail")
    data object Form : ExampleRoute("form")
    data object Login : ExampleRoute("login")
    data object Coroutines : ExampleRoute("coroutines")
    data object BottomSheet : ExampleRoute("bottomsheet")
}
