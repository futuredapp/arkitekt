package com.thefuntasty.mvvm.crusecases.base

import com.thefuntasty.mvvm.crusecases.CoroutineScopeOwner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

abstract class BaseCoroutineScopeOwnerTest : CoroutineScopeOwner {

    private val testDispatcher = TestCoroutineDispatcher()
    override val coroutineScope = TestCoroutineScope(testDispatcher)
    override fun getWorkerDispatcher(): CoroutineDispatcher = testDispatcher

    @Before
    fun setDispatchers() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun cleanupCoroutines() {
        coroutineScope.cleanupTestCoroutines()
    }
}
