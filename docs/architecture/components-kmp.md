# Components — KMP

The `decompose` module provides the building blocks for KMP presentation logic.

## BaseComponent

`BaseComponent<VS : Any, E : Any>` is the main base class for components.

Constructor parameters:

- `componentContext: GenericComponentContext<*>` — Decompose component context
- `defaultState: VS` — initial state value

Key members:

- `componentState: MutableStateFlow<VS>` — protected mutable state
- `componentCoroutineScope` — a `MainScope()` tied to the component lifecycle (cancelled on destroy)
- `events: Flow<E>` — flow of one-shot UI events backed by a `Channel`
- `sendUiEvent(event: E)` — protected function to emit an event
- `fun Flow<VS>.asStateFlow(started): StateFlow<VS>` — protected helper to convert a `Flow` to a `StateFlow` within the component scope

## Complete Example

```kotlin
class HomeComponent(
    componentContext: AppComponentContext,
    private val observeUserUseCase: ObserveUserUseCase,
    private val navigation: HomeNavigation,
) : BaseComponent<HomeState, HomeUiEvent>(componentContext, HomeState()),
    CoroutineScopeOwner {

    override val coroutineScope = componentCoroutineScope

    val state: StateFlow<HomeState> = componentState

    init {
        observeUserUseCase.execute(Unit) {
            onNext { componentState.value = componentState.value.copy(userName = it.name) }
            onError { sendUiEvent(HomeUiEvent.ShowError(it.message.orEmpty())) }
        }
    }

    fun onDetailClick() {
        navigation.toDetail()
    }
}
```

!!! note
    `BaseComponent` does **not** implement `CoroutineScopeOwner` directly. To execute use cases, implement `CoroutineScopeOwner` in your component and set `coroutineScope` to `componentCoroutineScope`.

## Supporting Types

- **`ArkitektComponentContext<T>`** — interface extending `GenericComponentContext<T>`. Use it as the base for your app's component context type.
- **`UiEvent`** — marker interface for one-shot events.

## Koin Factory Generation

Use the `@GenerateFactory` annotation to generate Koin factory functions for your components. See the [Factory Generator](../kmp/factory-generator.md) section for details.
