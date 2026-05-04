package app.futured.arkitekt.decompose.presentation

import com.arkivanov.decompose.GenericComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
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
 * @param lifecycleScope The coroutine scope tied to the lifecycle of the component.
 * It will be automatically canceled when component's lifecycle is destroyed.
 * You can inject your own scope for use in tests.
 */
abstract class BaseComponent<VS : Any, E : Any>(
    componentContext: GenericComponentContext<*>,
    private val defaultState: VS,
    open val lifecycleScope: CoroutineScope = MainScope(),
) {

    init {
        componentContext.lifecycle.doOnDestroy {
            eventChannel.close()
            lifecycleScope.cancel()
        }
    }

    /**
     * An internal state of the component of type [VS].
     */
    protected val componentState: MutableStateFlow<VS> = MutableStateFlow(defaultState)

    /**
     * Converts a [Flow] of component states to a [StateFlow].
     *
     * @param started The [SharingStarted] strategy for the [StateFlow].
     * @return A [StateFlow] emitting the values of the [Flow].
     */
    protected fun Flow<VS>.asStateFlow(started: SharingStarted = SharingStarted.Lazily) =
        stateIn(lifecycleScope, started, defaultState)

    // region UI events

    /**
     * Backing channel for UI events.
     *
     * Uses [Channel.BUFFERED] (capacity 64 by default) so producers don't
     * have to suspend under normal circumstances, while still retaining
     * events for a collector that hasn't subscribed yet.
     */
    private val eventChannel = Channel<E>(Channel.BUFFERED)

    /**
     * Stream of one-shot UI events.
     *
     * Intended to be collected by a **single** observer (typically the
     * current screen). Each emitted event is received exactly once — on
     * re-subscription after a configuration change, any buffered events
     * are delivered to the new collector.
     */
    val events: Flow<E> = eventChannel.receiveAsFlow()

    // endregion

    // region Implementation API

    /**
     * Sends a UI event.
     *
     * @param event The event to send.
     */
    protected fun sendUiEvent(event: E) {
        lifecycleScope.launch {
            eventChannel.send(event)
        }
    }

    // endregion
}
