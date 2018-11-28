package com.thefuntasty.interactors.vm

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.thefuntasty.interactors.BaseFlowabler
import com.thefuntasty.interactors.BaseRxViewModel
import com.thefuntasty.interactors.base.RxMockitoJUnitRunner
import com.thefuntasty.interactors.sample.TestViewState
import com.thefuntasty.interactors.sample.User
import io.reactivex.Flowable
import org.junit.Test
import java.util.concurrent.TimeUnit

class Flowablers : RxMockitoJUnitRunner() {

    private val mockViewState = mock<TestViewState>()

    @Test
    fun checkMultipleParametersAllowed() {
        val mockVM = object : BaseRxViewModel<TestViewState>() {
            override val viewState = mockViewState

            fun runInteractors() {
                val login = FlowablersChained.LoginFlowabler()

                login.execute {
                    // onNext
                }

                login.execute({
                    // onNext
                }, {
                    // onError
                })

                login.execute({
                    // onNext
                }, {
                    // onError
                }, {
                    // onComplete
                })
            }
        }
        mockVM.runInteractors()
    }

    @Test
    fun checkOnNextOnCompleteCalled() {
        val mockVM = object : BaseRxViewModel<TestViewState>() {
            override val viewState = mockViewState

            fun runInteractors() {
                val login = object : BaseFlowabler<User>() {
                    override fun create(): Flowable<User> {
                        return Flowable.just(User("hello world")).delay(50, TimeUnit.MILLISECONDS)
                    }
                }

                login.execute({
                    viewState.testMethodCall3("onNext")
                }, {
                    // onError
                }, {
                    // onComplete
                    viewState.testMethodCall3("onComplete")
                })
            }
        }
        mockVM.runInteractors()

        verify(mockViewState).testMethodCall3("onNext")
        verify(mockViewState).testMethodCall3("onComplete")
    }
}
