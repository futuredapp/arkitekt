package app.futured.arkitekt.sample.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import app.futured.arkitekt.core.EventsEffect
import app.futured.arkitekt.core.onEvent
import app.futured.arkitekt.sample.tools.ToastCreator
import app.futured.arkitekt.sample.ui.bottomsheet.CloseEvent
import app.futured.arkitekt.sample.ui.bottomsheet.ExampleViewModel
import app.futured.arkitekt.sample.ui.bottomsheet.ExampleViewModelFactory
import app.futured.arkitekt.sample.ui.coroutinesresult.NavigateBackEvent as CoroutinesNavigateBackEvent
import app.futured.arkitekt.sample.ui.coroutinesresult.CoroutinesResultViewModel
import app.futured.arkitekt.sample.ui.coroutinesresult.CoroutinesResultViewModelFactory
import app.futured.arkitekt.sample.ui.detail.DetailViewModel
import app.futured.arkitekt.sample.ui.detail.DetailViewModelFactory
import app.futured.arkitekt.sample.ui.detail.NavigateBackEvent as DetailNavigateBackEvent
import app.futured.arkitekt.sample.ui.form.FormViewModel
import app.futured.arkitekt.sample.ui.form.FormViewModelFactory
import app.futured.arkitekt.sample.ui.form.NavigateBackEvent as FormNavigateBackEvent
import app.futured.arkitekt.sample.ui.form.ShowToastEvent as FormShowToastEvent
import app.futured.arkitekt.sample.ui.login.activity.LoginViewModel as LoginActivityViewModel
import app.futured.arkitekt.sample.ui.login.activity.LoginViewModelFactory as LoginActivityViewModelFactory
import app.futured.arkitekt.sample.ui.login.activity.ShowToastEvent as LoginActivityShowToastEvent
import app.futured.arkitekt.sample.ui.login.fragment.LoginViewModel as LoginFragmentViewModel
import app.futured.arkitekt.sample.ui.login.fragment.LoginViewModelFactory as LoginFragmentViewModelFactory
import app.futured.arkitekt.sample.ui.login.fragment.NavigateBackEvent as LoginNavigateBackEvent
import app.futured.arkitekt.sample.ui.login.fragment.NotifyActivityEvent
import app.futured.arkitekt.sample.ui.main.MainViewModel
import app.futured.arkitekt.sample.ui.main.MainViewModelFactory
import app.futured.arkitekt.sample.ui.main.ShowBottomSheetEvent
import app.futured.arkitekt.sample.ui.main.ShowDetailEvent
import app.futured.arkitekt.sample.ui.main.ShowFormEvent
import app.futured.arkitekt.sample.ui.main.ShowLoadEvent
import app.futured.arkitekt.sample.ui.main.ShowLoginEvent

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModelFactory: MainViewModelFactory,
) {
    val viewModel: MainViewModel = viewModel(factory = viewModelFactory)

    viewModel.EventsEffect {
        onEvent<ShowDetailEvent> { navController.navigate("detail") }
        onEvent<ShowFormEvent> { navController.navigate("form") }
        onEvent<ShowLoginEvent> { navController.navigate("login") }
        onEvent<ShowBottomSheetEvent> { navController.navigate("bottomsheet") }
        onEvent<ShowLoadEvent> { navController.navigate("coroutines") }
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
    viewModelFactory: DetailViewModelFactory,
) {
    val viewModel: DetailViewModel = viewModel(factory = viewModelFactory)
    val numberText by viewModel.viewState.stringNumber.observeAsState("")

    LaunchedEffect(viewModel) {
        viewModel.onStart()
    }

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
    viewModelFactory: FormViewModelFactory,
    toastCreator: ToastCreator,
) {
    val viewModel: FormViewModel = viewModel(factory = viewModelFactory)
    val context = LocalContext.current
    val login by viewModel.viewState.login.observeAsState("")
    val password by viewModel.viewState.password.observeAsState("")
    val storedContent by viewModel.viewState.storedContent.observeAsState("")
    val submitEnabled by viewModel.viewState.submitEnabled.observeAsState(false)

    LaunchedEffect(viewModel) {
        viewModel.onStart()
    }

    viewModel.EventsEffect {
        onEvent<FormShowToastEvent> { toastCreator.showToast(context, it.message) }
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
    activityViewModelFactory: LoginActivityViewModelFactory,
    viewModelFactory: LoginFragmentViewModelFactory,
    toastCreator: ToastCreator,
) {
    val context = LocalContext.current
    val activityViewModel: LoginActivityViewModel = viewModel(factory = activityViewModelFactory)
    val viewModel: LoginFragmentViewModel = viewModel(factory = viewModelFactory)
    val name by viewModel.viewState.name.observeAsState("")
    val surname by viewModel.viewState.surname.observeAsState("")
    val fullName by viewModel.viewState.fullName.observeAsState("")
    val showHeader by viewModel.viewState.showHeader.observeAsState(0)

    LaunchedEffect(viewModel) {
        viewModel.onStart()
    }

    viewModel.EventsEffect {
        onEvent<NotifyActivityEvent> { activityViewModel.sendToastEvent(it.message) }
        onEvent<LoginNavigateBackEvent> { navController.popBackStack() }
    }

    activityViewModel.EventsEffect {
        onEvent<LoginActivityShowToastEvent> { toastCreator.showToast(context, it.message) }
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
    viewModelFactory: CoroutinesResultViewModelFactory,
) {
    val viewModel: CoroutinesResultViewModel = viewModel(factory = viewModelFactory)
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
    viewModelFactory: ExampleViewModelFactory,
) {
    val viewModel: ExampleViewModel = viewModel(factory = viewModelFactory)

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
