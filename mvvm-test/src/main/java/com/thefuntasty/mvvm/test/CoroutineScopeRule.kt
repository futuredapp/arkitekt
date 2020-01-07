package com.thefuntasty.mvvm.test

import com.thefuntasty.mvvm.crusecases.CoroutineScopeOwner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

@ExperimentalCoroutinesApi
class CoroutineScopeRule : TestRule {

    @ExperimentalCoroutinesApi
    class TestCoroutineScopeOwner : CoroutineScopeOwner {

        val testDispatcher = TestCoroutineDispatcher()

        override val coroutineScope = TestCoroutineScope(testDispatcher)

        override fun getWorkerDispatcher(): CoroutineDispatcher = testDispatcher
    }

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                val scopeOwner = TestCoroutineScopeOwner()
                Dispatchers.setMain(scopeOwner.testDispatcher)

                base.evaluate()

                scopeOwner.coroutineScope.cleanupTestCoroutines()
                Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
            }
        }
    }
}
