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
import androidx.navigation.NavHostController
import app.futured.arkitekt.compose.EventsEffect
import app.futured.arkitekt.compose.onEvent
import app.futured.arkitekt.sample.ui.compose.ExampleRoute

@Composable
fun HomeScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {


    viewModel.EventsEffect {
        onEvent<ShowDetailEvent> { navController.navigate(ExampleRoute.Detail) }
        onEvent<ShowFormEvent> { navController.navigate(ExampleRoute.Form) }
        onEvent<ShowLoginEvent> { navController.navigate(ExampleRoute.Login) }
        onEvent<ShowBottomSheetEvent> { navController.navigate(ExampleRoute.BottomSheet) }
        onEvent<ShowLoadEvent> { navController.navigate(ExampleRoute.Coroutines) }
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
