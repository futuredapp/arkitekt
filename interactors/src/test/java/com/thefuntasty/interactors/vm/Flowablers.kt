package com.thefuntasty.interactors.vm

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
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

    class LoginFlowabler : BaseFlowabler<String>() {
        override fun prepare(): Flowable<String> {
            return Flowable.just("Frantisek")
        }
    }

    @Test
    fun `check multiple parameters allowed in execute`() {
        val mockVM = object : BaseRxViewModel<TestViewState>() {
            override val viewState = mockViewState

            fun runInteractors() {
                val login = LoginFlowabler()

                // just check if it can be compiled

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
    fun `check onNext and onComplete called`() {
        val mockVM = object : BaseRxViewModel<TestViewState>() {
            override val viewState = mockViewState

            fun runInteractors() {
                val login = object : BaseFlowabler<User>() {
                    override fun prepare(): Flowable<User> {
                        return Flowable.just(User("hello world")).delay(50, TimeUnit.MILLISECONDS)
                    }
                }

                login.execute({
                    viewState.testMethodCall3("onNext")
                }, {
                    // onError
                }, {
                    viewState.testMethodCall3("onComplete")
                })

            }
        }
        mockVM.runInteractors()

        verify(mockViewState).testMethodCall3("onNext")
        verify(mockViewState).testMethodCall3("onComplete")
    }

    @Test
    fun `check onNext successfully called multiple times`() {
        val mockVM = object : BaseRxViewModel<TestViewState>() {
            override val viewState = mockViewState

            fun runInteractors() {
                val interactor = object : BaseFlowabler<String>() {
                    override fun prepare(): Flowable<String> {
                        return Flowable.just("A", "B")
                    }
                }

                interactor.execute {
                    viewState.testMethodCall3("onNext:$it")
                }
            }
        }
        mockVM.runInteractors()

        verify(mockViewState).testMethodCall3("onNext:A")
        verify(mockViewState).testMethodCall3("onNext:B")
        verifyNoMoreInteractions(mockViewState)
    }
}
