package app.futured.arkitekt.rxusecases.test

import app.futured.arkitekt.rxusecases.test.mockExecute
import app.futured.arkitekt.rxusecases.test.mockExecuteNullable
import app.futured.arkitekt.rxusecases.test.testutils.BaseTest
import app.futured.arkitekt.rxusecases.test.testutils.TestDisposablesOwner
import app.futured.arkitekt.rxusecases.usecases.CompletableUseCase
import io.mockk.mockk
import io.reactivex.Completable
import org.junit.Assert.assertEquals
import org.junit.Test

class CompletableUseCaseMethodsTests : BaseTest() {

    class TestUseCase : CompletableUseCase<String>() {
        override fun prepare(args: String): Completable {
            throw IllegalStateException("THIS SHOULD NOT BE CALLED")
        }
    }

    class TestUseCaseNullable : CompletableUseCase<String?>() {
        override fun prepare(args: String?): Completable {
            throw IllegalStateException("THIS SHOULD NOT BE CALLED")
        }
    }

    private val mockUseCase: TestUseCase = mockk()
    private val mockUseCaseNullable: TestUseCaseNullable = mockk()

    private val args: String = "INPUT"
    private val argsNullable: String? = "INPUT"
    private val expectedResult = "RESULT"
    private val initialValue = "INITIAL VALUE"

    @Test
    fun `when use case is mocked with just value then expected value should be returned`() {
        // GIVEN
        mockUseCase.mockExecute(args) { Completable.complete() }

        // WHEN
        val result = executeAndReturnResult()

        // THEN
        assertEquals(expectedResult, result)
    }

    @Test
    fun `when use case is mocked with just value and without args then expected value should be returned`() {
        // GIVEN
        mockUseCase.mockExecute { Completable.complete() }

        // WHEN
        val result = executeAndReturnResult()

        // THEN
        assertEquals(expectedResult, result)
    }

    @Test
    fun `when nullable use case is mocked with just value then expected value should be returned`() {
        // GIVEN
        mockUseCaseNullable.mockExecuteNullable(argsNullable) { Completable.complete() }

        // WHEN
        val result = executeNullableAndReturnResult()

        // THEN
        assertEquals(expectedResult, result)
    }

    @Test
    fun `when nullable use case is mocked with just value and without args then expected value should be returned`() {
        // GIVEN
        mockUseCaseNullable.mockExecuteNullable { Completable.complete() }

        // WHEN
        val result = executeNullableAndReturnResult()

        // THEN
        assertEquals(expectedResult, result)
    }

    @Test
    fun `when nullable use case is mocked with null value then expected value should be returned`() {
        // GIVEN
        mockUseCaseNullable.mockExecuteNullable(null) { Completable.complete() }

        // WHEN
        val result = executeNullAndReturnResult()

        // THEN
        assertEquals(expectedResult, result)
    }

    private fun executeAndReturnResult(): String? {
        var result: String? = initialValue
        with(TestDisposablesOwner()) {
            mockUseCase.execute(args) {
                onComplete { result = expectedResult }
                onError { result = it.localizedMessage }
            }
        }
        return result
    }

    private fun executeNullableAndReturnResult(): String? {
        var result: String? = initialValue
        with(TestDisposablesOwner()) {
            mockUseCaseNullable.execute(argsNullable) {
                onComplete { result = expectedResult }
                onError { result = it.localizedMessage }
            }
        }
        return result
    }

    private fun executeNullAndReturnResult(): String? {
        var result: String? = initialValue
        with(TestDisposablesOwner()) {
            mockUseCaseNullable.execute(null) {
                onComplete { result = expectedResult }
                onError { result = it.localizedMessage }
            }
        }
        return result
    }
}
