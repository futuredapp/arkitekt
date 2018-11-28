package com.thefuntasty.interactors.vm

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.thefuntasty.interactors.BaseRxViewModel
import com.thefuntasty.interactors.base.RxMockitoJUnitRunner
import com.thefuntasty.interactors.sample.TestViewState
import com.thefuntasty.interactors.simpleSingler
import io.reactivex.Single
import org.junit.Test

class SimpleSingler : RxMockitoJUnitRunner() {

    private val mockViewState = mock<TestViewState>()

    @Test
    fun createSimpleSingler() {
        val mockVM = object : BaseRxViewModel<TestViewState>() {
            override val viewState = mockViewState

            fun runInteractors() {
                val login = simpleSingler<String> {
                    Single.just("hello")
                }

                login.execute {
                    viewState.testMethodCall3(it)
                }
            }
        }

        mockVM.runInteractors()

        verify(mockViewState).testMethodCall3("hello")
    }
}
