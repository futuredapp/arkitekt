package arkitekt.crusecases.base

import app.futured.arkitekt.crusecases.CoroutineScopeOwner
import app.futured.arkitekt.crusecases.FlowUseCase
import app.futured.arkitekt.crusecases.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.setMain
import org.junit.Before

abstract class BaseCoroutineScopeOwnerTest : CoroutineScopeOwner {

    override val useCaseJobPool: MutableMap<FlowUseCase<*, *>, Job> = mutableMapOf()
    override val useCaseDeferredPool: MutableMap<UseCase<*, *>, Deferred<*>> = mutableMapOf()

    private val testDispatcher = StandardTestDispatcher()
    override val coroutineScope = TestScope(testDispatcher)
    override fun getWorkerDispatcher(): CoroutineDispatcher = testDispatcher

    @Before
    fun setDispatchers() {
        Dispatchers.setMain(testDispatcher)
    }

}
