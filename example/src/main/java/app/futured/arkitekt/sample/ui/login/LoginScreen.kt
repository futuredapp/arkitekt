package app.futured.arkitekt.sample.ui.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.dropUnlessResumed
import app.futured.arkitekt.compose.EventsEffect
import app.futured.arkitekt.compose.onEvent
import app.futured.arkitekt.sample.ui.compose.BackStackNavigator
import app.futured.arkitekt.sample.ui.compose.ExampleRoute

@Composable
fun LoginScreen(
    navigator: BackStackNavigator<ExampleRoute>,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val name by viewModel.viewState.name
    val surname by viewModel.viewState.surname
    val fullName by viewModel.viewState.fullName
    val showHeader by viewModel.viewState.showHeader
    val safeOnBack = dropUnlessResumed { navigator.onBack() }

    viewModel.EventsEffect {
        onEvent<ShowToastEvent> { Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show() }
        onEvent<NavigateBackEvent> { safeOnBack() }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (showHeader) {
            Text("Header is visible")
        }
        OutlinedTextField(
            value = name,
            onValueChange = { viewModel.viewState.name.value = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = surname,
            onValueChange = { viewModel.viewState.surname.value = it },
            label = { Text("Surname") },
            modifier = Modifier.fillMaxWidth(),
        )
        Button(onClick = viewModel::logIn, modifier = Modifier.fillMaxWidth()) {
            Text("login")
        }
        Text("Full name: $fullName")
        Button(onClick = viewModel::onBack) {
            Text("back")
        }
    }
}
