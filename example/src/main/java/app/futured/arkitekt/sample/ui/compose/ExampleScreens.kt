package app.futured.arkitekt.sample.ui.compose

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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import app.futured.arkitekt.compose.EventsEffect
import app.futured.arkitekt.compose.onEvent
import app.futured.arkitekt.sample.ui.bottomsheet.CloseEvent
import app.futured.arkitekt.sample.ui.bottomsheet.ExampleViewModel
import app.futured.arkitekt.sample.ui.coroutinesresult.CoroutinesResultViewModel
import app.futured.arkitekt.sample.ui.detail.DetailViewModel
import app.futured.arkitekt.sample.ui.form.FormViewModel
import app.futured.arkitekt.sample.ui.login.ShowToastEvent
import app.futured.arkitekt.sample.ui.main.MainViewModel
import app.futured.arkitekt.sample.ui.main.ShowBottomSheetEvent
import app.futured.arkitekt.sample.ui.main.ShowDetailEvent
import app.futured.arkitekt.sample.ui.main.ShowFormEvent
import app.futured.arkitekt.sample.ui.main.ShowLoadEvent
import app.futured.arkitekt.sample.ui.main.ShowLoginEvent
import app.futured.arkitekt.sample.ui.coroutinesresult.NavigateBackEvent as CoroutinesNavigateBackEvent
import app.futured.arkitekt.sample.ui.detail.NavigateBackEvent as DetailNavigateBackEvent
import app.futured.arkitekt.sample.ui.form.NavigateBackEvent as FormNavigateBackEvent
import app.futured.arkitekt.sample.ui.form.ShowToastEvent as FormShowToastEvent
import app.futured.arkitekt.sample.ui.login.LoginViewModel as LoginFragmentViewModel
import app.futured.arkitekt.sample.ui.login.NavigateBackEvent as LoginNavigateBackEvent


@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel(),
) {


    viewModel.EventsEffect {
        onEvent<ShowDetailEvent> { navController.navigate(ExampleRoute.Detail) }
        onEvent<ShowFormEvent> { navController.navigate(ExampleRoute.Form) }
        onEvent<ShowLoginEvent> { navController.navigate(ExampleRoute.Login) }
        onEvent<ShowBottomSheetEvent> { navController.navigate(ExampleRoute.BottomSheet) }
        onEvent<ShowLoadEvent> { navController.navigate(ExampleRoute.Coroutines) }
    }

    Column(
        modifier = Modifier
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

@Composable
fun DetailScreen(
    navController: NavHostController,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val numberText by viewModel.viewState.stringNumber.observeAsState("")


    viewModel.EventsEffect {
        onEvent<DetailNavigateBackEvent> { navController.popBackStack() }
    }

    Column(
        modifier = Modifier
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

@Composable
fun FormScreen(
    navController: NavHostController,
    viewModel: FormViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val login by viewModel.viewState.login.observeAsState("")
    val password by viewModel.viewState.password.observeAsState("")
    val storedContent by viewModel.viewState.storedContent.observeAsState("")
    val submitEnabled by viewModel.viewState.submitEnabled.observeAsState(false)


    viewModel.EventsEffect {
        onEvent<FormShowToastEvent> { Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show() }
        onEvent<FormNavigateBackEvent> { navController.popBackStack() }
    }

    Column(
        modifier = Modifier
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

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginFragmentViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val name by viewModel.viewState.name.observeAsState("")
    val surname by viewModel.viewState.surname.observeAsState("")
    val fullName by viewModel.viewState.fullName.observeAsState("")
    val showHeader by viewModel.viewState.showHeader.observeAsState(0)


    viewModel.EventsEffect {
        onEvent<ShowToastEvent> { Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show() }
        onEvent<LoginNavigateBackEvent> { navController.popBackStack() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (showHeader == android.view.View.VISIBLE) {
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

@Composable
fun CoroutinesResultScreen(
    navController: NavHostController,
    viewModel: CoroutinesResultViewModel = hiltViewModel(),
) {
    val contentState by viewModel.viewState.contentState.observeAsState(
        app.futured.arkitekt.sample.ui.coroutinesresult.CoroutinesResultViewState.State.IDLE
    )
    val contentDescription by viewModel.viewState.contentStateDescription.observeAsState("")

    viewModel.EventsEffect {
        onEvent<CoroutinesNavigateBackEvent> { navController.popBackStack() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("State: ${contentState ?: "IDLE"}")
        Text(contentDescription)
        Button(onClick = viewModel::onStartLoadingClicked) {
            Text("start")
        }
        Button(onClick = viewModel::onBack) {
            Text("back")
        }
    }
}

@Composable
fun BottomSheetScreen(
    navController: NavHostController,
    viewModel: ExampleViewModel = hiltViewModel(),
) {

    viewModel.EventsEffect {
        onEvent<CloseEvent> { navController.popBackStack() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Bottom sheet content")
        Button(onClick = viewModel::onClose) {
            Text("close")
        }
    }
}
