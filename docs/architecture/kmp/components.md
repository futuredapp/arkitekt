# Components — KMP

The `decompose` module provides the building blocks for KMP presentation logic, built on top of the [Decompose](https://arkivanov.github.io/Decompose/) library.

## BaseComponent

`BaseComponent<VS : Any, E : Any>` is the main base class for components, provided by the Arkitekt library.

Constructor parameters:

- `componentContext: GenericComponentContext<*>` — Decompose component context
- `defaultState: VS` — initial state value
- `lifecycleScope: CoroutineScope` — scope tied to the component lifecycle (defaults to `MainScope()`; inject your own scope in tests)

Key members:

- `componentState: MutableStateFlow<VS>` — protected mutable state
- `lifecycleScope: CoroutineScope` — public, open; cancelled automatically when the component is destroyed
- `events: Flow<E>` — single-subscriber flow of one-shot UI events backed by a buffered `Channel`
- `sendUiEvent(event: E)` — protected function to emit an event
- `fun Flow<VS>.asStateFlow(started): StateFlow<VS>` — protected helper to convert a `Flow` to a `StateFlow` within the component scope

### Events semantics

`events` is backed by a `Channel.BUFFERED` (capacity 64). Events emitted before a collector subscribes are buffered and delivered when the collector starts. Because it is a channel-based flow, **each event is delivered exactly once to a single collector**. If multiple collectors subscribe, only one receives each event. On configuration change (collector cancelled and restarted), any undelivered buffered events are drained to the new collector.

### Lifecycle

The component lifecycle is driven by Decompose's `LifecycleOwner`. The recommended place to start work is inside a `doOnStart` / `doOnCreate` block:

```kotlin
init {
    lifecycle.doOnStart {
        observeUserUseCase.execute(Unit) {
            onNext { update(componentState) { copy(userName = it.name) } }
        }
    }
}
```

When the lifecycle is **destroyed**, `lifecycleScope` is cancelled and the `events` channel is closed — any subsequent `sendUiEvent` calls are no-ops.

## ArkitektComponentContext

`ArkitektComponentContext<T>` is a marker interface extending Decompose's `GenericComponentContext<T>`. It serves as the base for your project's own component context type.

The recommended pattern is to define an `AppComponentContext` interface in your project that self-references the type parameter, and a `DefaultAppComponentContext` implementation that delegates to a standard Decompose `ComponentContext`:

```kotlin
// commonMain — define once per project
interface AppComponentContext : ArkitektComponentContext<AppComponentContext>

class DefaultAppComponentContext(componentContext: ComponentContext) :
    AppComponentContext,
    LifecycleOwner by componentContext,
    StateKeeperOwner by componentContext,
    InstanceKeeperOwner by componentContext,
    BackHandlerOwner by componentContext {

    override val componentContextFactory: ComponentContextFactory<AppComponentContext> =
        ComponentContextFactory { lifecycle, stateKeeper, instanceKeeper, backHandler ->
            val ctx = componentContext.componentContextFactory(lifecycle, stateKeeper, instanceKeeper, backHandler)
            DefaultAppComponentContext(ctx)
        }
}
```

### Creating the Root Component

On Android, Arkitekt is designed to be used with Decompose's [`retainedComponent`](https://arkivanov.github.io/Decompose/component/instance-retaining/#retained-components) to create the root component. This retains the entire component tree across configuration changes, similar to AndroidX `ViewModel`:

```kotlin
// Android Activity — onCreate
val rootComponent = retainedComponent { componentContext ->
    RootNavHostComponent(DefaultAppComponentContext(componentContext))
}
```

!!! note
    `retainedComponent` is an Android-specific extension on `ComponentActivity`. It should be called once in `onCreate`. On iOS, create a `DefaultComponentContext` with the application lifecycle and pass it directly.

Child components always receive their `AppComponentContext` from the parent — they never construct it themselves. Decompose creates a scoped child context automatically when you call `childStack`, `childSlot`, or similar APIs.

## AppComponent

It is recommended to define an application-level base class that extends `BaseComponent` and delegates `AppComponentContext`:

```kotlin
abstract class AppComponent<VS : Any, E : Any>(
    componentContext: AppComponentContext,
    defaultState: VS,
) : BaseComponent<VS, E>(componentContext, defaultState),
    AppComponentContext by componentContext
```

Because `AppComponent` delegates `AppComponentContext`, all context services — `lifecycle`, `stateKeeper`, `instanceKeeper`, `backHandler` — are directly accessible on every component without going through `componentContext`. This is the recommended approach for both screen components and nav-host components.

This pattern also makes it easy to integrate with use cases: implement `CoroutineScopeOwner` directly on `AppComponent` to make use case execution available in every component by default — see [Use Cases](../../use-cases/overview.md).

## Koin Factory Generation

Use the `@GenerateFactory` annotation to generate Koin dependency injection factories for your components. See the [Factory Generator](factory-generator.md) page for full details and KSP configuration.

## Complete Example

```kotlin
@GenerateFactory
class HomeComponent(
    @InjectedParam componentContext: AppComponentContext,
    @InjectedParam navigation: HomeNavigation,
    private val observeUserUseCase: ObserveUserUseCase,
) : AppComponent<HomeState, HomeUiEvent>(componentContext, HomeState()),
    CoroutineScopeOwner {

    override val useCaseScope = lifecycleScope
    override val useCaseJobPool = mutableMapOf<Any, Job>()

    val state: StateFlow<HomeState> = componentState

    init {
        lifecycle.doOnStart {
            observeUserUseCase.execute(Unit) {
                onNext { update(componentState) { copy(userName = it.name) } }
                onError { sendUiEvent(HomeUiEvent.ShowError(it.message.orEmpty())) }
            }
        }
    }

    fun onDetailClick() {
        navigation.toDetail()
    }
}
```

!!! note
    `lifecycle.doOnStart` is the recommended place to trigger work that should run each time the component becomes active. The `lifecycle` property is available implicitly because `AppComponent` delegates `AppComponentContext`. This also makes components easy to unit test — you can control the lifecycle externally and verify behavior at each stage.

!!! note
    `BaseComponent` does **not** implement `CoroutineScopeOwner` directly. To execute use cases, implement `CoroutineScopeOwner` in your component and set `useCaseScope = lifecycleScope`.

## Example Application

For a full KMP application using all the recommended patterns described in this documentation, see the [KMP Futured Template](https://github.com/futuredapp/kmp-futured-template) repository.
