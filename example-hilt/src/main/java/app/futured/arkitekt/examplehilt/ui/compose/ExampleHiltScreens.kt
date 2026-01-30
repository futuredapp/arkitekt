package app.futured.arkitekt.examplehilt.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import app.futured.arkitekt.core.EventsEffect
import app.futured.arkitekt.core.onEvent
import app.futured.arkitekt.examplehilt.ui.bottomsheet.CloseEvent
import app.futured.arkitekt.examplehilt.ui.bottomsheet.SomeViewModel
import app.futured.arkitekt.examplehilt.ui.first.FirstViewModel
import app.futured.arkitekt.examplehilt.ui.first.NavigateToBottomSheetEvent
import app.futured.arkitekt.examplehilt.ui.first.NavigateToSecondFragmentEvent
import app.futured.arkitekt.examplehilt.ui.second.SecondViewModel

@Composable
fun FirstScreen(navController: NavHostController) {
    val viewModel: FirstViewModel = hiltViewModel()
    val displayText by viewModel.viewState.displayText.observeAsState("")

    LaunchedEffect(viewModel) {
        viewModel.onStart()
    }

    viewModel.EventsEffect {
        onEvent<NavigateToSecondFragmentEvent> { navController.navigate("second/${it.number}") }
        onEvent<NavigateToBottomSheetEvent> { navController.navigate("bottomsheet/${it.number}") }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(displayText)
        Button(onClick = viewModel::onNext, modifier = Modifier.fillMaxWidth()) {
            Text("Next")
        }
        Button(onClick = viewModel::onBottomSheet, modifier = Modifier.fillMaxWidth()) {
            Text("Bottom sheet")
        }
    }
}

@Composable
fun SecondScreen(navController: NavHostController) {
    val viewModel: SecondViewModel = hiltViewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(viewModel.viewState.displayText)
        Button(onClick = { navController.popBackStack() }) {
            Text("Back")
        }
    }
}

@Composable
fun BottomSheetScreen(navController: NavHostController) {
    val viewModel: SomeViewModel = hiltViewModel()

    viewModel.EventsEffect {
        onEvent<CloseEvent> { navController.popBackStack() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Number: ${viewModel.viewState.number}")
        Button(onClick = viewModel::onClose) {
            Text("Close")
        }
    }
}
