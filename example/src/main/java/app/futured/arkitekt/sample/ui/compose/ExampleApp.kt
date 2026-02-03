package app.futured.arkitekt.sample.ui.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.futured.arkitekt.sample.ui.bottomsheet.BottomSheetScreen
import app.futured.arkitekt.sample.ui.coroutinesresult.CoroutinesResultScreen
import app.futured.arkitekt.sample.ui.detail.DetailScreen
import app.futured.arkitekt.sample.ui.form.FormScreen
import app.futured.arkitekt.sample.ui.home.HomeScreen
import app.futured.arkitekt.sample.ui.login.LoginScreen
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
    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            startDestination = ExampleRoute.Home,
        ) {
            composable<ExampleRoute.Home> {
                HomeScreen(
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

}


sealed interface ExampleRoute {
    @Serializable
    data object Home : ExampleRoute

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
