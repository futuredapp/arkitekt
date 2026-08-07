# Quick start (Android)

This walkthrough builds a simple login screen end to end using Arkitekt with Jetpack Compose and Hilt.

## Define the ViewState

The view state holds all observable UI fields using Compose `mutableStateOf`.

```kotlin
class LoginViewState @Inject constructor() : ViewState {
    var name by mutableStateOf("")
    var surname by mutableStateOf("")
    var fullName by mutableStateOf("")
    var isLoading by mutableStateOf(false)
}
```

## Create a use case

Use cases extend `UseCase<Args, Result>` from the `cr-usecases` module. They encapsulate a single unit of business logic.

```kotlin
class LoginUseCase @Inject constructor(
    private val loginStore: LoginStore,
) : UseCase<LoginData, Unit> {

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
                viewState.isLoading = true
            }
            onSuccess {
                viewState.isLoading = false
                viewState.fullName = "${args.name} ${args.surname}"
            }
            onError { error ->
                viewState.isLoading = false
                sendEvent(LoginEvent.ShowError(error.message.orEmpty()))
            }
        }
    }
}
```

## Compose the screen

The screen composable obtains the ViewModel via `hiltViewModel()`, observes state, and calls ViewModel functions on user interaction.

```kotlin
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val name = viewModel.viewState.name
    val surname = viewModel.viewState.surname
    val fullName = viewModel.viewState.fullName
    val isLoading = viewModel.viewState.isLoading

    Column {
        TextField(
            value = name,
            onValueChange = { viewModel.viewState.name = it },
            label = { Text("Name") },
        )
        TextField(
            value = surname,
            onValueChange = { viewModel.viewState.surname = it },
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

---

!!! tip "See it in a full project"
    This repo's [`example`](https://github.com/futuredapp/arkitekt/tree/5.x/example) module shows these patterns in a minimal Android app. For a complete project setup, see the [Android Project Template](https://github.com/futuredapp/android-project-template-compose).
