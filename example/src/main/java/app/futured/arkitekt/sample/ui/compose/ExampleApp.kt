package app.futured.arkitekt.sample.ui.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import app.futured.arkitekt.sample.ui.bottomsheet.BottomSheetScreen
import app.futured.arkitekt.sample.ui.coroutinesresult.CoroutinesResultScreen
import app.futured.arkitekt.sample.ui.detail.DetailScreen
import app.futured.arkitekt.sample.ui.form.FormScreen
import app.futured.arkitekt.sample.ui.home.HomeScreen
import app.futured.arkitekt.sample.ui.login.LoginScreen
import kotlinx.serialization.Serializable

sealed interface ExampleRoute : NavKey {
    @Serializable
    data object Home : ExampleRoute

    @Serializable
    data object Detail : ExampleRoute

    @Serializable
    data class Form(val name: String) : ExampleRoute

    @Serializable
    data object Login : ExampleRoute

    @Serializable
    data object Coroutines : ExampleRoute

    @Serializable
    data object BottomSheet : ExampleRoute
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExampleApp() {
    val backStack = rememberNavBackStack(ExampleRoute.Home)
    val bottomSheetStrategy = remember { BottomSheetSceneStrategy<NavKey>() }
    val backStackNavigator = remember {
        @Suppress("UNCHECKED_CAST")
        BackStackNavigatorImpl(backStack) as BackStackNavigator<ExampleRoute>
    }


    MaterialTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
            NavDisplay(
                modifier = Modifier.padding(paddingValues),
                backStack = backStack,
                onBack = dropUnlessResumed { backStackNavigator.onBack() },
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator()
                ),
                sceneStrategy = bottomSheetStrategy,
                entryProvider = entryProvider {
                    entry<ExampleRoute.Home> {
                        HomeScreen(navigator = backStackNavigator)
                    }
                    entry<ExampleRoute.Detail> {
                        DetailScreen(navigator = backStackNavigator)
                    }
                    entry<ExampleRoute.Form> {
                        FormScreen(
                            navigator = backStackNavigator,
                            route = it,
                        )
                    }
                    entry<ExampleRoute.Login> {
                        LoginScreen(navigator = backStackNavigator)
                    }
                    entry<ExampleRoute.Coroutines> {
                        CoroutinesResultScreen(navigator = backStackNavigator)
                    }
                    entry<ExampleRoute.BottomSheet>(
                        metadata = BottomSheetSceneStrategy.bottomSheet()
                    ) {
                        BottomSheetScreen(navigator = backStackNavigator)
                    }
                }
            )
        }
    }
}
