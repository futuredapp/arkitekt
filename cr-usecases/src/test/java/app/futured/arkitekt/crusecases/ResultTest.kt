package app.futured.arkitekt.crusecases

import app.futured.arkitekt.crusecases.Error
import app.futured.arkitekt.crusecases.Result
import app.futured.arkitekt.crusecases.Success
import app.futured.arkitekt.crusecases.fold
import app.futured.arkitekt.crusecases.getOrCancel
import app.futured.arkitekt.crusecases.getOrDefault
import app.futured.arkitekt.crusecases.getOrElse
import app.futured.arkitekt.crusecases.getOrNull
import app.futured.arkitekt.crusecases.getOrThrow
import app.futured.arkitekt.crusecases.map
import app.futured.arkitekt.crusecases.mapCatching
import app.futured.arkitekt.crusecases.recover
import app.futured.arkitekt.crusecases.recoverCatching
import app.futured.arkitekt.crusecases.tryCatch
import kotlinx.coroutines.CancellationException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class ResultTest {

    @Test
    fun `when result is Success then value component should be equal to value and error should be null`() {
        val result = Success(1)

        val (value, error) = result

        assertEquals(1, value)
        assertNull(error)
    }

    @Test
    fun `when result is Error then value component should be null value and error should be equal to exception`() {
        val exception = IllegalStateException()
        val result = Error(exception)

        val (value, cause) = result

        assertEquals(exception, cause)
        assertNull(value)
    }

    @Test
    fun `when tryCatch is called with value then result should be Success`() {
        val result = tryCatch { 1 }

        assertEquals(Success(1), result)
    }

    @Test
    fun `when tryCatch is called with exception then result should be Error`() {
        val exception = IllegalStateException()
        val result = tryCatch { throw exception }

        assertEquals(Error(exception), result)
    }

    @Test
    fun `when getOrNull is called for Success then value should be returned`() {
        val result = Success(1)

        val value = result.getOrNull()

        assertEquals(1, value)
    }

    @Test
    fun `when getOrNull is called for Error then null should be returned`() {
        val exception = IllegalStateException()
        val result = Error(exception)

        val value = result.getOrNull()

        assertNull(value)
    }

    @Test
    fun `when getOrNull is called for Error with CancellationException then exception should be rethrown`() {
        val exception = CancellationException()
        val result = Error(exception)

        assertCancellationException { result.getOrNull() }
    }

    @Test
    fun `when getOrDefault is called for Success then value should be returned`() {
        val result = Success(1)

        val value = result.getOrDefault(100)

        assertEquals(1, value)
    }

    @Test
    fun `when getOrDefault is called for Error then default value should be returned`() {
        val exception = IllegalStateException()
        val result = Error(exception)

        val value = result.getOrDefault(100)

        assertEquals(100, value)
    }

    @Test
    fun `when getOrDefault is called for Error with CancellationException then exception should be rethrown`() {
        val exception = CancellationException()
        val result = Error(exception)

        assertCancellationException { result.getOrDefault(100) }
    }

    @Test
    fun `when getOrElse is called for Success then value should be returned`() {
        val result = Success(1)

        val value = result.getOrElse { 100 }

        assertEquals(1, value)
    }

    @Test
    fun `when getOrElse is called for Error then result of else block should be returned`() {
        val exception = IllegalStateException()
        val result = Error(exception)

        val value = result.getOrElse { 100 }

        assertEquals(100, value)
    }

    @Test
    fun `when getOrElse is called for Error with CancellationException then exception should be rethrown`() {
        val exception = CancellationException()
        val result = Error(exception)

        assertCancellationException { result.getOrElse { 100 } }
    }

    @Test
    fun `when getOrThrow is called for Success then value should be returned`() {
        val result = Success(1)

        val value = result.getOrThrow()

        assertEquals(1, value)
    }

    @Test
    fun `when getOrThrow is called for Error then exception should be throw`() {
        val exception = IllegalStateException()
        val result = Error(exception) as Result<Int>

        try {
            result.getOrThrow()
            fail()
        } catch (e: Throwable) {
            assertEquals(exception, e)
        }
    }

    @Test
    fun `when getOrThrow is called for Error with CancellationException then exception should be rethrown`() {
        val exception = CancellationException()
        val result = Error(exception) as Result<Int>

        assertCancellationException { result.getOrThrow() }
    }

    @Test
    fun `when getOrCancel is called for Success then CancellationException should be throw and block should not be called`() {
        val result = Success(1)

        var blockResult: String? = null
        val value = result.getOrCancel { blockResult = "XXX" }

        assertEquals(1, value)
        assertNull(blockResult)
    }

    @Test
    fun `when getOrCancel is called for Error then CancellationException should be throw and block should be called`() {
        val exception = IllegalStateException()
        val result = Error(exception) as Result<Int>

        var blockResult: String? = null
        assertCancellationException {
            result.getOrCancel { blockResult = "XXX" }
            fail()
        }
        assertEquals("XXX", blockResult)
    }

    @Test
    fun `when getOrCancel is called for Error with CancellationException then CancellationException should be throw and block should not be called`() {
        val exception = CancellationException()
        val result = Error(exception) as Result<Int>

        var blockResult: String? = null
        assertCancellationException {
            result.getOrCancel { blockResult = "XXX" }
            fail()
        }
        assertNull(blockResult)
    }

    @Test
    fun `when map is called for Success then result should be transformed Success`() {
        val result = Success(1)

        val value = result.map { it.toString() }

        assertEquals(Success("1"), value)
    }

    @Test
    fun `when map is called for Error then the result should be the same error`() {
        val exception = IllegalStateException()
        val result = Error(exception)

        val value = result.map { it.toString() }

        assertEquals(Error(exception), value)
    }

    @Test
    fun `when mapCatching is called for Success with no exception inside block then result should be transformed Success`() {
        val result = Success(1)

        val value = result.mapCatching { it.toString() }

        assertEquals(Success("1"), value)
    }

    @Test
    fun `when mapCatching is called for Success with exception inside block then result should be Error`() {
        val exception = IllegalStateException()
        val result = Success(1)

        val value = result.mapCatching { throw exception }

        assertEquals(Error(exception), value)
    }

    @Test
    fun `when mapCatching is called for Error with exception inside block then the result should be the same error`() {
        val exception = IllegalStateException()
        val result = Error(exception)

        val value = result.mapCatching { throw RuntimeException() }

        assertEquals(Error(exception), value)
    }

    @Test
    fun `when mapCatching is called for Success with CancellationException inside block then exception should be rethrown`() {
        val result = Success(1)

        assertCancellationException { result.mapCatching { throw CancellationException() } }
    }

    @Test
    fun `when recover is called for Success then the result should be the same Success`() {
        val result = Success(1)

        val value = result.recover { 100 }

        assertEquals(Success(1), value)
    }

    @Test
    fun `when recover is called for Error with exception inside block then result should be Success`() {
        val result = Error(RuntimeException())

        val value = result.recover { 100 }

        assertEquals(Success(100), value)
    }

    @Test
    fun `when recoverCatching is called for Success then the result should be the same Success`() {
        val result = Success(1)

        val value = result.recoverCatching { 100 }

        assertEquals(Success(1), value)
    }

    @Test
    fun `when recoverCatching is called for Error with exception inside block then result should be Error`() {
        val exception = IllegalStateException()
        val result = Error(RuntimeException())

        val value = result.recoverCatching { throw exception }

        assertEquals(Error(exception), value)
    }

    @Test
    fun `when recoverCatching is called for Error with CancellationException inside block then error should be rethrown`() {
        val exception = CancellationException()
        val result = Error(RuntimeException())

        assertCancellationException { result.recoverCatching { throw exception } }
    }

    @Test
    fun `when recoverCatching is called for Error with no exception inside block then the result should be Success`() {
        val exception = IllegalStateException()
        val result = Error(exception)

        val value = result.recoverCatching { 100 }

        assertEquals(Success(100), value)
    }

    @Test
    fun `when fold is called for Success then result of onSuccess should be returned`() {
        val result = Success(1)

        val value = result.fold(onSuccess = { "OK" }, onError = { "NOT OK"} )

        assertEquals("OK", value)
    }

    @Test
    fun `when fold is called for Error then result of onError should be returned`() {
        val result = Error(IllegalStateException())

        val value = result.fold(onSuccess = { "OK" }, onError = { "NOT OK"} )

        assertEquals("NOT OK", value)
    }

    @Test
    fun `when fold is called for Error with CancellationException then exception should be rethrown`() {
        val exception = CancellationException()
        val result = Error(exception)

        assertCancellationException { result.fold(onSuccess = { "OK" }, onError = { "NOT OK"} ) }
    }

    private fun assertCancellationException(block: () -> Unit) {
        var caughtException: Throwable? = null
        try {
            block()
        } catch (e: CancellationException) {
            caughtException = e
        } catch (e: Throwable) {
            fail()
        }
        assertTrue(caughtException is CancellationException)
    }
}
