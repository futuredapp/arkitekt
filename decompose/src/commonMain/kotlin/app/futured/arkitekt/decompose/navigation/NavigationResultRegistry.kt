package app.futured.arkitekt.decompose.navigation

import com.arkivanov.essenty.statekeeper.SerializableContainer
import com.arkivanov.essenty.statekeeper.StateKeeper
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.serializer

/**
 * A typed, named handle identifying a navigation result channel.
 *
 * Declaring keys as named constants next to a nav-host (rather than scattering bare strings) keeps
 * them in one reviewable place and lets the result type travel with the key, so call sites don't
 * repeat the type and serializer:
 *
 * ```kotlin
 * object HomeResultKeys {
 *     val Picker = ResultKey<String>("home.picker")
 * }
 * ```
 *
 * @param T the type of the result, which must be [kotlinx.serialization.Serializable].
 * @property name the stable string key. It must be **stable across process death** (so a recreated
 * parent re-attaches to the same result slot) and **unique across concurrently-active destinations**
 * (the registry is application-wide).
 * @property serializer the [KSerializer] used to persist and restore the result.
 */
class ResultKey<T : Any>(
    val name: String,
    val serializer: KSerializer<T>,
)

/**
 * Creates a [ResultKey] inferring the [KSerializer] from the reified type [T].
 */
inline fun <reified T : Any> ResultKey(name: String): ResultKey<T> = ResultKey(name, serializer())

/**
 * A durable, application-wide store for one-shot navigation results.
 *
 * A child destination sends a result via [send] and the parent collects it via [results], routed by
 * a stable string key. Results survive both configuration changes and **process death**: each value
 * is persisted into the owning component's [StateKeeper] until it is collected exactly once.
 *
 * Exposed through `ArkitektComponentContext.navigationResultRegistry`; obtain a [ResultFlow] handle
 * with `ArkitektComponentContext.resultFlow(key)` rather than using this interface directly.
 *
 * Results are intended to be **small** one-shot values (ids, selections). They are stored in the
 * Android saved state `Bundle` (~1 MB limit); never pass large payloads through them.
 */
interface NavigationResultRegistry {

    /**
     * Persists a one-shot [value] for [key] and delivers it to an active collector if present.
     *
     * Overwrites any previous undelivered value for the same key (latest wins).
     */
    suspend fun <T : Any> send(key: String, value: T, serializer: KSerializer<T>)

    /**
     * Returns a cold [Flow] of results for [key].
     *
     * On collection it first replays a single pending value if one exists (from this process or
     * restored after process death), clears it (consume-once), then forwards live emissions. Only a
     * single collector per key may be active at a time.
     *
     * @throws IllegalStateException if [key] already has an active collector.
     */
    fun <T : Any> results(key: String, serializer: KSerializer<T>): Flow<T>
}

/**
 * Creates a [NavigationResultRegistry] anchored to the given [stateKeeper].
 *
 * Create a single instance at the root component (over the root's [StateKeeper], which survives
 * process death) and share that same instance down the component tree via the component context.
 */
fun NavigationResultRegistry(stateKeeper: StateKeeper): NavigationResultRegistry =
    DefaultNavigationResultRegistry(stateKeeper)

/**
 * Default [NavigationResultRegistry] anchored to a [StateKeeper].
 */
internal class DefaultNavigationResultRegistry(stateKeeper: StateKeeper) : NavigationResultRegistry {

    /**
     * Undelivered results, each held as a lazily (de)serialized [SerializableContainer]. This map is
     * the source of truth for durability and is persisted via [StateKeeper].
     */
    private val pending: MutableMap<String, SerializableContainer> =
        stateKeeper.consume(STATE_KEY, mapSerializer)?.toMutableMap() ?: mutableMapOf()

    /**
     * Per-key hot channel for same-process delivery to an already-attached collector.
     * Values emitted while no collector is attached are not retained here (durability comes from
     * [pending]); the extra buffer only keeps [send] from suspending.
     */
    private val liveFlows = mutableMapOf<String, MutableSharedFlow<Any>>()

    /** Keys with a currently-active collector, used to fail fast on key collisions. */
    private val activeCollectors = mutableSetOf<String>()

    init {
        stateKeeper.register(STATE_KEY, mapSerializer) { pending.toMap() }
    }

    override suspend fun <T : Any> send(key: String, value: T, serializer: KSerializer<T>) {
        // Persist for durability, then notify any active collector.
        pending[key] = SerializableContainer(value, serializer)
        liveFlow(key).emit(value)
    }

    override fun <T : Any> results(key: String, serializer: KSerializer<T>): Flow<T> = flow {
        check(activeCollectors.add(key)) {
            "Result key '$key' already has an active collector — result keys must be unique " +
                "across concurrently-active destinations"
        }
        try {
            // 1. Replay a pending value (this process or restored after death) exactly once.
            pending.remove(key)?.consume(serializer)?.let { emit(it) }
            // 2. Forward live emissions, clearing the durable copy as each is delivered so a later
            //    process death cannot replay an already-consumed value.
            liveFlow(key).collect { value ->
                pending.remove(key)
                @Suppress("UNCHECKED_CAST")
                emit(value as T)
            }
        } finally {
            activeCollectors.remove(key)
        }
    }

    private fun liveFlow(key: String): MutableSharedFlow<Any> =
        liveFlows.getOrPut(key) {
            MutableSharedFlow(
                replay = 0,
                extraBufferCapacity = 1,
                onBufferOverflow = BufferOverflow.DROP_OLDEST,
            )
        }

    private companion object {
        private const val STATE_KEY = "arkitekt.navigation.results"
        private val mapSerializer = MapSerializer(serializer<String>(), SerializableContainer.serializer())
    }
}
