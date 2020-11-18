package app.futured.arkitekt.crusecases.test

import app.futured.arkitekt.crusecases.UseCase
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UseCaseTests {

    class TestUseCase : UseCase<String, String>() {
        override suspend fun build(args: String): String {
            throw IllegalStateException("THIS SHOULD NOT BE CALLED")
        }
    }

    class TestUseCaseNullable : UseCase<String?, String>() {
        override suspend fun build(args: String?): String {
            throw IllegalStateException("THIS SHOULD NOT BE CALLED")
        }
    }

    private val mockUseCase: TestUseCase = mockk()
    private val mockUseCaseNullable: TestUseCaseNullable = mockk()

    private val args: String = "INPUT"
    private val argsNullable: String? = "INPUT"
    private val expectedResult = "RESULT"
    private val initialValue = "INITIAL VALUE"
    private val errorValue = "ERROR VALUE"

    private lateinit var testCoroutineScopeOwner: TestCoroutineScopeOwner

    @Before
    fun setUp() {
        testCoroutineScopeOwner = TestCoroutineScopeOwner()
        Dispatchers.setMain(testCoroutineScopeOwner.testDispatcher)
    }

    @After
    fun tearDown() {
        testCoroutineScopeOwner.coroutineScope.cleanupTestCoroutines()
        Dispatchers.resetMain()
    }

    @Test
    fun `when use case is mocked then expected value should be returned`() {
        // GIVEN
        mockUseCase.mockExecute(args) { expectedResult }

        // WHEN
        val result = executeAndReturnResult()

        // THEN
        assertEquals(expectedResult, result)
    }

    @Test
    fun `when use case is mocked and without argument then expected value should be returned`() {
        // GIVEN
        mockUseCase.mockExecute { expectedResult }

        // WHEN
        val result = executeAndReturnResult()

        // THEN
        assertEquals(expectedResult, result)
    }

    @Test
    fun `when nullable use case is mocked then expected value should be returned`() {
        // GIVEN
        mockUseCaseNullable.mockExecuteNullable(args) { expectedResult }

        // WHEN
        val result = executeNullableAndReturnResult()

        // THEN
        assertEquals(expectedResult, result)
    }

    @Test
    fun `when nullable use case is mocked and with null argument then expected value should be returned`() {
        // GIVEN
        mockUseCaseNullable.mockExecuteNullable(null) { expectedResult }

        // WHEN
        val result = executeNullAndReturnResult()

        // THEN
        assertEquals(expectedResult, result)
    }

    @Test
    fun `when nullable use case is mocked and without argument then expected value should be returned`() {
        // GIVEN
        mockUseCaseNullable.mockExecuteNullable { expectedResult }

        // WHEN
        val result = executeNullableAndReturnResult()

        // THEN
        assertEquals(expectedResult, result)
    }

    @Test
    fun `when use case is mocked with error then expected value should be set to error`() {
        // GIVEN
        mockUseCase.mockExecute(args) { throw IllegalStateException() }

        // WHEN
        val result = executeAndReturnResult()

        // THEN
        assertEquals(errorValue, result)
    }

    private fun executeAndReturnResult(): String? {
        var result: String? = initialValue
        with(testCoroutineScopeOwner) {
            mockUseCase.execute(args) {
                onSuccess { result = it }
                onError { result = errorValue }
            }
        }
        return result
    }

    private fun executeNullableAndReturnResult(): String? {
        var result: String? = initialValue
        with(testCoroutineScopeOwner) {
            mockUseCaseNullable.execute(argsNullable) {
                onSuccess { result = it }
                onError { result = errorValue }
            }
        }
        return result
    }

    private fun executeNullAndReturnResult(): String? {
        var result: String? = initialValue
        with(testCoroutineScopeOwner) {
            mockUseCaseNullable.execute(null) {
                onSuccess { result = it }
                onError { result = errorValue }
            }
        }
        return result
    }
}
