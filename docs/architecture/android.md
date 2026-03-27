# Architecture — Android

## Components

Arkitekt provides two ViewModel base classes for the Android / Compose path.

### BaseCoreViewModel

`BaseCoreViewModel<VS : ViewState>` from the **core** module is an abstract `ViewModel` that gives you:

- `viewState: VS` — the screen's view state instance
- `events: Flow<Event<VS>>` — a channel-backed flow of one-shot events
- `sendEvent(event)` — sends an event to the UI

Use this when you do **not** need to execute use cases.

```kotlin
class SimpleViewModel(
    override val viewState: SimpleViewState,
) : BaseCoreViewModel<SimpleViewState>()
```

### BaseViewModel

`BaseViewModel<VS : ViewState>` from the **compose** module extends `BaseCoreViewModel` and implements `CoroutineScopeOwner` (from `cr-usecases`). Use this when you need to execute use cases.

```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(
    override val viewState: HomeViewState,
    private val getProfileUseCase: GetProfileUseCase,
) : BaseViewModel<HomeViewState>() {

    init {
        getProfileUseCase.execute(Unit) {
            onSuccess { viewState.userName = it.name }
            onError { sendEvent(ShowErrorEvent(it.message)) }
        }
    }
}
```

### CoroutineScopeOwner

`CoroutineScopeOwner` (from `cr-usecases`) is implemented by `BaseViewModel` and provides:

- `coroutineScope` — the scope for executing use cases, backed by `viewModelScope`
- `getWorkerDispatcher()` — returns `Dispatchers.IO` by default; override for testing
- `launchWithHandler {}` — launches a coroutine with try-catch that calls `defaultErrorHandler` and logs to `UseCaseErrorHandler.globalOnErrorLogger`
- `defaultErrorHandler(exception)` — by default rethrows the exception; override to customize error handling

### Obtaining the ViewModel in Compose

```kotlin
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val title = viewModel.viewState.title
    // ...
}
```

## State Management

### ViewState

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

### Observing State in Compose

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

### StateFlow Alternative

If you prefer `StateFlow` over Compose `mutableStateOf`, you can expose a `StateFlow` from the ViewModel and collect it in the Composable with `collectAsState()`:

```kotlin
class HomeViewState : ViewState {
    val title = MutableStateFlow("")
}

// In Composable
val title by viewModel.viewState.title.collectAsState()
```

## Events

Events are one-shot messages sent from a ViewModel to a Composable. They are backed by a `Channel`, which guarantees single delivery even during screen rotation.

### Defining Events

Define events as a sealed class extending `Event<VS>`:

```kotlin
sealed class HomeEvent : Event<HomeViewState>() {
    object ShowDetailEvent : HomeEvent()
    data class ShowMessageEvent(val message: String) : HomeEvent()
}
```

### Sending Events

Send an event from the ViewModel:

```kotlin
sendEvent(ShowDetailEvent)
```

### Collecting Events in Compose

Use `EventsEffect` to collect events in a Composable:

```kotlin
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    viewModel.EventsEffect {
        onEvent<ShowDetailEvent> { /* navigate to detail */ }
        onEvent<ShowMessageEvent> { showSnackbar(it.message) }
    }
}
```

`EventsEffect` is an extension function on `BaseCoreViewModel` from the **compose** module. It launches a coroutine that collects events for the lifetime of the Composable.

`onEvent<E>` is a type-safe filter — it checks whether the received event is of type `E` and executes the lambda only when it matches.

## Navigation

Arkitekt does not provide its own navigation library for the Android path. Use Jetpack Navigation with Compose or any other navigation solution you prefer.

### Example with Jetpack Navigation

```kotlin
@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(onNavigateToDetail = { navController.navigate("detail/$it") })
        }
        composable("detail/{id}") { backStackEntry ->
            DetailScreen(id = backStackEntry.arguments?.getString("id").orEmpty())
        }
    }
}
```

For a working implementation, see the **example** module in the [Arkitekt GitHub repository](https://github.com/futuredapp/arkitekt).
