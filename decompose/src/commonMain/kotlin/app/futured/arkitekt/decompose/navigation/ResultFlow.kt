package app.futured.arkitekt.decompose.navigation

import app.futured.arkitekt.decompose.ArkitektComponentContext
import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * A bidirectional Flow that enables navigation components to send results back to their callers.
 *
 * This interface extends [Flow] to provide result collection capabilities while also allowing
 * components to send results through [sendResult]. It's commonly used in Decompose navigation
 * to pass data between navigation destinations.
 *
 * Obtain a durable instance from a component via [ArkitektComponentContext.resultFlow]. Such
 * instances survive configuration changes **and process death** (see [NavigationResultRegistry]).
 *
 * @param T The type of results that flow through this ResultFlow
 *
 * Example usage:
 * ```kotlin
 * // In the parent nav-host
 * private val pickerResult = resultFlow(HomeResultKeys.Picker)
 *
 * init {
 *     lifecycle.doOnCreate {
 *         pickerResult
 *             .onEach { selected -> update(componentState) { copy(selection = selected) } }
 *             .launchIn(lifecycleScope)
 *     }
 * }
 *
 * private fun openPicker() = stackNavigation.push(PickerConfig(HomeResultKeys.Picker))
 *
 * // In the child (picker) component, which received the typed key in its config
 * fun onItemSelected(item: String) = launchWithHandler {
 *     resultFlow(resultKey).sendResult(item)
 *     navigation.back()
 * }
 * ```
 */
@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
interface ResultFlow<T> : Flow<T> {
    /**
     * Sends a result item through this flow.
     *
     * @param item The result item to send to collectors
     */
    suspend fun sendResult(item: T)
}

/**
 * Creates a new in-memory [ResultFlow] instance.
 *
 * @param T The type of results that will flow through this ResultFlow
 * @return A new ResultFlow instance backed by a MutableSharedFlow
 */
@Deprecated(
    message = "Not durable across process death; use ArkitektComponentContext.resultFlow(key) instead.",
    replaceWith = ReplaceWith("resultFlow(key)"),
)
fun <T> ResultFlow(): ResultFlow<T> = InMemoryResultFlow()

/**
 * Returns a durable [ResultFlow] for the given [key], backed by this context's
 * [ArkitektComponentContext.navigationResultRegistry]. Results sent through it survive configuration
 * changes and process death.
 *
 * This is the recommended, collision-resistant entry point: the result type and serializer travel
 * with the [ResultKey].
 */
fun <T : Any> ArkitektComponentContext<*>.resultFlow(key: ResultKey<T>): ResultFlow<T> =
    DurableResultFlow(navigationResultRegistry, key.name, key.serializer)

/**
 * Internal in-memory implementation of [ResultFlow] backed by a [MutableSharedFlow].
 *
 * This implementation does not survive process death. It backs the deprecated no-arg [ResultFlow]
 * factory and is kept only for backwards compatibility.
 *
 * @param T The type of results that flow through this ResultFlow
 * @param backingFlow The underlying MutableSharedFlow used for result propagation
 */
internal class InMemoryResultFlow<T>(
    private val backingFlow: MutableSharedFlow<T> = MutableSharedFlow(),
) : ResultFlow<T> {
    override suspend fun collect(collector: FlowCollector<T>) = backingFlow.collect(collector)

    override suspend fun sendResult(item: T) = backingFlow.emit(item)
}

/**
 * Durable [ResultFlow] that delegates collection and emission to a [NavigationResultRegistry],
 * routed by a stable [key]. Created via [ArkitektComponentContext.resultFlow].
 *
 * @param T The type of results that flow through this ResultFlow
 */
internal class DurableResultFlow<T : Any>(
    private val registry: NavigationResultRegistry,
    private val key: String,
    private val serializer: KSerializer<T>,
) : ResultFlow<T> {
    override suspend fun collect(collector: FlowCollector<T>) = registry.results(key, serializer).collect(collector)

    override suspend fun sendResult(item: T) = registry.send(key, item, serializer)
}

/**
 * Custom serializer for [ResultFlow] that handles serialization and deserialization.
 *
 * This serializer doesn't actually serialize any flow data (returns Unit on serialize),
 * as the flow's runtime state cannot be meaningfully persisted. Instead, it creates
 * a fresh empty [ResultFlow] instance during deserialization.
 *
 * @param T The type of results that flow through the ResultFlow
 * @param dataSerializer The serializer for the data type T (used for descriptor only)
 */
@Deprecated(
    message = "Not durable across process death. Carry a plain String result key in the config and " +
        "use ArkitektComponentContext.resultFlow(key) instead.",
)
internal class ResultFlowSerializer<T>(
    private val dataSerializer: KSerializer<T>,
) : KSerializer<ResultFlow<T>> {
    override val descriptor: SerialDescriptor
        get() = dataSerializer.descriptor

    /**
     * Serializes the ResultFlow. This is a no-op as flow state cannot be serialized.
     *
     * @param encoder The encoder to use for serialization
     * @param value The ResultFlow instance to serialize
     */
    override fun serialize(
        encoder: Encoder,
        value: ResultFlow<T>,
    ) = Unit

    /**
     * Deserializes a ResultFlow by creating a new empty instance.
     *
     * @param decoder The decoder to use for deserialization
     * @return A new empty ResultFlow instance
     */
    @Suppress("DEPRECATION")
    override fun deserialize(decoder: Decoder): ResultFlow<T> = ResultFlow()
}
