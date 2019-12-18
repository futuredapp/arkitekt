package com.thefuntasty.mvvm.crusecases

import com.thefuntasty.mvvm.crusecases.base.BaseCoroutineScopeOwnerTest
import com.thefuntasty.mvvm.crusecases.testusecases.TestFailureFlowUseCase
import com.thefuntasty.mvvm.crusecases.testusecases.TestFailureUseCase
import com.thefuntasty.mvvm.crusecases.testusecases.TestFlowUseCase
import com.thefuntasty.mvvm.crusecases.testusecases.TestUseCase
import org.junit.Assert
import org.junit.Test

class CoroutineScopeOwnerTest : BaseCoroutineScopeOwnerTest() {

    @Test
    fun `given 1s delay use case when executed two times then first execution cancelled`() {
        val testUseCase = TestUseCase()
        var executionCount = 0

        testUseCase.execute(1) {
            onSuccess { executionCount++ }
            onError { Assert.fail("Exception thrown where shouldn't") }
        }
        coroutineScope.advanceTimeBy(500)

        testUseCase.execute(1) {
            onSuccess { executionCount++ }
            onError { Assert.fail("Exception thrown where shouldn't") }
        }
        coroutineScope.advanceTimeBy(1000)

        Assert.assertEquals(1, executionCount)
    }

    @Test
    fun `given failing test use case when executed then indicates onError`() {
        val testFailureUseCase = TestFailureUseCase()
        var resultError: Throwable? = null

        testFailureUseCase.execute(IllegalStateException()) {
            onError { resultError = it }
        }

        Assert.assertNotNull(resultError)
    }

    @Test
    fun `given test flow use case when executed two times then first execution cancelled`() {
        val testFlowUseCase = TestFlowUseCase()
        val testingList = listOfNotNull(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        val resultList = mutableListOf<Int>()

        testFlowUseCase.execute(TestFlowUseCase.Data(testingList, 1000)) {
            onNext { resultList.add(it) }
            onError { Assert.fail("Exception thrown where shouldn't") }
            onComplete { Assert.fail("onComplete called where shouldn't") }
        }

        testFlowUseCase.execute(TestFlowUseCase.Data(testingList, 1000)) {
            onNext { resultList.add(it) }
            onError { Assert.fail("Exception thrown where shouldn't") }
        }
        coroutineScope.advanceTimeBy(10000)

        Assert.assertEquals(testingList, resultList)
    }

    @Test
    fun `given test flow use case when executed and all items emitted then completes`() {
        val testFlowUseCase = TestFlowUseCase()
        var completed = false
        val testingList = listOfNotNull(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)

        testFlowUseCase.execute(TestFlowUseCase.Data(testingList, 1000)) {
            onError { Assert.fail("Exception thrown where shouldn't") }
            onComplete { completed = true }
        }
        coroutineScope.advanceTimeBy(10000)

        Assert.assertEquals(true, completed)
    }

    @Test
    fun `given failing flow use case when executed then indicates onError`() {
        val testFlowFailureUseCase = TestFailureFlowUseCase()
        var resultError: Throwable? = null

        testFlowFailureUseCase.execute(IllegalStateException()) {
            onNext { Assert.fail("onNext called where shouldn't") }
            onError { resultError = it }
            onComplete { Assert.fail("onComplete called where shouldn't") }
        }

        Assert.assertNotNull(resultError)
    }
}
