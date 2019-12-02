package com.thefuntasty.mvvm.crinteractors

import com.thefuntasty.mvvm.crinteractors.testinteractors.base.BaseCoroutineScopeOwnerTest
import com.thefuntasty.mvvm.crinteractors.testinteractors.testinteractors.TestFailureFlowUseCase
import com.thefuntasty.mvvm.crinteractors.testinteractors.testinteractors.TestFailureUseCase
import com.thefuntasty.mvvm.crinteractors.testinteractors.testinteractors.TestFlowUseCase
import com.thefuntasty.mvvm.crinteractors.testinteractors.testinteractors.TestUseCase
import org.junit.Assert
import org.junit.Test

class CoroutineScopeOwnerTest : BaseCoroutineScopeOwnerTest() {

    @Test
    fun previousExecutionCanceled() {
        val testUseCase = TestUseCase()
        var count = 0
        testUseCase.execute(1) {
            onSuccess { count++ }
        }
        coroutineScope.advanceTimeBy(500)
        testUseCase.execute(1) {
            onSuccess { count++ }
        }
        coroutineScope.advanceTimeBy(1000)
        Assert.assertEquals("PreviousExecutionNotCanceled", 1, count)
    }

    @Test
    fun onErrorCalled() {
        val testFailureUseCase = TestFailureUseCase()
        var resultError: Throwable? = null
        testFailureUseCase.execute(IllegalStateException()) {
            onError { resultError = it }
        }
        Assert.assertNotNull(resultError)
    }

    @Test
    fun flowPreviousExecutionCanceled() {
        val testFlowUseCase = TestFlowUseCase()
        val testingList = listOfNotNull(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        val resultList = mutableListOf<Int>()
        testFlowUseCase.execute(TestFlowUseCase.Data(testingList, 1000)) {
            onNext { resultList.add(it) }
            onError {
                it.printStackTrace()
                Assert.fail("Exception thrown where shouldn't")
            }
            onComplete {
                Assert.fail("onComplete called where shouldn't")
            }
        }

        testFlowUseCase.execute(TestFlowUseCase.Data(testingList, 1000)) {
            onNext { resultList.add(it) }
            onError {
                Assert.fail("Exception thrown where shouldn't")
            }
        }
        coroutineScope.advanceTimeBy(10000)
        Assert.assertEquals("PreviousExecutionNotCanceled", testingList, resultList)
    }

    @Test
    fun flowOnCompleteCalled() {
        val testFlowUseCase = TestFlowUseCase()
        var completed = false
        val testingList = listOfNotNull(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        testFlowUseCase.execute(TestFlowUseCase.Data(testingList, 1000)) {
            onError {
                Assert.fail("Exception thrown where shouldn't")
            }
            onComplete {
                completed = true
            }
        }
        coroutineScope.advanceTimeBy(10000)
        Assert.assertEquals("PreviousExecutionNotCanceled", true, completed)
    }

    @Test
    fun flowOnErrorCalled() {
        val testFlowFailureUseCase = TestFailureFlowUseCase()
        var resultError: Throwable? = null
        testFlowFailureUseCase.execute(IllegalStateException()) {
            onNext {
                Assert.fail("onNext called where shouldn't")
            }
            onError {
                resultError = it
            }
            onComplete {
                Assert.fail("onComplete called where shouldn't")
            }
        }
        Assert.assertNotNull(resultError)
    }
}
