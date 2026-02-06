package app.futured.arkitekt.sample.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.dropUnlessResumed
import app.futured.arkitekt.compose.EventsEffect
import app.futured.arkitekt.compose.onEvent
import app.futured.arkitekt.sample.ui.compose.BackStackNavigator
import app.futured.arkitekt.sample.ui.compose.ExampleRoute

@Composable
fun HomeScreen(
    navigator: BackStackNavigator<ExampleRoute>,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val safeNavigateToDetail = dropUnlessResumed { navigator.onNavigate(ExampleRoute.Detail) }
    val safeNavigateToForm = dropUnlessResumed { navigator.onNavigate(ExampleRoute.Form("testArg")) }
    val safeNavigateToLogin = dropUnlessResumed { navigator.onNavigate(ExampleRoute.Login) }
    val safeNavigateToBottomSheet = dropUnlessResumed { navigator.onNavigate(ExampleRoute.BottomSheet) }
    val safeNavigateToCoroutines = dropUnlessResumed { navigator.onNavigate(ExampleRoute.Coroutines) }

    viewModel.EventsEffect {
        onEvent<ShowDetailEvent> { safeNavigateToDetail() }
        onEvent<ShowFormEvent> { safeNavigateToForm() }
        onEvent<ShowLoginEvent> { safeNavigateToLogin() }
        onEvent<ShowBottomSheetEvent> { safeNavigateToBottomSheet() }
        onEvent<ShowLoadEvent> { safeNavigateToCoroutines() }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Button(onClick = viewModel::onDetail, modifier = Modifier.fillMaxWidth()) {
            Text("detail")
        }
        Button(onClick = viewModel::onForm, modifier = Modifier.fillMaxWidth()) {
            Text("form")
        }
        Button(onClick = viewModel::onLogin, modifier = Modifier.fillMaxWidth()) {
            Text("login")
        }
        Button(onClick = viewModel::onBottomSheet, modifier = Modifier.fillMaxWidth()) {
            Text("bottom sheet")
        }
        Button(onClick = viewModel::onLoad, modifier = Modifier.fillMaxWidth()) {
            Text("Coroutines Result")
        }
    }
}
