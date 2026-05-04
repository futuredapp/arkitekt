package app.futured.arkitekt.crusecases.test

import app.futured.arkitekt.crusecases.CoroutineScopeOwner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope

@ExperimentalCoroutinesApi
class TestCoroutineScopeOwner : CoroutineScopeOwner {
    val testDispatcher = StandardTestDispatcher()

    override val useCaseScope = TestScope(testDispatcher)
    override val useCaseJobPool: MutableMap<Any, Job> = mutableMapOf()

    override fun getWorkerDispatcher(): CoroutineDispatcher = testDispatcher
}
