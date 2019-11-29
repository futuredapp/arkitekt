package com.thefuntasty.mvvm.crinteractors

import com.thefuntasty.mvvm.crinteractors.testinteractors.base.BaseCoroutineScopeOwnerTest
import com.thefuntasty.mvvm.crinteractors.testinteractors.testinteractors.TestFailureInteractor
import com.thefuntasty.mvvm.crinteractors.testinteractors.testinteractors.TestFlowFailureInteractor
import com.thefuntasty.mvvm.crinteractors.testinteractors.testinteractors.TestFlowInteractor
import com.thefuntasty.mvvm.crinteractors.testinteractors.testinteractors.TestInteractor
import org.junit.Assert
import org.junit.Test

class CoroutineScopeOwnerTest : BaseCoroutineScopeOwnerTest() {

    @Test
    fun previousExecutionCanceled() {
        val testInteractor = TestInteractor()
        var count = 0
        testInteractor.execute(1) {
            onSuccess { count++ }
        }
        coroutineScope.advanceTimeBy(500)
        testInteractor.execute(1) {
            onSuccess { count++ }
        }
        coroutineScope.advanceTimeBy(1000)
        Assert.assertEquals("PreviousExecutionNotCanceled", 1, count)
    }

    @Test
    fun onErrorCalled() {
        val testFailureInteractor = TestFailureInteractor()
        var resultError: Throwable? = null
        testFailureInteractor.execute(IllegalStateException()) {
            onError { resultError = it }
        }
        Assert.assertNotNull(resultError)
    }

    @Test
    fun flowPreviousExecutionCanceled() {
        val testFlowInteractor = TestFlowInteractor()
        val testingList = listOfNotNull(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        val resultList = mutableListOf<Int>()
        testFlowInteractor.execute(TestFlowInteractor.Data(testingList, 1000)) {
            onNext { resultList.add(it) }
            onError {
                it.printStackTrace()
                Assert.fail("Exception thrown where shouldn't")
            }
            onComplete {
                Assert.fail("onComplete called where shouldn't")
            }
        }

        testFlowInteractor.execute(TestFlowInteractor.Data(testingList, 1000)) {
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
        val testFlowInteractor = TestFlowInteractor()
        var completed = false
        val testingList = listOfNotNull(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        testFlowInteractor.execute(TestFlowInteractor.Data(testingList, 1000)) {
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
        val testFlowFailureInteractor = TestFlowFailureInteractor()
        var resultError: Throwable? = null
        testFlowFailureInteractor.execute(IllegalStateException()) {
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
