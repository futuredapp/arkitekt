# State Management — Android

## ViewState

`ViewState` is a marker interface. Implement it with Compose `mutableStateOf` fields so the UI recomposes automatically when values change.

```kotlin
class HomeViewState @Inject constructor() : ViewState {
    var title by mutableStateOf("")
    var isLoading by mutableStateOf(false)
}
```

`ViewState` is injected into the ViewModel and acts as the **single source of truth** for the screen's state.

```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(
    override val viewState: HomeViewState,
) : BaseViewModel<HomeViewState>() {

    fun onNameLoaded(name: String) {
        viewState.title = name
    }
}
```

You can also use `derivedStateOf` for computed properties:

```kotlin
class FormViewState @Inject constructor() : ViewState {
    var login by mutableStateOf("")
    var password by mutableStateOf("")
    val isFormValid by derivedStateOf {
        login.isNotBlank() && password.isNotBlank()
    }
}
```

## Observing State in Compose

Use Compose state delegation to observe ViewState fields:

```kotlin
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val title = viewModel.viewState.title
    val isLoading = viewModel.viewState.isLoading

    if (isLoading) {
        CircularProgressIndicator()
    } else {
        Text(title)
    }
}
```

## StateFlow Alternative

If you prefer `StateFlow` over Compose `mutableStateOf`, you can expose a `StateFlow` from the ViewModel and collect it in the Composable with `collectAsState()`:

```kotlin
class HomeViewState : ViewState {
    val title = MutableStateFlow("")
}

// In Composable
val title by viewModel.viewState.title.collectAsState()
```
