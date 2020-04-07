package com.thefuntasty.mvvm.crusecases.test

import app.futured.arkitekt.crusecases.CoroutineScopeOwner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope

@ExperimentalCoroutinesApi
class TestCoroutineScopeOwner : CoroutineScopeOwner {

    val testDispatcher = TestCoroutineDispatcher()

    override val coroutineScope = TestCoroutineScope(testDispatcher)

    override fun getWorkerDispatcher(): CoroutineDispatcher = testDispatcher
}
