package app.futured.arkitekt.crusecases

import app.futured.arkitekt.core.error.UseCaseErrorHandler
import app.futured.arkitekt.crusecases.base.BaseCoroutineScopeOwnerTest
import app.futured.arkitekt.crusecases.testusecases.TestFailureFlowUseCase
import app.futured.arkitekt.crusecases.testusecases.TestFailureUseCase
import app.futured.arkitekt.crusecases.testusecases.TestFlowUseCase
import app.futured.arkitekt.crusecases.testusecases.TestUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CoroutineScopeOwnerTest : BaseCoroutineScopeOwnerTest() {

    @Before
    fun setUp() {
        UseCaseErrorHandler.globalOnErrorLogger = {}
    }

    @After
    fun tearDown() {
        UseCaseErrorHandler.globalOnErrorLogger = {}
    }

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

    @Test
    fun `given success use case launched in coroutine then result is set to success`() {
        val testUseCase = TestUseCase()

        var result: Result<Int>? = null
        coroutineScope.launch {
            result = testUseCase.execute(1)
        }
        coroutineScope.advanceTimeBy(10000)

        Assert.assertEquals(Success(1), result)
    }

    @Test
    fun `given failing use case launched in coroutine then result is set to error`() {
        val testUseCase = TestFailureUseCase()

        var result: Result<Unit>? = null
        coroutineScope.launch {
            result = testUseCase.execute(IllegalStateException())
        }
        coroutineScope.advanceTimeBy(10000)

        Assert.assertTrue(result is Error)
        Assert.assertTrue((result as Error).error is IllegalStateException)
    }

    @Test
    fun `given failing use case with CancellationException launched in coroutine then error is rethrown`() {
        val testUseCase = TestFailureUseCase()

        var result: Result<Unit>? = null
        coroutineScope.launch {
            result = testUseCase.execute(CancellationException())
        }
        coroutineScope.advanceTimeBy(10000)

        Assert.assertNull(result)
    }

    @Test
    fun `given success use case launched two times in coroutine then the first one is cancelled`() {
        val testUseCase = TestUseCase()

        var result: Result<Int>? = null
        coroutineScope.launch {
            testUseCase.execute(1)
            Assert.fail("Execute should be cancelled")
        }
        coroutineScope.launch {
            result = testUseCase.execute(1)
        }
        coroutineScope.advanceTimeBy(10000)

        Assert.assertEquals(Success(1), result)
    }

    @Test
    fun `given success use case launched two times with cancelPrevious set to false in coroutine then the first one is not cancelled`() {
        val testUseCase = TestUseCase()

        var result1: Result<Int>? = null
        var result2: Result<Int>? = null
        coroutineScope.launch {
            result1 = testUseCase.execute(1, cancelPrevious = false)
        }
        coroutineScope.launch {
            result2 = testUseCase.execute(2, cancelPrevious = false)
        }
        coroutineScope.advanceTimeBy(10000)

        Assert.assertEquals(Success(1), result1)
        Assert.assertEquals(Success(2), result2)
    }

    @Test
    fun `when launchWithHandler throws an exception then this exception is send to logUnhandledException and defaultErrorHandler`() {
        var logException: Throwable? = null
        var handlerException: Throwable? = null
        val testOwner = object : BaseCoroutineScopeOwnerTest() {
            override fun defaultErrorHandler(exception: Throwable) {
                handlerException = exception
            }
        }
        UseCaseErrorHandler.globalOnErrorLogger = { exception ->
            logException = exception
        }

        val exception = IllegalStateException()
        testOwner.launchWithHandler { throw exception }
        testOwner.coroutineScope.advanceTimeBy(10000)

        Assert.assertEquals(exception, logException)
        Assert.assertEquals(exception, handlerException)
    }

    @Test
    fun `when launchWithHandler throws an CancellationException then this exception is not send to logUnhandledException and defaultErrorHandler`() {
        var logException: Throwable? = null
        var handlerException: Throwable? = null
        val testOwner = object : BaseCoroutineScopeOwnerTest() {
            override fun defaultErrorHandler(exception: Throwable) {
                handlerException = exception
            }
        }
        UseCaseErrorHandler.globalOnErrorLogger = { exception ->
            logException = exception
        }

        val exception = CancellationException()
        testOwner.launchWithHandler { throw exception }
        testOwner.coroutineScope.advanceTimeBy(10000)

        Assert.assertEquals(null, logException)
        Assert.assertEquals(null, handlerException)
    }

    @Test
    fun `when launchWithHandler throws an CancellationException with non cancellation cause then this exception is send to logUnhandledException only`() {
        var logException: Throwable? = null
        var handlerException: Throwable? = null
        val testOwner = object : BaseCoroutineScopeOwnerTest() {
            override fun defaultErrorHandler(exception: Throwable) {
                handlerException = exception
            }
        }
        UseCaseErrorHandler.globalOnErrorLogger = { exception ->
            logException = exception
        }

        val exception = CancellationException("Message", cause = CancellationException("Message", cause = IllegalStateException()))
        testOwner.launchWithHandler { throw exception }
        testOwner.coroutineScope.advanceTimeBy(10000)

        Assert.assertEquals(exception, logException)
        Assert.assertEquals(null, handlerException)
    }
}
