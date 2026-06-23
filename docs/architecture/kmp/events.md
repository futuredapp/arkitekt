# Events — KMP

Events are one-shot messages sent from a Component to the UI layer.

## Defining Events

Define events as a sealed interface extending `UiEvent`:

```kotlin
sealed interface HomeUiEvent : UiEvent {
    data object ShowToast : HomeUiEvent
    data class ShowError(val message: String) : HomeUiEvent
}
```

## Sending Events

Send an event from the Component:

```kotlin
sendUiEvent(HomeUiEvent.ShowToast)
```

## Collecting Events in Compose

Use `EventsEffect` from the **decompose** module:

```kotlin
@Composable
fun HomeScreen(component: HomeComponent) {
    EventsEffect(component.events) {
        onEvent<HomeUiEvent.ShowToast> { /* show toast */ }
        onEvent<HomeUiEvent.ShowError> { showSnackbar(it.message) }
    }
}
```

!!! note
    Unlike the Android path where `EventsEffect` is an extension on `BaseCoreViewModel`, the KMP variant takes the `eventsFlow` parameter directly (e.g. `component.events`).
