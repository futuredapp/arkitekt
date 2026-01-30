package app.futured.arkitekt.decompose.presentation

import com.arkivanov.decompose.GenericComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Base class for all Components in architecture.
 * The BaseComponent allows implementation of stateful screen / navigation host Components which perform some presentation logic.
 *
 * @param VS The type of the component state.
 * @param E The type of the UI events.
 * @param componentContext The context of the component.
 * @param defaultState The default Component state.
 */
abstract class BaseComponent<VS : Any, E : Any>(
    componentContext: GenericComponentContext<*>,
    private val defaultState: VS,
) {

    /**
     * An internal state of the component of type [VS].
     */
    protected val componentState: MutableStateFlow<VS> = MutableStateFlow(defaultState)

    // region Lifecycle

    /**
     * The coroutine scope tied to the lifecycle of the component.
     * It is cancelled when the component is destroyed.
     */
    protected val componentCoroutineScope = MainScope().also { scope ->
        componentContext.lifecycle.doOnDestroy { scope.cancel() }
    }

    /**
     * Converts a [Flow] of component states to a [StateFlow].
     *
     * @param started The [SharingStarted] strategy for the [StateFlow].
     * @return A [StateFlow] emitting the values of the [Flow].
     */
    protected fun Flow<VS>.asStateFlow(started: SharingStarted = SharingStarted.Lazily) =
        stateIn(componentCoroutineScope, started, defaultState)

    // endregion

    // region UI events

    /**
     * Channel for sending UI events.
     */
    private val uiEventChannel = Channel<E>(Channel.BUFFERED)

    /**
     * Flow of UI events.
     */
    val events: Flow<E> = uiEventChannel
        .receiveAsFlow()
        .shareIn(componentCoroutineScope, SharingStarted.Lazily)

    // endregion

    // region Implementation API

    /**
     * Sends a UI event.
     *
     * @param event The event to send.
     */
    protected fun sendUiEvent(event: E) {
        componentCoroutineScope.launch {
            uiEventChannel.send(event)
        }
    }

    // endregion
}
