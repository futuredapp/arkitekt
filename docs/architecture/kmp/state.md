# State Management — KMP

## State as a Data Class

In the KMP / Decompose path, state is a plain data class — no marker interface required.

```kotlin
data class HomeState(
    val title: String = "",
    val isLoading: Boolean = false,
)
```

## Holding and Updating State

`BaseComponent` provides `componentState: MutableStateFlow<VS>` to hold the component's state.

Update state using the built-in `MutableStateFlow.update` extension:

```kotlin
componentState.update { copy(title = "new") }
```

Or use the equivalent Arkitekt helper from `app.futured.arkitekt.decompose.ext`:

```kotlin
update(componentState) { copy(title = "new") }
```

## Exposing State

Define an interface that exposes only what the UI needs, then implement it in your component:

```kotlin
interface HomeScreen {
    val state: StateFlow<HomeState>
}

class HomeComponent(
    componentContext: AppComponentContext,
) : HomeScreen, BaseComponent<HomeState, HomeUiEvent>(componentContext, HomeState()) {

    override val state: StateFlow<HomeState> = componentState
}
```

This keeps the UI decoupled from the concrete component class, which makes Compose previews and tests straightforward — just provide a fake `HomeScreen` implementation.

## Observing State in Compose

Pass the interface type to your composable, not the concrete component:

```kotlin
@Composable
fun HomeScreen(component: HomeScreen) {
    val state by component.state.collectAsState()

    if (state.isLoading) {
        CircularProgressIndicator()
    } else {
        Text(state.title)
    }
}
```

## Value / Flow Conversions

Arkitekt provides utility extensions for bridging Decompose `Value` and Kotlin Coroutines `StateFlow` / `Flow` APIs.

### `Value<T>.asStateFlow()`

Converts a Decompose `Value` to a Kotlin `StateFlow`:

```kotlin
val state: StateFlow<HomeState> = decomposeValue.asStateFlow()
```

### `Flow<T>.collectAsValue(initial, coroutineScope)`

Converts a Kotlin `Flow` to a Decompose `Value`:

```kotlin
val decomposeValue: Value<HomeState> = stateFlow.collectAsValue(
    initial = HomeState(),
    coroutineScope = lifecycleScope,
)
```
