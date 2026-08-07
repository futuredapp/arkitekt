package app.futured.arkitekt.decompose.navigation

import com.arkivanov.essenty.statekeeper.SerializableContainer
import com.arkivanov.essenty.statekeeper.StateKeeperDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class NavigationResultRegistryTest {
    @Test
    fun `result survives process death`() = runTest {
        // Producer process: send before any collector attaches.
        val producerKeeper = StateKeeperDispatcher()
        val producer = DefaultNavigationResultRegistry(producerKeeper)
        producer.send("k", "hello", String.serializer())

        // Simulate process death: save, serialize/deserialize, restore into a fresh registry.
        val restoredKeeper = StateKeeperDispatcher(roundTrip(producerKeeper.save()))
        val consumer = DefaultNavigationResultRegistry(restoredKeeper)

        val received = consumer.results("k", String.serializer()).first()
        assertEquals("hello", received)
    }

    @Test
    fun `result delivered exactly once across config change in the same process`() = runTest {
        val registry = DefaultNavigationResultRegistry(StateKeeperDispatcher())
        val received = mutableListOf<Int>()
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            registry.results("k", Int.serializer()).collect { received.add(it) }
        }
        runCurrent() // collector attached, pending empty, now collecting live

        registry.send("k", 42, Int.serializer())
        runCurrent()

        assertEquals(listOf(42), received) // exactly once, not also via the durable slot
        job.cancel()
    }

    @Test
    fun `result emitted before the collector attaches is still delivered`() = runTest {
        val registry = DefaultNavigationResultRegistry(StateKeeperDispatcher())
        registry.send("k", "x", String.serializer()) // no collector yet

        val received = registry.results("k", String.serializer()).first()
        assertEquals("x", received)
    }

    @Test
    fun `result is consumed once and not redelivered after a later process death`() = runTest {
        val producerKeeper = StateKeeperDispatcher()
        DefaultNavigationResultRegistry(producerKeeper).send("k", "once", String.serializer())

        // First restore delivers the value (and clears the slot).
        val consumerKeeper = StateKeeperDispatcher(roundTrip(producerKeeper.save()))
        val consumer = DefaultNavigationResultRegistry(consumerKeeper)
        assertEquals("once", consumer.results("k", String.serializer()).first())

        // A second process death after consumption must yield nothing.
        val laterKeeper = StateKeeperDispatcher(roundTrip(consumerKeeper.save()))
        val later = DefaultNavigationResultRegistry(laterKeeper)
        val again = withTimeoutOrNull(TIMEOUT_MS.milliseconds) { later.results("k", String.serializer()).first() }
        assertNull(again)
    }

    @Test
    fun `second concurrent collector on the same key fails fast`() = runTest {
        val registry = DefaultNavigationResultRegistry(StateKeeperDispatcher())
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            registry.results("k", String.serializer()).collect { }
        }
        runCurrent() // first collector active

        assertFailsWith<IllegalStateException> {
            registry.results("k", String.serializer()).first()
        }

        // Once the first collector is gone, the key is free again.
        job.cancel()
        runCurrent()
        registry.send("k", "ok", String.serializer())
        assertEquals("ok", registry.results("k", String.serializer()).first())
    }

    @Test
    fun `typed key round-trips inside a navigation config`() {
        val config = PickerConfig(ResultKey("home.picker"))

        val restored = Json.decodeFromString(
            PickerConfig.serializer(),
            Json.encodeToString(PickerConfig.serializer(), config),
        )

        assertEquals("home.picker", restored.resultKey.name)
        // The serializer is recovered from the config's compile-time type argument, so the restored
        // key can still (de)serialize values without the caller restating the type.
        assertEquals("v", restored.resultKey.serializer.let { ser -> Json.decodeFromString(ser, "\"v\"") })
    }

    @Test
    fun `config embedding a typed key stays comparable by value across a round-trip`() {
        val config = PickerConfig(ResultKey("home.picker"))

        val restored = Json.decodeFromString(
            PickerConfig.serializer(),
            Json.encodeToString(PickerConfig.serializer(), config),
        )

        // Decompose compares configurations by value; name-based ResultKey equality must preserve that.
        assertEquals(config, restored)
    }

    @Serializable
    private data class PickerConfig(val resultKey: ResultKey<String>)

    private companion object {
        private const val TIMEOUT_MS = 100L

        /** Forces a real serialization round-trip, mimicking what the OS does on process death. */
        private fun roundTrip(container: SerializableContainer): SerializableContainer =
            Json.decodeFromString(
                SerializableContainer.serializer(),
                Json.encodeToString(SerializableContainer.serializer(), container),
            )
    }
}
