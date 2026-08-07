# Quick start (KMP)

This walkthrough builds a simple login feature end to end using Arkitekt with Decompose and Koin in a Kotlin Multiplatform project.

## Define the state

State is a plain data class in common code.

```kotlin
data class LoginState(
    val name: String = "",
    val surname: String = "",
    val fullName: String = "",
    val isLoading: Boolean = false,
)
```

## Define navigation actions

Navigation actions are declared as an interface extending `NavigationActions`. The parent component provides the implementation.

```kotlin
interface LoginNavigation : NavigationActions {
    fun toHome()
}
```

## Define UI events

One-shot events are modeled as a sealed interface extending `UiEvent`.

```kotlin
sealed interface LoginUiEvent : UiEvent {
    data class ShowError(val message: String) : LoginUiEvent
}
```

## Build the component

The component extends `BaseComponent<LoginState, LoginUiEvent>` and is annotated with `@GenerateFactory` for automatic factory generation.

To execute use cases, the component implements `CoroutineScopeOwner` and provides `lifecycleScope` as `useCaseScope`.

```kotlin
@GenerateFactory
class LoginComponent(
    @InjectedParam componentContext: AppComponentContext,
    @InjectedParam navigation: LoginNavigation,
    private val loginUseCase: LoginUseCase,
) : BaseComponent<LoginState, LoginUiEvent>(componentContext, LoginState()),
    CoroutineScopeOwner {

    override val useCaseScope = lifecycleScope
    override val useCaseJobPool = mutableMapOf<Any, Job>()

    val state: StateFlow<LoginState> = componentState

    fun onNameChanged(name: String) {
        update(componentState) { copy(name = name) }
    }

    fun onSurnameChanged(surname: String) {
        update(componentState) { copy(surname = surname) }
    }

    fun logIn() {
        val currentState = componentState.value
        val args = LoginData(
            name = currentState.name,
            surname = currentState.surname,
        )
        loginUseCase.execute(args) {
            onStart {
                update(componentState) { copy(isLoading = true) }
            }
            onSuccess {
                update(componentState) {
                    copy(
                        isLoading = false,
                        fullName = "${args.name} ${args.surname}",
                    )
                }
            }
            onError { error ->
                update(componentState) { copy(isLoading = false) }
                sendUiEvent(LoginUiEvent.ShowError(error.message.orEmpty()))
            }
        }
    }
}
```

## Compose the screen (Android)

On the Android target, the screen composable receives the component, collects its state, and uses `EventsEffect` from the `decompose` module to handle one-shot events.

```kotlin
@Composable
fun LoginScreen(component: LoginComponent) {
    val state by component.state.collectAsState()

    Column {
        TextField(
            value = state.name,
            onValueChange = { component.onNameChanged(it) },
            label = { Text("Name") },
        )
        TextField(
            value = state.surname,
            onValueChange = { component.onSurnameChanged(it) },
            label = { Text("Surname") },
        )

        Button(
            onClick = { component.logIn() },
            enabled = !state.isLoading,
        ) {
            Text("Log In")
        }

        if (state.fullName.isNotEmpty()) {
            Text(text = "Welcome, ${state.fullName}!")
        }
    }

    EventsEffect(component.events) {
        onEvent<LoginUiEvent.ShowError> { event ->
            // Show error snackbar or toast
        }
    }
}
```

## Koin module setup

Register the use case in a Koin module so the generated `LoginComponentFactory` can resolve it automatically.

```kotlin
val loginModule = module {
    factory { LoginUseCase(get()) }
}
```

The `@GenerateFactory` annotation generates a `LoginComponentFactory` that resolves all non-`@InjectedParam` constructor parameters from Koin. You only supply `componentContext` and `navigation` manually when creating the component. See [Factory Generator](../architecture/kmp/factory-generator.md) for details.

---

!!! tip "See it in a full project"
    The [KMP Futured Template](https://github.com/futuredapp/kmp-futured-template) is a complete working app (Android + iOS) built with these same patterns: BaseComponent, Koin, Decompose navigation, and KSP factory generation.
