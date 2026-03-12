# Events — Android

Events are one-shot messages sent from a ViewModel to a Composable. They are backed by a `Channel`, which guarantees single delivery even during screen rotation.

## Defining Events

Define events as a sealed class extending `Event<VS>`:

```kotlin
sealed class HomeEvent : Event<HomeViewState>() {
    object ShowDetailEvent : HomeEvent()
    data class ShowMessageEvent(val message: String) : HomeEvent()
}
```

## Sending Events

Send an event from the ViewModel:

```kotlin
sendEvent(ShowDetailEvent)
```

## Collecting Events in Compose

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
