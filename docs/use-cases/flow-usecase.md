# FlowUseCase

`FlowUseCase<ARGS, T>` is an abstract class for streaming operations that emit multiple values over time.

## Defining a FlowUseCase

Implement the `build` method returning a `Flow<T>`:

```kotlin
class ObserveFormUseCase @Inject constructor(
    private val formStore: FormStore,
) : FlowUseCase<Unit, Pair<String, String>>() {
    override fun build(args: Unit): Flow<Pair<String, String>> =
        formStore.getFormFlow()
}
```

The `FlowUseCase` class has a `job` property that allows tracking and cancellation of the current execution.

## Execution

Execute with callback handlers to react to each emitted value:

```kotlin
observeUserUseCase.execute(Unit) {
    onStart { /* called before flow starts */ }
    onNext { value -> /* called for each emitted value */ }
    onError { error -> /* called on error */ }
    onComplete { /* called when flow completes without error */ }
    disposePrevious(true) // default
}
```

### Callback Options

- **`onStart`** — called before the flow collection begins
- **`onNext`** — called for each value emitted by the flow
- **`onError`** — called when the flow throws an error
- **`onComplete`** — called when the flow completes successfully (without error)
- **`disposePrevious`** — whether to cancel any previous flow collection before starting a new one (default `true`)

## Common Patterns

`FlowUseCase` is commonly used for:

- Observing data stores or repositories
- Watching database changes (e.g., Room or SQLDelight flows)
- Listening to real-time updates (e.g., WebSocket streams)
- Monitoring connectivity or sensor data
