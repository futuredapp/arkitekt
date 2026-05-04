package app.futured.arkitekt.crusecases.test

import app.futured.arkitekt.crusecases.CoroutineScopeOwner
import app.futured.arkitekt.crusecases.FlowUseCase
import app.futured.arkitekt.crusecases.UseCase
import app.futured.arkitekt.crusecases.error.UseCaseErrorHandler
import app.futured.arkitekt.crusecases.execute
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CoroutineScopeOwnerTest {

    class TestUseCase : UseCase<Int, Int> {
        override suspend fun build(args: Int): Int {
            delay(1000)
            return args
        }
    }

    class TestFailureUseCase : UseCase<Throwable, Unit> {
        override suspend fun build(args: Throwable): Unit = throw args
    }

    class TestFlowUseCase : FlowUseCase<TestFlowUseCase.Data, Int> {
        data class Data(val listToEmit: List<Int>, val delayBetweenEmits: Long)

        override fun build(args: Data): Flow<Int> =
            args.listToEmit.asFlow().onEach { delay(args.delayBetweenEmits) }
    }

    class TestFailureFlowUseCase : FlowUseCase<Throwable, Unit> {
        override fun build(args: Throwable): Flow<Unit> = flow { throw args }
    }

    private lateinit var testOwner: TestCoroutineScopeOwner

    @Before
    fun setUp() {
        UseCaseErrorHandler.globalOnErrorLogger = {}
        testOwner = TestCoroutineScopeOwner()
        Dispatchers.setMain(testOwner.testDispatcher)
    }

    @After
    fun tearDown() {
        UseCaseErrorHandler.globalOnErrorLogger = {}
        Dispatchers.resetMain()
    }

    @Test
    fun `given 1s delay use case when executed two times then first execution cancelled`() {
        val testUseCase = TestUseCase()
        var executionCount = 0

        with(testOwner) {
            testUseCase.execute(1) {
                onSuccess { executionCount++ }
                onError { fail("Exception thrown where shouldn't") }
            }
            useCaseScope.advanceTimeByCompat(500)

            testUseCase.execute(1) {
                onSuccess { executionCount++ }
                onError { fail("Exception thrown where shouldn't") }
            }
            useCaseScope.advanceTimeByCompat(1000)
        }

        assertEquals(1, executionCount)
    }

    @Test
    fun `given failing test use case when executed then indicates onError`() {
        var resultError: Throwable? = null

        with(testOwner) {
            TestFailureUseCase().execute(IllegalStateException()) {
                onError { resultError = it }
            }
            useCaseScope.advanceTimeByCompat(1000)
        }

        assertNotNull(resultError)
    }

    @Test
    fun `given test flow use case when executed then emits all items`() {
        val testingList = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        val resultList = mutableListOf<Int>()

        with(testOwner) {
            TestFlowUseCase().execute(TestFlowUseCase.Data(testingList, 1000)) {
                onNext { resultList.add(it) }
                onError { fail("Exception thrown where shouldn't") }
            }
            useCaseScope.advanceTimeByCompat(10000)
        }

        assertEquals(testingList, resultList)
    }

    @Test
    fun `given test flow use case when executed and all items emitted then completes`() {
        val testingList = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        var completed = false

        with(testOwner) {
            TestFlowUseCase().execute(TestFlowUseCase.Data(testingList, 1000)) {
                onError { fail("Exception thrown where shouldn't") }
                onComplete { completed = true }
            }
            useCaseScope.advanceTimeByCompat(10000)
        }

        assertEquals(true, completed)
    }

    @Test
    fun `given failing flow use case when executed then indicates onError`() {
        var resultError: Throwable? = null

        with(testOwner) {
            TestFailureFlowUseCase().execute(IllegalStateException()) {
                onError { resultError = it }
                onComplete { fail("onComplete called where shouldn't") }
            }
            useCaseScope.advanceTimeByCompat(1000)
        }

        assertNotNull(resultError)
    }

    @Test
    fun `given success use case launched in coroutine then result is set to success`() {
        val testUseCase = TestUseCase()
        var result: Result<Int>? = null

        with(testOwner) {
            useCaseScope.launch {
                result = testUseCase.execute(1)
            }
            useCaseScope.advanceTimeByCompat(10000)
        }

        assertEquals(Result.success(1), result)
    }

    @Test
    fun `given failing use case launched in coroutine then result is set to error`() {
        var result: Result<Unit>? = null

        with(testOwner) {
            useCaseScope.launch {
                result = TestFailureUseCase().execute(IllegalStateException())
            }
            useCaseScope.advanceTimeByCompat(10000)
        }

        assertTrue(result?.isFailure == true)
        assertTrue((result?.exceptionOrNull()) is IllegalStateException)
    }

    @Test
    fun `given failing use case with CancellationException launched in coroutine then error is rethrown`() {
        var result: Result<Unit>? = null

        with(testOwner) {
            useCaseScope.launch {
                result = TestFailureUseCase().execute(CancellationException())
            }
            useCaseScope.advanceTimeByCompat(10000)
        }

        assertNull(result)
    }

    @Test
    fun `given success use case launched two times in coroutine then the first one is cancelled`() {
        val testUseCase = TestUseCase()
        var result: Result<Int>? = null

        with(testOwner) {
            useCaseScope.launch {
                testUseCase.execute(1)
                fail("Execute should be cancelled")
            }
            useCaseScope.launch {
                result = testUseCase.execute(1)
            }
            useCaseScope.advanceTimeByCompat(10000)
        }

        assertEquals(Result.success(1), result)
    }

    @Test
    fun `given success use case launched two times with cancelPrevious set to false in coroutine then the first one is not cancelled`() {
        val testUseCase = TestUseCase()
        var result1: Result<Int>? = null
        var result2: Result<Int>? = null

        with(testOwner) {
            useCaseScope.launch {
                result1 = testUseCase.execute(1, cancelPrevious = false)
            }
            useCaseScope.launch {
                result2 = testUseCase.execute(2, cancelPrevious = false)
            }
            useCaseScope.advanceTimeByCompat(10000)
        }

        assertEquals(Result.success(1), result1)
        assertEquals(Result.success(2), result2)
    }

    @Test
    fun `when launchWithHandler throws an exception then this exception is send to logUnhandledException and defaultErrorHandler`() {
        var logException: Throwable? = null
        var handlerException: Throwable? = null
        val ownerDispatcher = StandardTestDispatcher()
        val ownerScope = TestScope(ownerDispatcher)
        val owner = object : CoroutineScopeOwner {
            override val useCaseScope: CoroutineScope = ownerScope
            override val useCaseJobPool: MutableMap<Any, Job> = mutableMapOf()
            override fun getWorkerDispatcher() = ownerDispatcher
            override fun defaultErrorHandler(exception: Throwable) {
                handlerException = exception
            }
        }
        Dispatchers.setMain(ownerDispatcher)
        UseCaseErrorHandler.globalOnErrorLogger = { logException = it }

        val exception = IllegalStateException()
        owner.launchWithHandler { throw exception }
        ownerScope.advanceTimeByCompat(10000)

        assertEquals(exception, logException)
        assertEquals(exception, handlerException)
    }

    @Test
    fun `when launchWithHandler throws a CancellationException then this exception is not send to logUnhandledException and defaultErrorHandler`() {
        var logException: Throwable? = null
        var handlerException: Throwable? = null
        val ownerDispatcher = StandardTestDispatcher()
        val ownerScope = TestScope(ownerDispatcher)
        val owner = object : CoroutineScopeOwner {
            override val useCaseScope: CoroutineScope = ownerScope
            override val useCaseJobPool: MutableMap<Any, Job> = mutableMapOf()
            override fun getWorkerDispatcher() = ownerDispatcher
            override fun defaultErrorHandler(exception: Throwable) {
                handlerException = exception
            }
        }
        Dispatchers.setMain(ownerDispatcher)
        UseCaseErrorHandler.globalOnErrorLogger = { logException = it }

        val exception = CancellationException()
        owner.launchWithHandler { throw exception }
        ownerScope.advanceTimeByCompat(10000)

        assertEquals(null, logException)
        assertEquals(null, handlerException)
    }

    @Test
    fun `when launchWithHandler throws a CancellationException with non-cancellation cause then exception is sent to logUnhandledException only`() {
        var logException: Throwable? = null
        var handlerException: Throwable? = null
        val ownerDispatcher = StandardTestDispatcher()
        val ownerScope = TestScope(ownerDispatcher)
        val owner = object : CoroutineScopeOwner {
            override val useCaseScope: CoroutineScope = ownerScope
            override val useCaseJobPool: MutableMap<Any, Job> = mutableMapOf()
            override fun getWorkerDispatcher() = ownerDispatcher
            override fun defaultErrorHandler(exception: Throwable) {
                handlerException = exception
            }
        }
        Dispatchers.setMain(ownerDispatcher)
        UseCaseErrorHandler.globalOnErrorLogger = { logException = it }

        val exception = CancellationException("Message", cause = CancellationException("Message", cause = IllegalStateException()))
        owner.launchWithHandler { throw exception }
        ownerScope.advanceTimeByCompat(10000)

        assertEquals(exception, logException)
        assertEquals(null, handlerException)
    }

    @Test
    fun `when useCase is executed and onError is called then globalOnErrorLogger is called`() {
        var logException: Throwable? = null
        var resultError: Throwable? = null
        UseCaseErrorHandler.globalOnErrorLogger = { logException = it }

        with(testOwner) {
            TestFailureUseCase().execute(IllegalStateException()) {
                onError { resultError = it }
            }
            useCaseScope.advanceTimeByCompat(10000)
        }

        assertTrue(resultError is IllegalStateException)
        assertTrue(logException is IllegalStateException)
    }

    @Test
    fun `when flowUseCase is executed and onError is called then globalOnErrorLogger is called`() {
        var logException: Throwable? = null
        var resultError: Throwable? = null
        UseCaseErrorHandler.globalOnErrorLogger = { logException = it }

        with(testOwner) {
            TestFailureFlowUseCase().execute(IllegalStateException()) {
                onError { resultError = it }
            }
            useCaseScope.advanceTimeByCompat(10000)
        }

        assertTrue(resultError is IllegalStateException)
        assertTrue(logException is IllegalStateException)
    }

    private fun TestScope.advanceTimeByCompat(delayTimeMillis: Long) {
        testScheduler.apply { advanceTimeBy(delayTimeMillis); runCurrent() }
    }
}
