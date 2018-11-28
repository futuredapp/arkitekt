package com.thefuntasty.interactors.vm

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.thefuntasty.interactors.BaseRxViewModel
import com.thefuntasty.interactors.BaseSingler
import com.thefuntasty.interactors.base.RxMockitoJUnitRunner
import com.thefuntasty.interactors.sample.TestViewState
import com.thefuntasty.interactors.sample.User
import io.reactivex.Single
import io.reactivex.subscribers.TestSubscriber
import org.junit.Test
import java.util.concurrent.TimeUnit

class SinglersChained : RxMockitoJUnitRunner() {

    class LoginSingler : BaseSingler<User>() {

        override fun create(): Single<User> {
            return Single.fromCallable { User("TestName") }.delay(300, TimeUnit.MILLISECONDS)
        }
    }

    class AnalyticsSingler : BaseSingler<String>() {

        private lateinit var user: User

        fun init(user: User) = apply { this.user = user }

        override fun create(): Single<String> = Single.fromCallable {
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
                val login = LoginSingler()
                val analytics = AnalyticsSingler()

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
                val login = LoginSingler()
                val analytics = AnalyticsSingler()

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
                val login = LoginSingler()
                val analytics = AnalyticsSingler()

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
