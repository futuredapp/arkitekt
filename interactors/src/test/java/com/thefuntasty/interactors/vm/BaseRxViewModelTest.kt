package com.thefuntasty.interactors.vm

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.thefuntasty.interactors.BaseRxViewModel
import com.thefuntasty.interactors.BaseSingler
import com.thefuntasty.interactors.base.RxMockitoJUnitRunner
import com.thefuntasty.interactors.sample.TestFlowablerFactory
import com.thefuntasty.interactors.sample.TestViewState
import io.reactivex.Single
import io.reactivex.subscribers.TestSubscriber
import org.junit.Test

class BaseRxViewModelTest : RxMockitoJUnitRunner() {

    private val mockViewState = mock<TestViewState>()

    @Test
    fun `check correct number of onNext calls`() {
        val mockVM = object : BaseRxViewModel<TestViewState>() {
            override val viewState = mockViewState

            fun runTwoEmitsFlowable() = TestFlowablerFactory.twoEmits.execute { viewState.testMethodCall() }
        }

        mockVM.runTwoEmitsFlowable()

        verify(mockViewState, times(2)).testMethodCall()
        verifyNoMoreInteractions(mockViewState)
    }

    @Test
    fun `check disposable removed after clean`() {
        val testSubscriber = TestSubscriber<String>()
        val mockVM = object : BaseRxViewModel<TestViewState>() {
            override val viewState = mockViewState

            fun runTwoEmitsFlowable() = TestFlowablerFactory.twoEmits.executeSubscriber(testSubscriber)

            fun clear() = onCleared()

            override fun onCleared() {
                super.onCleared()
                viewState.testMethodCall2(disposables.size())
            }
        }

        mockVM.runTwoEmitsFlowable()
        testSubscriber.await()
        mockVM.clear()

        verify(mockViewState).testMethodCall2(0)
        verifyNoMoreInteractions(mockViewState)
    }

    @Test
    fun `check disposable stored before completion`() {
        val mockVM = object : BaseRxViewModel<TestViewState>() {
            override val viewState = mockViewState

            fun runNeverCompletesFlowable() = TestFlowablerFactory.neverCompletes.execute { }

            fun clear() = onCleared()

            override fun onCleared() {
                viewState.testMethodCall2(disposables.size())
                super.onCleared()
                viewState.testMethodCall2(disposables.size())
            }
        }

        mockVM.runNeverCompletesFlowable()
        mockVM.clear()

        // one disposable in disposables
        verify(mockViewState).testMethodCall2(1)

        // zero disposables after onCleared call
        verify(mockViewState).testMethodCall2(0)
        verifyNoMoreInteractions(mockViewState)
    }

    @Test
    fun `check interactor init on single instance`() {
        val interactor = object : BaseSingler<String>() {

            private lateinit var str: String

            fun init(str: String) = apply {
                this.str = str
            }

            override fun prepare(): Single<String> {
                return Single.fromCallable { str }
            }
        }

        val testSubscriber = TestSubscriber<String>()

        val mockVM = object : BaseRxViewModel<TestViewState>() {
            override val viewState = mockViewState

            fun runInteractor() = interactor.init("A").executeSubscriber(testSubscriber)
        }

        mockVM.runInteractor()

        testSubscriber.assertValue("A")
        testSubscriber.assertComplete()
    }

    @Test
    fun `check interactor init on multiple instances`() {
        val interactor = object : BaseSingler<String>() {

            private lateinit var str: String

            fun init(str: String) = apply {
                this.str = str
            }

            override fun prepare(): Single<String> {
                return Single.fromCallable { str }
            }
        }

        val testSubscriber = TestSubscriber<String>()
        val testSubscriber2 = TestSubscriber<String>()

        val mockVM = object : BaseRxViewModel<TestViewState>() {
            override val viewState = mockViewState

            fun runInteractor() {
                interactor.init("A").executeSubscriber(testSubscriber)
                interactor.init("B").executeSubscriber(testSubscriber2)
            }
        }

        mockVM.runInteractor()

        testSubscriber.assertValue("A")
        testSubscriber.assertComplete()
        testSubscriber2.assertValue("B")
        testSubscriber2.assertComplete()
    }

    @Test
    fun `check interactor init on multiple instances mixed`() {
        val interactor = object : BaseSingler<String>() {

            private lateinit var str: String

            fun init(str: String) = apply {
                this.str = str
            }

            override fun prepare(): Single<String> {
                val s = str
                return Single.fromCallable { s }
            }
        }

        val testSubscriber = TestSubscriber<String>()
        val testSubscriber2 = TestSubscriber<String>()

        val mockVM = object : BaseRxViewModel<TestViewState>() {
            override val viewState = mockViewState

            fun runInteractor() {
                val temp = interactor.init("A").stream()

                interactor.init("B").executeSubscriber(testSubscriber2)

                temp.toFlowable().subscribe(testSubscriber)
            }
        }

        mockVM.runInteractor()

        testSubscriber.assertValue("A")
        testSubscriber.assertComplete()
        testSubscriber2.assertValue("B")
        testSubscriber2.assertComplete()
    }
}
