package com.thefuntasty.interactors.vm

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.thefuntasty.interactors.BaseFlowabler
import com.thefuntasty.interactors.BaseRxViewModel
import com.thefuntasty.interactors.base.RxMockitoJUnitRunner
import com.thefuntasty.interactors.sample.TestViewState
import com.thefuntasty.interactors.sample.User
import io.reactivex.Flowable
import io.reactivex.subscribers.TestSubscriber
import org.junit.Test
import java.util.concurrent.TimeUnit

class FlowablersChained : RxMockitoJUnitRunner() {

    class LoginFlowabler : BaseFlowabler<User>() {

        override fun create(): Flowable<User> {
            return Flowable.fromCallable { User("TestName") }.delay(10, TimeUnit.MILLISECONDS)
        }
    }

    class AnalyticsFlowabler : BaseFlowabler<String>() {

        private lateinit var user: User

        fun init(user: User) = apply { this.user = user }

        override fun create(): Flowable<String> = Flowable.fromCallable {
            user.toString()
            "Analytics stored"
        }
    }

    private val mockViewState = mock<TestViewState>()

    @Test
    fun checkChainWithInit() {
        val testSubscriber = TestSubscriber<String>()
        val mockVM = object : BaseRxViewModel<TestViewState>() {
            override val viewState = mockViewState

            fun runInteractors() {
                val login = LoginFlowabler()
                val analytics = AnalyticsFlowabler()

                login.chain(analytics) {
                    init(it)
                }.executeSubscribe(testSubscriber)
            }
        }

        mockVM.runInteractors()

        testSubscriber.await()
        testSubscriber.assertValue("Analytics stored")
        testSubscriber.assertComplete()
    }

    @Test
    fun checkChainWithMissingInit() {
        val testSubscriber = TestSubscriber<String>()
        val mockVM = object : BaseRxViewModel<TestViewState>() {
            override val viewState = mockViewState

            fun runInteractors() {
                val login = LoginFlowabler()
                val analytics = AnalyticsFlowabler()

                login.chain(analytics).executeSubscribe(testSubscriber)
            }
        }

        mockVM.runInteractors()

        testSubscriber.await()
        testSubscriber.assertError(RuntimeException::class.java)
    }

    @Test
    fun checkOnlyOneDisposableCreated() {
        val testSubscriber = TestSubscriber<String>()
        val mockVM = object : BaseRxViewModel<TestViewState>() {
            override val viewState = mockViewState

            fun runInteractors() {
                val login = LoginFlowabler()
                val analytics = AnalyticsFlowabler()

                login.chain(analytics) { init(it) }.executeSubscribe(testSubscriber)
            }

            fun clear() {
                onCleared()
            }

            override fun onCleared() {
                viewState.testMethodCall2(disposables.size())
                super.onCleared()
            }
        }

        mockVM.runInteractors()
        testSubscriber.await()
        mockVM.clear()

        verify(mockViewState).testMethodCall2(1)
    }
}
