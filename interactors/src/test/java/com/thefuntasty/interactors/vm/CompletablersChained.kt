package com.thefuntasty.interactors.vm

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.thefuntasty.interactors.BaseCompletabler
import com.thefuntasty.interactors.BaseRxViewModel
import com.thefuntasty.interactors.base.RxMockitoJUnitRunner
import com.thefuntasty.interactors.sample.TestViewState
import io.reactivex.Completable
import io.reactivex.subscribers.TestSubscriber
import org.junit.Test

class CompletablersChained : RxMockitoJUnitRunner() {

    class LoginCompletabler : BaseCompletabler() {

        override fun create(): Completable {
            return Completable.complete()
        }
    }

    class AnalyticsCompletabler : BaseCompletabler() {

        override fun create(): Completable {
            return Completable.complete()
        }
    }

    private val mockViewState = mock<TestViewState>()

    @Test
    fun checkCompletablersChained() {
        val testSubscriber = TestSubscriber<Unit>()
        val mockVM = object : BaseRxViewModel<TestViewState>() {
            override val viewState = mockViewState

            fun runInteractors() {
                val login = LoginCompletabler()
                val analytics = AnalyticsCompletabler()

                login.chain(analytics).executeSubscribe(testSubscriber)
            }
        }

        mockVM.runInteractors()

        testSubscriber.await()
        testSubscriber.assertComplete()
    }

    @Test
    fun checkCompletablersChainedErrorFirst() {
        val testSubscriber = TestSubscriber<Unit>()
        val mockVM = object : BaseRxViewModel<TestViewState>() {
            override val viewState = mockViewState

            fun runInteractors() {
                val login = object : BaseCompletabler() {
                    override fun create(): Completable = Completable.error(IllegalStateException())
                }
                val analytics = AnalyticsCompletabler()

                login.chain(analytics).executeSubscribe(testSubscriber)
            }
        }

        mockVM.runInteractors()

        testSubscriber.await()
        testSubscriber.assertError(IllegalStateException::class.java)
    }

    @Test
    fun checkCompletablersChainedErrorSecond() {
        val testSubscriber = TestSubscriber<Unit>()
        val mockVM = object : BaseRxViewModel<TestViewState>() {
            override val viewState = mockViewState

            fun runInteractors() {
                val login = LoginCompletabler()
                val analytics = object : BaseCompletabler() {
                    override fun create(): Completable = Completable.error(IllegalStateException())
                }

                login.chain(analytics) {
                    viewState.testMethodCall()
                }.executeSubscribe(testSubscriber)
            }
        }

        mockVM.runInteractors()

        testSubscriber.await()
        testSubscriber.assertError(IllegalStateException::class.java)

        verify(mockViewState).testMethodCall()
        verifyNoMoreInteractions(mockViewState)
    }
}
