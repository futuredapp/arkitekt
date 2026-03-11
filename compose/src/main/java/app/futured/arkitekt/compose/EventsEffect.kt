package app.futured.arkitekt.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import app.futured.arkitekt.core.BaseCoreViewModel
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.core.event.Event

/**
 * When [EventsEffect] enters composition, it will start observing the event flow from its ViewModel.
 * Each event sent from ViewModel goes through [observer] lambda which can be used to react to a specific event.
 * Use the [onEvent] function to filter out the event you are interested in.
 *
 * @param observer Event receiver lambda.
 */
@Composable
fun <VS : ViewState> BaseCoreViewModel<VS>.EventsEffect(
    observer: suspend Event<VS>.() -> Unit,
) {
    LaunchedEffect(this) {
        events.collect {
            observer(it)
        }
    }
}

inline fun <reified E : Event<*>> Event<*>.onEvent(action: (E) -> Unit) {
    if (this is E) {
        action(this)
    }
}
