package app.futured.arkitekt.sample.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import app.futured.arkitekt.compose.EventsEffect
import app.futured.arkitekt.compose.onEvent

@Composable
fun DetailScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val numberText by viewModel.viewState.stringNumber


    viewModel.EventsEffect {
        onEvent<NavigateBackEvent> { navController.popBackStack() }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Number: $numberText")
        Button(onClick = viewModel::incrementNumber) {
            Text("increment")
        }
        Button(onClick = viewModel::onBack) {
            Text("back")
        }
    }
}
