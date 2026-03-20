package app.futured.arkitekt.sample.ui.form

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
fun FormScreen(
    navigator: BackStackNavigator<ExampleRoute>,
    modifier: Modifier = Modifier,
    route: ExampleRoute.Form,
    viewModel: FormViewModel =
        hiltViewModel<FormViewModel, FormViewModel.Factory> {
            it.create(route)
        },
) {
    val context = LocalContext.current
    val login by viewModel.viewState.login
    val password by viewModel.viewState.password
    val storedContent by viewModel.viewState.storedContent
    val submitEnabled by viewModel.viewState.submitEnabled
    val safeOnBack = dropUnlessResumed { navigator.onBack() }

    viewModel.EventsEffect {
        onEvent<ShowToastEvent> { Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show() }
        onEvent<NavigateBackEvent> { safeOnBack() }
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        OutlinedTextField(
            value = login,
            onValueChange = { viewModel.viewState.login.value = it },
            label = { Text("Login") },
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = password,
            onValueChange = { viewModel.viewState.password.value = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
        )
        Button(
            onClick = viewModel::onSubmit,
            enabled = submitEnabled,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("submit")
        }
        Text("Stored: $storedContent")
        Button(onClick = viewModel::onBack) {
            Text("back")
        }
    }
}
