package app.futured.arkitekt.sample.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.futured.arkitekt.sample.ui.main.MainViewModel
import app.futured.arkitekt.sample.ui.main.MainViewModelFactory

@Composable
fun MainScreen(viewModelFactory: MainViewModelFactory) {
    val viewModel: MainViewModel = viewModel(factory = viewModelFactory)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Example minimal Compose screen")
    }
}
