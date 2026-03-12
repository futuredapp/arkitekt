# Components — Android

Arkitekt provides two ViewModel base classes for the Android / Compose path.

## BaseCoreViewModel

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

## BaseViewModel

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

## CoroutineScopeOwner

`CoroutineScopeOwner` (from `cr-usecases`) is implemented by `BaseViewModel` and provides:

- `coroutineScope` — the scope for executing use cases, backed by `viewModelScope`
- `getWorkerDispatcher()` — returns `Dispatchers.IO` by default; override for testing
- `launchWithHandler {}` — launches a coroutine with try-catch that calls `defaultErrorHandler` and logs to `UseCaseErrorHandler.globalOnErrorLogger`
- `defaultErrorHandler(exception)` — by default rethrows the exception; override to customize error handling

## Obtaining the ViewModel in Compose

```kotlin
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val title = viewModel.viewState.title
    // ...
}
```
