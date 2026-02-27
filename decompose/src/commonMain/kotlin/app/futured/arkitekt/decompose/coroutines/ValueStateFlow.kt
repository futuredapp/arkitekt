package app.futured.arkitekt.decompose.coroutines

import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * A [StateFlow] implementation that wraps a Decompose Value.
 *
 * @param T The type of the value.
 * @property decomposeValue The Decompose Value to be wrapped.
 */
internal class ValueStateFlow<out T : Any>(private val decomposeValue: Value<T>) : StateFlow<T> {

    override val value: T
        get() = decomposeValue.value

    override val replayCache: List<T> = listOf(value)

    override suspend fun collect(collector: FlowCollector<T>): Nothing {
        val relay = MutableStateFlow(decomposeValue.value)
        val cancellation = decomposeValue.subscribe { value -> relay.value = value }

        try {
            relay.collect(collector)
        } finally {
            cancellation.cancel()
        }
    }
}
