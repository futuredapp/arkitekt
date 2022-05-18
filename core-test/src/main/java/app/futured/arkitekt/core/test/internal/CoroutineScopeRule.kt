package app.futured.arkitekt.core.test.internal

import app.futured.arkitekt.crusecases.CoroutineScopeOwner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

@ExperimentalCoroutinesApi
class CoroutineScopeRule : TestRule {

    @ExperimentalCoroutinesApi
    class TestCoroutineScopeOwner : CoroutineScopeOwner {

        val testDispatcher = UnconfinedTestDispatcher()

        override val coroutineScope = TestScope(testDispatcher)

        override fun getWorkerDispatcher(): CoroutineDispatcher = testDispatcher
    }

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                val scopeOwner = TestCoroutineScopeOwner()
                Dispatchers.setMain(scopeOwner.testDispatcher)

                base.evaluate()

                Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
            }
        }
    }
}
