package com.thefuntasty.interactors.vm

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.thefuntasty.interactors.BaseCompletabler
import com.thefuntasty.interactors.BaseFlowabler
import com.thefuntasty.interactors.BaseRxViewModel
import com.thefuntasty.interactors.base.RxMockitoJUnitRunner
import com.thefuntasty.interactors.sample.TestViewState
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.subscribers.TestSubscriber
import org.junit.Test
import java.util.concurrent.TimeUnit

class FlowablerCompletablerChained : RxMockitoJUnitRunner() {

    class IntervalFlowabler : BaseFlowabler<Long>() {

        override fun create(): Flowable<Long> {
            return Flowable.interval(100, TimeUnit.MILLISECONDS)
                .take(3)
        }
    }

    class ProcessorCompletabler : BaseCompletabler() {

        private var second: Long = -1

        fun init(second: Long) = apply { this.second = second }

        override fun create(): Completable {
            return Completable.fromCallable {
                // something
            }
        }
    }

    private val mockViewState = mock<TestViewState>()

    @Test
    fun checkFlowableToSingleChain() {
        val testSubscriber = TestSubscriber<Unit>()
        val mockVM = object : BaseRxViewModel<TestViewState>() {
            override val viewState = mockViewState

            fun runInteractors() {
                val interval = IntervalFlowabler()
                val processor = ProcessorCompletabler()

                interval.chain(processor) { second ->
                    viewState.testMethodCall()
                    init(second)
                }.executeSubscribe(testSubscriber)
            }
        }

        mockVM.runInteractors()

        testSubscriber.await()
        testSubscriber.assertValues(Unit, Unit, Unit)
        testSubscriber.assertComplete()

        verify(mockViewState, times(3)).testMethodCall()
    }

    @Test
    fun checkSingleToFlowableChain() {
        val testSubscriber = TestSubscriber<Long>()
        val mockVM = object : BaseRxViewModel<TestViewState>() {
            override val viewState = mockViewState

            fun runInteractors() {
                val interval = IntervalFlowabler()
                val processor = ProcessorCompletabler()

                processor.chain(interval) {
                    viewState.testMethodCall()
                }.executeSubscribe(testSubscriber)
            }
        }

        mockVM.runInteractors()

        testSubscriber.await()
        testSubscriber.assertValues(0, 1, 2)
        testSubscriber.assertComplete()

        verify(mockViewState).testMethodCall()
    }
}
