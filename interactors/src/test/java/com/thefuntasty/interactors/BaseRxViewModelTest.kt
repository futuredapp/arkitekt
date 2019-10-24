package com.thefuntasty.interactors

import com.nhaarman.mockito_kotlin.mock
import com.thefuntasty.interactors.base.RxMockitoJUnitRunner
import com.thefuntasty.mvvm.ViewState
import org.junit.Test

class BaseRxViewModelTest : RxMockitoJUnitRunner() {

    private val mockViewState = mock<ViewState>()

    @Test
    fun `check disposable removed after clean`() {
//        val testSubscriber = TestSubscriber<String>()
//        val mockVM = object : BaseRxViewModel<TestViewState>() {
//            override val viewState = mockViewState
//
//            fun runTwoEmitsFlowable() = TestFlowablerFactory.twoEmits.executeSubscriber(testSubscriber)
//
//            fun clear() = onCleared()
//
//            override fun onCleared() {
//                super.onCleared()
//                viewState.testMethodCall2(disposables.size())
//            }
//        }
//
//        mockVM.runTwoEmitsFlowable()
//        testSubscriber.await()
//        mockVM.clear()
//
//        verify(mockViewState).testMethodCall2(0)
//        verifyNoMoreInteractions(mockViewState)
        // TODO
    }

    @Test
    fun `check disposable stored before completion`() {
//        val mockVM = object : BaseRxViewModel<TestViewState>() {
//            override val viewState = mockViewState
//
//            fun runNeverCompletesFlowable() = TestFlowablerFactory.neverCompletes.execute { }
//
//            fun clear() = onCleared()
//
//            override fun onCleared() {
//                viewState.testMethodCall2(disposables.size())
//                super.onCleared()
//                viewState.testMethodCall2(disposables.size())
//            }
//        }
//
//        mockVM.runNeverCompletesFlowable()
//        mockVM.clear()
//
//        // one disposable in disposables
//        verify(mockViewState).testMethodCall2(1)
//
//        // zero disposables after onCleared call
//        verify(mockViewState).testMethodCall2(0)
//        verifyNoMoreInteractions(mockViewState)
        // TODO
    }
}
