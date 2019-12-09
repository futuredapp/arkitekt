package com.thefuntasty.mvvm.usecases

import com.thefuntasty.mvvm.usecases.base.RxMockitoJUnitRunner
import com.thefuntasty.mvvm.usecases.disposables.withDisposablesOwner
import com.thefuntasty.mvvm.usecases.base.BaseCompletabler
import io.reactivex.Completable
import org.junit.Assert.assertEquals
import org.junit.Test

class InteractorHandlerTest : RxMockitoJUnitRunner() {

    @Test
    fun `interactor handler called when exception is thrown`() {
        val testException = RuntimeException("Test error")
        var globalLoggedException: Throwable? = null
        var builderException: Throwable? = null

        UseCaseErrorHandler.globalOnErrorLogger = {
            globalLoggedException = it
        }

        withDisposablesOwner {
            object : BaseCompletabler<Unit>() {
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
