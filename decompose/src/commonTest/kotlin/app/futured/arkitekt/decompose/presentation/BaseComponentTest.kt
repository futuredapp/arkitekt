package app.futured.arkitekt.decompose.presentation

import app.futured.arkitekt.decompose.test.ComponentTest
import app.futured.arkitekt.decompose.test.ComponentTestPreparation
import app.futured.arkitekt.decompose.test.runComponentTest
import com.arkivanov.decompose.GenericComponentContext
import com.arkivanov.essenty.lifecycle.create
import com.arkivanov.essenty.lifecycle.destroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private data class TestState(val value: Int = 0)

private sealed interface TestEvent {
    data object First : TestEvent
    data class WithPayload(val n: Int) : TestEvent
}

private class TestableComponent(
    componentContext: GenericComponentContext<*>,
    lifecycleScope: CoroutineScope,
) : BaseComponent<TestState, TestEvent>(
    componentContext = componentContext,
    defaultState = TestState(),
    lifecycleScope = lifecycleScope,
) {
    fun emit(event: TestEvent) = sendUiEvent(event)
    val state get() = componentState
}

private fun ComponentTest.createComponent(
    scope: CoroutineScope = testScope,
) = TestableComponent(componentContext, scope)

@OptIn(ExperimentalCoroutinesApi::class)
class BaseComponentTest : ComponentTest by ComponentTestPreparation() {

    // region UI events

    @Test
    fun `sendUiEvent emits event to collector`() = runComponentTest {
        lifecycleRegistry.create()
        val component = createComponent()
        val received = mutableListOf<TestEvent>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            component.events.collect { received.add(it) }
        }

        component.emit(TestEvent.First)
        runCurrent()

        assertEquals(listOf<TestEvent>(TestEvent.First), received)
    }

    @Test
    fun `sendUiEvent preserves order and payload`() = runComponentTest {
        lifecycleRegistry.create()
        val component = createComponent()
        val received = mutableListOf<TestEvent>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            component.events.collect { received.add(it) }
        }

        component.emit(TestEvent.WithPayload(1))
        component.emit(TestEvent.WithPayload(2))
        component.emit(TestEvent.WithPayload(3))
        runCurrent()

        assertEquals(
            listOf<TestEvent>(TestEvent.WithPayload(1), TestEvent.WithPayload(2), TestEvent.WithPayload(3)),
            received,
        )
    }

    @Test
    fun `events are buffered before collection`() = runComponentTest {
        lifecycleRegistry.create()
        val component = createComponent()

        component.emit(TestEvent.WithPayload(10))
        component.emit(TestEvent.WithPayload(20))
        runCurrent()

        val received = mutableListOf<TestEvent>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            component.events.collect { received.add(it) }
        }

        assertEquals(listOf<TestEvent>(TestEvent.WithPayload(10), TestEvent.WithPayload(20)), received)
    }

    @Test
    fun `events are delivered exactly once across re-subscription`() = runComponentTest {
        lifecycleRegistry.create()
        val component = createComponent()

        val firstCollector = mutableListOf<TestEvent>()
        val firstJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            component.events.collect { firstCollector.add(it) }
        }

        component.emit(TestEvent.WithPayload(1))
        runCurrent()

        firstJob.cancel()

        val secondCollector = mutableListOf<TestEvent>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            component.events.collect { secondCollector.add(it) }
        }

        component.emit(TestEvent.WithPayload(2))
        runCurrent()

        assertEquals(listOf<TestEvent>(TestEvent.WithPayload(1)), firstCollector)
        assertEquals(listOf<TestEvent>(TestEvent.WithPayload(2)), secondCollector)
    }

    @Test
    fun `componentState starts with default state`() = runComponentTest {
        lifecycleRegistry.create()
        val component = createComponent()

        assertEquals(TestState(), component.state.value)
    }

    // endregion

    // region Lifecycle

    @Test
    fun `onDestroy completes the events flow`() = runComponentTest {
        val scope = CoroutineScope(testDispatcher + Job())
        val component = createComponent(scope = scope)
        lifecycleRegistry.create()

        var completed = false
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            component.events
                .onCompletion { completed = true }
                .collect { }
        }

        lifecycleRegistry.destroy()

        assertTrue(completed)
    }

    @Test
    fun `onDestroy cancels the lifecycleScope`() = runComponentTest {
        val scope = CoroutineScope(testDispatcher + Job())
        createComponent(scope = scope)
        lifecycleRegistry.create()

        lifecycleRegistry.destroy()

        assertFalse(scope.coroutineContext[Job]!!.isActive)
    }

    @Test
    fun `sendUiEvent after destroy is a no-op`() = runComponentTest {
        val scope = CoroutineScope(testDispatcher + Job())
        val component = createComponent(scope = scope)
        lifecycleRegistry.create()

        lifecycleRegistry.destroy()
        runCurrent()

        val received = mutableListOf<TestEvent>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            component.events.collect { received.add(it) }
        }

        component.emit(TestEvent.First)
        runCurrent()

        assertTrue(received.isEmpty())
    }

    // endregion
}
