package com.thefuntasty.interactors.vm

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.thefuntasty.interactors.BaseCompletabler
import com.thefuntasty.interactors.BaseRxViewModel
import com.thefuntasty.interactors.BaseSingler
import com.thefuntasty.interactors.base.RxMockitoJUnitRunner
import com.thefuntasty.interactors.sample.TestViewState
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subscribers.TestSubscriber
import org.junit.Test

class SinglerCompletablerChained : RxMockitoJUnitRunner() {

    class ProcessorCompletabler : BaseCompletabler() {

        override fun create(): Completable {
            return Completable.fromCallable {
                // something
            }
        }
    }

    class ProcessorSingler : BaseSingler<String>() {

        private var second: Long = -1

        override fun create(): Single<String> {
            return Single.fromCallable {
                "S:$second"
            }
        }
    }

    private val mockViewState = mock<TestViewState>()

    @Test
    fun checkCompletableToSingleChain() {
        val testSubscriber = TestSubscriber<String>()
        val mockVM = object : BaseRxViewModel<TestViewState>() {
            override val viewState = mockViewState

            fun runInteractors() {
                val completabler = ProcessorCompletabler()
                val singler = ProcessorSingler()

                completabler.chain(singler) {
                    mockViewState.testMethodCall()
                }.executeSubscribe(testSubscriber)
            }
        }

        mockVM.runInteractors()

        testSubscriber.await()
        testSubscriber.assertValues("S:-1")
        testSubscriber.assertComplete()

        verify(mockViewState).testMethodCall()
    }

    @Test
    fun checkSingleToCompletableChain() {
        val testSubscriber = TestSubscriber<Unit>()
        val mockVM = object : BaseRxViewModel<TestViewState>() {
            override val viewState = mockViewState

            fun runInteractors() {
                val completabler = ProcessorCompletabler()
                val singler = ProcessorSingler()

                singler.chain(completabler) { value ->
                    mockViewState.testMethodCall3(value)
                }.executeSubscribe(testSubscriber)
            }
        }

        mockVM.runInteractors()

        testSubscriber.await()
        testSubscriber.assertComplete()

        verify(mockViewState).testMethodCall3("S:-1")
    }
}
