package com.thefuntasty.mvvm.crusecases.test

import com.thefuntasty.mvvm.crusecases.FlowUseCase
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class FlowUseCaseTests {

    class TestUseCase : FlowUseCase<String, String>() {
        override fun build(args: String): Flow<String> {
            throw IllegalStateException("THIS SHOULD NOT BE CALLED")
        }
    }

    class TestUseCaseNullable : FlowUseCase<String?, String>() {
        override fun build(args: String?): Flow<String> {
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

    @Test
    fun `when use case is mocked then expected value should be returned`() {
        // GIVEN
        mockUseCase.mockExecute(args) { flowOf(expectedResult) }

        // WHEN
        val result = executeAndReturnResult()

        // THEN
        assertEquals(expectedResult, result)
    }

    @Test
    fun `when use case is mocked and without argument then expected value should be returned`() {
        // GIVEN
        mockUseCase.mockExecute { flowOf(expectedResult) }

        // WHEN
        val result = executeAndReturnResult()

        // THEN
        assertEquals(expectedResult, result)
    }

    @Test
    fun `when nullable use case is mocked then expected value should be returned`() {
        // GIVEN
        mockUseCaseNullable.mockExecuteNullable(args) { flowOf(expectedResult) }

        // WHEN
        val result = executeNullableAndReturnResult()

        // THEN
        assertEquals(expectedResult, result)
    }

    @Test
    fun `when nullable use case is mocked and with null argument then expected value should be returned`() {
        // GIVEN
        mockUseCaseNullable.mockExecuteNullable(null) { flowOf(expectedResult) }

        // WHEN
        val result = executeNullAndReturnResult()

        // THEN
        assertEquals(expectedResult, result)
    }

    @Test
    fun `when nullable use case is mocked and without argument then expected value should be returned`() {
        // GIVEN
        mockUseCaseNullable.mockExecuteNullable { flowOf(expectedResult) }

        // WHEN
        val result = executeNullableAndReturnResult()

        // THEN
        assertEquals(expectedResult, result)
    }

    @Test
    fun `when use case is mocked with error then error value should be returned`() {
        // GIVEN
        mockUseCase.mockExecute(args) { flow { throw IllegalStateException() } }

        // WHEN
        val result = executeAndReturnResult()

        // THEN
        assertEquals(errorValue, result)
    }

    private fun executeAndReturnResult(): String? {
        var result: String? = initialValue
        with(TestCoroutineScopeOwner()) {
            mockUseCase.execute(args) {
                onNext { result = it }
                onError { result = errorValue }
            }
        }
        return result
    }

    private fun executeNullableAndReturnResult(): String? {
        var result: String? = initialValue
        with(TestCoroutineScopeOwner()) {
            mockUseCaseNullable.execute(argsNullable) {
                onNext { result = it }
                onError { result = errorValue }
            }
        }
        return result
    }

    private fun executeNullAndReturnResult(): String? {
        var result: String? = initialValue
        with(TestCoroutineScopeOwner()) {
            mockUseCaseNullable.execute(null) {
                onNext { result = it }
                onError { result = errorValue }
            }
        }
        return result
    }
}
