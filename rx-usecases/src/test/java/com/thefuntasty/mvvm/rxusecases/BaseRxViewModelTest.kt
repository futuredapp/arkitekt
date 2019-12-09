package com.thefuntasty.mvvm.rxusecases

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.thefuntasty.mvvm.ViewState
import com.thefuntasty.mvvm.rxusecases.usecases.CompletablerUseCase
import com.thefuntasty.mvvm.rxusecases.usecases.RxMockitoJUnitRunner
import io.reactivex.Completable
import org.junit.Test

class BaseRxViewModelTest : RxMockitoJUnitRunner() {

    class TestViewState : ViewState {
        fun fnI(num: Int) {}
    }

    private val mockViewState = mock<TestViewState>()

    @Test
    fun `check disposable removed after clear`() {
        val vm = object : BaseRxViewModel<ViewState>() {
            override val viewState = mockViewState

            fun runUseCase() {
                object : CompletablerUseCase<Unit>() {
                    override fun prepare(args: Unit) = Completable.complete()
                }.execute(Unit)
            }

            fun clear() = onCleared()

            override fun onCleared() {
                super.onCleared()
                viewState.fnI(disposables.size())
            }
        }

        vm.runUseCase()
        vm.clear()

        verify(mockViewState).fnI(0)
        verifyNoMoreInteractions(mockViewState)
    }

    @Test
    fun `check disposable stored in composite disposables before onClear`() {
        val vm = object : BaseRxViewModel<ViewState>() {
            override val viewState = mockViewState

            fun runUseCase() {
                object : CompletablerUseCase<Unit>() {
                    override fun prepare(args: Unit) = Completable.complete()
                }.execute(Unit)
            }

            fun clear() = onCleared()

            override fun onCleared() {
                viewState.fnI(disposables.size())
                super.onCleared()
                viewState.fnI(disposables.size())
            }
        }

        vm.runUseCase()
        vm.clear()

        verify(mockViewState).fnI(1)

        verify(mockViewState).fnI(0)
        verifyNoMoreInteractions(mockViewState)
    }
}
