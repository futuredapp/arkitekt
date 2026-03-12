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

Update state by replacing the value directly:

```kotlin
componentState.value = componentState.value.copy(title = "new")
```

Or use the `update` helper from `app.futured.arkitekt.decompose.ext`:

```kotlin
update(componentState) { copy(title = "new") }
```

## Exposing State

Expose state to the UI as a read-only `StateFlow`:

```kotlin
class HomeComponent(
    componentContext: AppComponentContext,
) : BaseComponent<HomeState, HomeUiEvent>(componentContext, HomeState()) {

    val state: StateFlow<HomeState> = componentState
}
```

## asStateFlow Helper

`BaseComponent` provides an `asStateFlow()` extension on `Flow<VS>` that converts a `Flow` to a `StateFlow` within the component's coroutine scope. This is useful when you want to derive state from another flow:

```kotlin
val derivedState: StateFlow<DerivedState> = someFlow
    .map { DerivedState(it) }
    .asStateFlow(SharingStarted.WhileSubscribed(5_000))
```

## Observing State in Compose

```kotlin
@Composable
fun HomeScreen(component: HomeComponent) {
    val state by component.state.collectAsState()

    if (state.isLoading) {
        CircularProgressIndicator()
    } else {
        Text(state.title)
    }
}
```
