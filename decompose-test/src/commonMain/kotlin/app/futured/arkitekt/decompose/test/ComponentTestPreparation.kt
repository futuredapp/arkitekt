package app.futured.arkitekt.decompose.test

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

/**
 * Default [ComponentTest] implementation intended for Kotlin class delegation.
 *
 * Open for subclassing to add project-specific fixtures or helpers.
 */
open class ComponentTestPreparation : ComponentTest {

    override val testScope = TestScope()
    override val testDispatcher = StandardTestDispatcher(testScope.testScheduler)
    override val lifecycleRegistry = LifecycleRegistry()
    override val componentContext = DefaultComponentContext(lifecycleRegistry)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun cleanup() {
        if (lifecycleRegistry.state != Lifecycle.State.DESTROYED) {
            lifecycleRegistry.destroy()
        }
        Dispatchers.resetMain()
    }
}
