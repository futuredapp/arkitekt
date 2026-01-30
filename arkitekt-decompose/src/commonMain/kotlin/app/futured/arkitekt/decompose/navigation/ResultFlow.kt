package app.futured.arkitekt.decompose.navigation

import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Creates a new [ResultFlow] instance.
 *
 * @param T The type of results that will flow through this ResultFlow
 * @return A new ResultFlow instance backed by a MutableSharedFlow
 */
fun <T> ResultFlow(): ResultFlow<T> = ResultFlowImpl()

/**
 * A bidirectional Flow that enables navigation components to send results back to their callers.
 *
 * This interface extends [Flow] to provide result collection capabilities while also allowing
 * components to send results through [sendResult]. It's commonly used in Decompose navigation
 * to pass data between navigation destinations.
 *
 * The ResultFlow is serializable and will be recreated as an empty flow during deserialization,
 * making it suitable for use in navigation configurations that need to be preserved.
 *
 * @param T The type of results that flow through this ResultFlow
 *
 * Example usage:
 * ```kotlin
 * // In parent component
 * val resultFlow = ResultFlow<String>()
 *
 * // Pass to child component
 * childComponent.navigateToChild(resultFlow)
 *
 * // Collect results
 * resultFlow.collect { result ->
 *     // Handle result from child
 * }
 *
 * // In child component
 * resultFlow.sendResult("some result")
 * ```
 */
@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
@Serializable(with = ResultFlowSerializer::class)
interface ResultFlow<T> : Flow<T> {
    /**
     * Sends a result item through this flow.
     *
     * @param item The result item to send to collectors
     */
    suspend fun sendResult(item: T)
}

/**
 * Internal implementation of [ResultFlow] backed by a [MutableSharedFlow].
 *
 * This implementation delegates all Flow operations to the backing MutableSharedFlow,
 * providing both collection and emission capabilities.
 *
 * @param T The type of results that flow through this ResultFlow
 * @param backingFlow The underlying MutableSharedFlow used for result propagation
 */
internal class ResultFlowImpl<T>(private val backingFlow: MutableSharedFlow<T> = MutableSharedFlow()) : ResultFlow<T> {

    override suspend fun collect(collector: FlowCollector<T>) = backingFlow.collect(collector)

    override suspend fun sendResult(item: T) = backingFlow.emit(item)
}

/**
 * Custom serializer for [ResultFlow] that handles serialization and deserialization.
 *
 * This serializer doesn't actually serialize any flow data (returns Unit on serialize),
 * as the flow's runtime state cannot be meaningfully persisted. Instead, it creates
 * a fresh empty [ResultFlow] instance during deserialization.
 *
 * This approach allows ResultFlow to be used in serializable navigation configurations
 * while maintaining proper lifecycle semantics - each deserialized instance gets a
 * new, independent flow.
 *
 * @param T The type of results that flow through the ResultFlow
 * @param dataSerializer The serializer for the data type T (used for descriptor only)
 */
internal class ResultFlowSerializer<T>(private val dataSerializer: KSerializer<T>) : KSerializer<ResultFlow<T>> {

    override val descriptor: SerialDescriptor
        get() = dataSerializer.descriptor

    /**
     * Serializes the ResultFlow. This is a no-op as flow state cannot be serialized.
     *
     * @param encoder The encoder to use for serialization
     * @param value The ResultFlow instance to serialize
     */
    override fun serialize(encoder: Encoder, value: ResultFlow<T>) = Unit

    /**
     * Deserializes a ResultFlow by creating a new empty instance.
     *
     * @param decoder The decoder to use for deserialization
     * @return A new empty ResultFlow instance
     */
    override fun deserialize(decoder: Decoder): ResultFlow<T> = ResultFlow()
}
