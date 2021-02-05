package app.futured.arkitekt.crusecases.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class ThrowableExtensionsTests {

    @Test
    fun `when root cause exists then rootCause should return it`() {
        val rootException = ArithmeticException()
        val exception = RuntimeException(IllegalStateException(rootException))

        assertEquals(rootException, exception.rootCause)
    }

    @Test
    fun `when root cause does not exists then rootCause should return null`() {
        val exception = RuntimeException()

        assertEquals(null, exception.rootCause)
    }
}
