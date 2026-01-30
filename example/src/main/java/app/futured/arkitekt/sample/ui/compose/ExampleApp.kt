package app.futured.arkitekt.sample.ui.compose

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.futured.arkitekt.sample.tools.ToastCreator
import app.futured.arkitekt.sample.ui.bottomsheet.ExampleViewModelFactory
import app.futured.arkitekt.sample.ui.coroutinesresult.CoroutinesResultViewModelFactory
import app.futured.arkitekt.sample.ui.detail.DetailViewModelFactory
import app.futured.arkitekt.sample.ui.form.FormViewModelFactory
import app.futured.arkitekt.sample.ui.login.activity.LoginViewModelFactory as LoginActivityViewModelFactory
import app.futured.arkitekt.sample.ui.login.fragment.LoginViewModelFactory as LoginFragmentViewModelFactory
import app.futured.arkitekt.sample.ui.main.MainViewModelFactory

@Composable
fun ExampleApp(dependencies: Dependencies) {
    val navController = rememberNavController()
    MaterialTheme {
        ExampleNavGraph(
            navController = navController,
            dependencies = dependencies,
        )
    }
}

@Composable
private fun ExampleNavGraph(
    navController: NavHostController,
    dependencies: Dependencies,
) {
    NavHost(
        navController = navController,
        startDestination = ExampleRoute.Main.route,
    ) {
        composable(ExampleRoute.Main.route) {
            MainScreen(
                navController = navController,
                viewModelFactory = dependencies.mainViewModelFactory,
            )
        }
        composable(ExampleRoute.Detail.route) {
            DetailScreen(
                navController = navController,
                viewModelFactory = dependencies.detailViewModelFactory,
            )
        }
        composable(ExampleRoute.Form.route) {
            FormScreen(
                navController = navController,
                viewModelFactory = dependencies.formViewModelFactory,
                toastCreator = dependencies.toastCreator,
            )
        }
        composable(ExampleRoute.Login.route) {
            LoginScreen(
                navController = navController,
                activityViewModelFactory = dependencies.loginActivityViewModelFactory,
                viewModelFactory = dependencies.loginFragmentViewModelFactory,
                toastCreator = dependencies.toastCreator,
            )
        }
        composable(ExampleRoute.Coroutines.route) {
            CoroutinesResultScreen(
                navController = navController,
                viewModelFactory = dependencies.coroutinesResultViewModelFactory,
            )
        }
        composable(ExampleRoute.BottomSheet.route) {
            BottomSheetScreen(
                navController = navController,
                viewModelFactory = dependencies.exampleViewModelFactory,
            )
        }
    }
}

@Immutable
data class Dependencies(
    val mainViewModelFactory: MainViewModelFactory,
    val detailViewModelFactory: DetailViewModelFactory,
    val formViewModelFactory: FormViewModelFactory,
    val loginActivityViewModelFactory: LoginActivityViewModelFactory,
    val loginFragmentViewModelFactory: LoginFragmentViewModelFactory,
    val coroutinesResultViewModelFactory: CoroutinesResultViewModelFactory,
    val exampleViewModelFactory: ExampleViewModelFactory,
    val toastCreator: ToastCreator,
)

private sealed class ExampleRoute(val route: String) {
    data object Main : ExampleRoute("main")
    data object Detail : ExampleRoute("detail")
    data object Form : ExampleRoute("form")
    data object Login : ExampleRoute("login")
    data object Coroutines : ExampleRoute("coroutines")
    data object BottomSheet : ExampleRoute("bottomsheet")
}
