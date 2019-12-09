package com.thefuntasty.mvvm.rxusecases

import com.thefuntasty.mvvm.rxusecases.disposables.withDisposablesOwner
import com.thefuntasty.mvvm.rxusecases.usecases.CompletablerUseCase
import com.thefuntasty.mvvm.rxusecases.usecases.RxMockitoJUnitRunner
import io.reactivex.Completable
import org.junit.Assert.assertEquals
import org.junit.Test

class UseCaseHandlerTest : RxMockitoJUnitRunner() {

    @Test
    fun `use case handler called when exception is thrown`() {
        val testException = RuntimeException("Test error")
        var globalLoggedException: Throwable? = null
        var builderException: Throwable? = null

        UseCaseErrorHandler.globalOnErrorLogger = {
            globalLoggedException = it
        }

        withDisposablesOwner {
            object : CompletablerUseCase<Unit>() {
                override fun prepare(args: Unit): Completable {
                    return Completable.error(testException)
                }
            }.execute(Unit) {
                onError { builderException = it }
            }

            assertEquals(testException, globalLoggedException)
            assertEquals(testException, builderException)
        }
    }
}
