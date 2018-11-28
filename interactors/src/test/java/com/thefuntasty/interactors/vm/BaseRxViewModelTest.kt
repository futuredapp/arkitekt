package com.thefuntasty.interactors.vm

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.thefuntasty.interactors.BaseRxViewModel
import com.thefuntasty.interactors.base.RxMockitoJUnitRunner
import com.thefuntasty.interactors.sample.TestFlowablerFactory
import com.thefuntasty.interactors.sample.TestViewState
import io.reactivex.subscribers.TestSubscriber
import org.junit.Test

class BaseRxViewModelTest : RxMockitoJUnitRunner() {

    private val mockViewState = mock<TestViewState>()

    @Test
    fun checkCorrectNumberOfTestMethodCalls() {
        val mockVM = object : BaseRxViewModel<TestViewState>() {
            override val viewState = mockViewState

            fun runTwoEmitsFlowable() = TestFlowablerFactory.twoEmits.execute({ viewState.testMethodCall() })
        }

        mockVM.runTwoEmitsFlowable()

        verify(mockViewState, times(2)).testMethodCall()
        verifyNoMoreInteractions(mockViewState)
    }

    @Test
    fun checkIfDisposableRemovedAfterOnClear() {
        val testSubscriber = TestSubscriber<String>()
        val mockVM = object : BaseRxViewModel<TestViewState>() {
            override val viewState = mockViewState

            fun runTwoEmitsFlowable() = TestFlowablerFactory.twoEmits.executeSubscribe(testSubscriber)

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
    fun checkIfDisposableStoredBeforeCompleted() {
        val mockVM = object : BaseRxViewModel<TestViewState>() {
            override val viewState = mockViewState

            fun runNeverCompletesFlowable() = TestFlowablerFactory.neverCompletes.execute({ })

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
}
