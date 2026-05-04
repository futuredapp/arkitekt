package app.futured.arkitekt.decompose.test

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Contract for [BaseComponent] unit tests in Kotlin Multiplatform.
 *
 * Implement via Kotlin class delegation backed by [ComponentTestPreparation].
 * [setup] and [cleanup] are wired to [BeforeTest] / [AfterTest] and run automatically.
 *
 * Sample test:
 *
 *     class SampleComponentTest : ComponentTest by ComponentTestPreparation() {
 *
 *         @Test
 *         fun `example test`() = runComponentTest {
 *             lifecycleRegistry.create()
 *             val component = SampleComponent(componentContext, testScope)
 *             // assertions...
 *         }
 *     }
 */
interface ComponentTest {
    /**
     * [TestScope] shared by the test body and [runComponentTest].
     */
    val testScope: TestScope

    /**
     * [TestDispatcher] installed as [kotlinx.coroutines.Dispatchers.Main] during each test.
     */
    val testDispatcher: TestDispatcher

    /**
     * [LifecycleRegistry] that drives the [componentContext] lifecycle.
     * Call [com.arkivanov.essenty.lifecycle.create] inside a test to start the component lifecycle.
     */
    val lifecycleRegistry: LifecycleRegistry

    /**
     * [DefaultComponentContext] backed by [lifecycleRegistry]. Pass this to the component under test.
     */
    val componentContext: DefaultComponentContext

    /**
     * Sets [testDispatcher] as [kotlinx.coroutines.Dispatchers.Main]. Called automatically under [BeforeTest].
     */
    @BeforeTest
    fun setup()

    /**
     * Destroys [lifecycleRegistry] if still alive and resets [kotlinx.coroutines.Dispatchers.Main]. Called automatically under [AfterTest].
     */
    @AfterTest
    fun cleanup()
}

private val DEFAULT_TIMEOUT = 60.seconds

/**
 * Runs [testBody] inside [testScope] via [kotlinx.coroutines.test.runTest].
 *
 * @param timeout Maximum duration before the test times out. Defaults to 60 seconds.
 * @param testBody Suspend test body executed with [TestScope] as the receiver.
 */
fun ComponentTest.runComponentTest(
    timeout: Duration = DEFAULT_TIMEOUT,
    testBody: suspend TestScope.() -> Unit,
) = testScope.runTest(timeout, testBody)
