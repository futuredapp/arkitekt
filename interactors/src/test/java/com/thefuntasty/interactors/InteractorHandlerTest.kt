package com.thefuntasty.interactors

import com.thefuntasty.interactors.base.RxMockitoJUnitRunner
import com.thefuntasty.interactors.disposables.withDisposablesOwner
import com.thefuntasty.interactors.interactors.BaseCompletabler
import io.reactivex.Completable
import org.junit.Assert.assertEquals
import org.junit.Test

class InteractorHandlerTest : RxMockitoJUnitRunner() {

    @Test
    fun `interactor handler called when exception is thrown`() {
        val testException = RuntimeException("Test error")
        var globalLoggedException: Throwable? = null
        var builderException: Throwable? = null

        InteractorErrorHandler.globalOnErrorLogger = {
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
