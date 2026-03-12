# Quick Start — Android

This walkthrough builds a simple login screen end-to-end using Arkitekt with Jetpack Compose and Hilt.

## Define the ViewState

The view state holds all observable UI fields using Compose `mutableStateOf`.

```kotlin
class LoginViewState @Inject constructor() : ViewState {
    val name = mutableStateOf("")
    val surname = mutableStateOf("")
    val fullName = mutableStateOf("")
    val isLoading = mutableStateOf(false)
}
```

## Create a Use Case

Use cases extend `UseCase<Args, Result>` from the `cr-usecases` module. They encapsulate a single unit of business logic.

```kotlin
class LoginUseCase @Inject constructor(
    private val loginStore: LoginStore,
) : UseCase<LoginData, Unit>() {

    override suspend fun build(args: LoginData) {
        loginStore.login(args.name, args.surname)
    }
}
```

## Build the ViewModel

The ViewModel extends `BaseViewModel<LoginViewState>` from the `compose` module. `BaseViewModel` extends `BaseCoreViewModel` and adds `CoroutineScopeOwner`, which is needed to execute use cases with the `execute()` extension.

```kotlin
@HiltViewModel
class LoginViewModel @Inject constructor(
    override val viewState: LoginViewState,
    private val loginUseCase: LoginUseCase,
) : BaseViewModel<LoginViewState>() {

    fun logIn() {
        val args = LoginData(
            name = viewState.name.value,
            surname = viewState.surname.value,
        )
        loginUseCase.execute(args) {
            onStart {
                viewState.isLoading.value = true
            }
            onSuccess {
                viewState.isLoading.value = false
                viewState.fullName.value = "${args.name} ${args.surname}"
            }
            onError { error ->
                viewState.isLoading.value = false
                sendEvent(LoginEvent.ShowError(error.message.orEmpty()))
            }
        }
    }
}
```

## Compose the Screen

The screen composable obtains the ViewModel via `hiltViewModel()`, observes state, and calls ViewModel functions on user interaction.

```kotlin
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val name by viewModel.viewState.name
    val surname by viewModel.viewState.surname
    val fullName by viewModel.viewState.fullName
    val isLoading by viewModel.viewState.isLoading

    Column {
        TextField(
            value = name,
            onValueChange = { viewModel.viewState.name.value = it },
            label = { Text("Name") },
        )
        TextField(
            value = surname,
            onValueChange = { viewModel.viewState.surname.value = it },
            label = { Text("Surname") },
        )

        Button(
            onClick = { viewModel.logIn() },
            enabled = !isLoading,
        ) {
            Text("Log In")
        }

        if (fullName.isNotEmpty()) {
            Text(text = "Welcome, $fullName!")
        }
    }

    viewModel.EventsEffect {
        onEvent<LoginEvent.ShowError> { event ->
            // Show error snackbar or toast
        }
    }
}
```

Use `EventsEffect` to handle one-shot events dispatched by the ViewModel via `sendEvent()`. Events are collected only while the composable is in the composition.
