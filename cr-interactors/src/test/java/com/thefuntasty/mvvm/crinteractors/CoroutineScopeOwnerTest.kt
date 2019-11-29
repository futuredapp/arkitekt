package com.thefuntasty.mvvm.crinteractors

import com.thefuntasty.mvvm.crinteractors.testinteractors.base.BaseCoroutineScopeOwnerTest
import com.thefuntasty.mvvm.crinteractors.testinteractors.testinteractors.TestFailureUsecase
import com.thefuntasty.mvvm.crinteractors.testinteractors.testinteractors.TestFlowFailureUsecase
import com.thefuntasty.mvvm.crinteractors.testinteractors.testinteractors.TestFlowUsecase
import com.thefuntasty.mvvm.crinteractors.testinteractors.testinteractors.TestUsecase
import org.junit.Assert
import org.junit.Test

class CoroutineScopeOwnerTest : BaseCoroutineScopeOwnerTest() {

    @Test
    fun previousExecutionCanceled() {
        val testUsecase = TestUsecase()
        var count = 0
        testUsecase.execute(1) {
            onSuccess { count++ }
        }
        coroutineScope.advanceTimeBy(500)
        testUsecase.execute(1) {
            onSuccess { count++ }
        }
        coroutineScope.advanceTimeBy(1000)
        Assert.assertEquals("PreviousExecutionNotCanceled", 1, count)
    }

    @Test
    fun onErrorCalled() {
        val testFailureUsecase = TestFailureUsecase()
        var resultError: Throwable? = null
        testFailureUsecase.execute(IllegalStateException()) {
            onError { resultError = it }
        }
        Assert.assertNotNull(resultError)
    }

    @Test
    fun flowPreviousExecutionCanceled() {
        val testFlowUsecase = TestFlowUsecase()
        val testingList = listOfNotNull(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        val resultList = mutableListOf<Int>()
        testFlowUsecase.execute(TestFlowUsecase.Data(testingList, 1000)) {
            onNext { resultList.add(it) }
            onError {
                it.printStackTrace()
                Assert.fail("Exception thrown where shouldn't")
            }
            onComplete {
                Assert.fail("onComplete called where shouldn't")
            }
        }

        testFlowUsecase.execute(TestFlowUsecase.Data(testingList, 1000)) {
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
        val testFlowUsecase = TestFlowUsecase()
        var completed = false
        val testingList = listOfNotNull(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        testFlowUsecase.execute(TestFlowUsecase.Data(testingList, 1000)) {
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
        val testFlowFailureUsecase = TestFlowFailureUsecase()
        var resultError: Throwable? = null
        testFlowFailureUsecase.execute(IllegalStateException()) {
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
