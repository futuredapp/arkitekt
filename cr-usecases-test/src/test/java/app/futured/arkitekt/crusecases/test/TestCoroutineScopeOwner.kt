package app.futured.arkitekt.crusecases.test

import app.futured.arkitekt.crusecases.CoroutineScopeOwner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope

@ExperimentalCoroutinesApi
class TestCoroutineScopeOwner : CoroutineScopeOwner {

    val testDispatcher = StandardTestDispatcher()

    override val coroutineScope = TestScope(testDispatcher)

    override fun getWorkerDispatcher(): CoroutineDispatcher = testDispatcher
}
