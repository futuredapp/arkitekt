package com.thefuntasty.mvvm.crinteractors.testinteractors.base

import com.thefuntasty.mvvm.crinteractors.CoroutineScopeOwner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

abstract class BaseCoroutineScopeOwnerTest : CoroutineScopeOwner {

    private val testDispatcher = TestCoroutineDispatcher()
    override lateinit var coroutineScope: TestCoroutineScope
    override fun getWorkerDispatcher(): CoroutineDispatcher = testDispatcher
    @Before
    fun setDispatchers() {
        Dispatchers.setMain(testDispatcher)
        coroutineScope = TestCoroutineScope(testDispatcher)
    }

    @After
    fun cleanupCorouintes() {
        coroutineScope.cleanupTestCoroutines()
    }
}