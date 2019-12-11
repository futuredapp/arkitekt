package com.thefuntasty.mvvm.crinteractors

import com.thefuntasty.mvvm.crinteractors.testinteractors.testinteractors.TestFailureInteractor
import com.thefuntasty.mvvm.crinteractors.testinteractors.testinteractors.TestFlowFailureInteractor
import com.thefuntasty.mvvm.crinteractors.testinteractors.testinteractors.TestFlowInteractor
import com.thefuntasty.mvvm.crinteractors.testinteractors.testinteractors.TestInteractor
import com.thefuntasty.mvvm.crinteractors.testinteractors.base.BaseCoroutineScopeOwnerTest
import org.junit.Assert
import org.junit.Test

class CoroutineScopeOwnerTest : BaseCoroutineScopeOwnerTest() {

    @Test
    fun previousExecutionCanceled() {
        val testInteractor = TestInteractor().apply { init(1) }
        var count = 0
        testInteractor.execute( {
            count++
        }, {
            Assert.fail("Exception thrown where shouldn't")
        })
        coroutineScope.advanceTimeBy(500)
        testInteractor.execute( {
            count++
        }, {
            Assert.fail("Exception thrown where shouldn't")
        })
        coroutineScope.advanceTimeBy(1000)
        Assert.assertEquals("PreviousExecutionNotCanceled", 1, count)
    }

    @Test
    fun onErrorCalled() {
        val testFailureInteractor = TestFailureInteractor()
        var resultError: Throwable? = null
        testFailureInteractor.init(IllegalStateException()).execute({
        }, {
            resultError = it
        })
        Assert.assertNotNull(resultError)
    }

    @Test
    fun flowPreviousExecutionCanceled() {
        val testFlowInteractor = TestFlowInteractor()
        val testingList = listOfNotNull(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        val resultList = mutableListOf<Int>()
        testFlowInteractor.init(testingList, 1000).execute({
            resultList.add(it)
        }, {
            it.printStackTrace()
            Assert.fail("Exception thrown where shouldn't")
        }, {
            Assert.fail("onComplete called where shouldn't")
        })

        testFlowInteractor.execute({
            resultList.add(it)
        }, {
            Assert.fail("Exception thrown where shouldn't")
        }, {})
        coroutineScope.advanceTimeBy(10000)
        Assert.assertEquals("PreviousExecutionNotCanceled", testingList, resultList)
    }

    @Test
    fun flowOnCompleteCalled() {
        val testFlowInteractor = TestFlowInteractor()
        var completed = false
        val testingList = listOfNotNull(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        testFlowInteractor.init(testingList, 1000).execute({
        }, {
            Assert.fail("Exception thrown where shouldn't")
        }, {
            completed = true
        })
        coroutineScope.advanceTimeBy(10000)
        Assert.assertEquals("PreviousExecutionNotCanceled", true, completed)
    }

    @Test
    fun flowOnErrorCalled() {
        val testFlowFailureInteractor = TestFlowFailureInteractor()
        var resultError: Throwable? = null
        testFlowFailureInteractor.init(IllegalStateException()).execute({
            Assert.fail("onNext called where shouldn't")
        }, {
            resultError = it
        }, {
            Assert.fail("onComplete called where shouldn't")
        })
        Assert.assertNotNull(resultError)
    }
}
