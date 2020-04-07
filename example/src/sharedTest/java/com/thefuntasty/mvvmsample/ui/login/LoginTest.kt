package com.thefuntasty.mvvmsample.ui.login

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.thefuntasty.mvvm.rxusecases.test.mockExecute
import app.futured.arkitekt.core.view.asProvider
import app.futured.arkitekt.core.view.doAfterActivityInjection
import app.futured.arkitekt.core.view.doAfterFragmentInjection
import com.thefuntasty.mvvmsample.R
import com.thefuntasty.mvvmsample.domain.GetStateUseCase
import com.thefuntasty.mvvmsample.domain.ObserveUserFullNameUseCase
import com.thefuntasty.mvvmsample.domain.SyncLoginUseCase
import com.thefuntasty.mvvmsample.tools.ToastCreator
import com.thefuntasty.mvvmsample.ui.login.activity.LoginActivity
import com.thefuntasty.mvvmsample.ui.login.fragment.LoginFragment
import com.thefuntasty.mvvmsample.ui.login.fragment.LoginViewModel
import com.thefuntasty.mvvmsample.ui.login.fragment.LoginViewModelFactory
import com.thefuntasty.mvvmsample.ui.login.fragment.LoginViewState
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import org.hamcrest.Matchers.containsString
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.thefuntasty.mvvmsample.ui.login.activity.LoginViewModel as ActivityLoginViewModel
import com.thefuntasty.mvvmsample.ui.login.activity.LoginViewModelFactory as ActivityLoginViewModelFactory
import com.thefuntasty.mvvmsample.ui.login.activity.LoginViewState as ActivityLoginViewState

@RunWith(AndroidJUnit4::class)
class LoginTest {

    private val mockLoginCompletabler: SyncLoginUseCase = mockk()
    private val mockObserveUserFullNameUseCase: ObserveUserFullNameUseCase = mockk()
    private val mockGetStateUseCase: GetStateUseCase = mockk()
    private val mockToastCreator: ToastCreator = mockk(relaxUnitFun = true)

    @Before
    fun setUp() {
        doAfterFragmentInjection<LoginFragment> { fragment ->
            fragment.viewModelFactory = LoginViewModelFactory(
                LoginViewModel(
                    mockLoginCompletabler,
                    mockObserveUserFullNameUseCase,
                    mockGetStateUseCase,
                    LoginViewState()
                ).asProvider()
            )
        }

        doAfterActivityInjection<LoginActivity> { activity ->
            activity.viewModelFactory = ActivityLoginViewModelFactory(
                ActivityLoginViewModel(
                    ActivityLoginViewState()
                ).asProvider()
            )
            activity.toastCreator = mockToastCreator
        }

        // Mock methods that are called during initialization with no-op responses
        mockGetStateUseCase.mockExecute { Maybe.never() }
        mockObserveUserFullNameUseCase.mockExecute { Observable.never() }
    }

    @Test
    fun whenGetStateUseCaseReceivesAnyValue_thenShowHeaderIsSetToVisible() {
        // GIVEN
        mockGetStateUseCase.mockExecute(true) { Maybe.just(true) }

        // WHEN
        launchActivity<LoginActivity>()

        // THEN
        onView(withId(R.id.header)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    @Test
    fun whenGetStateUseCaseReceivesNoValue_thenShowHeaderIsSetToGone() {
        // GIVEN
        mockGetStateUseCase.mockExecute(true) { Maybe.empty() }

        // WHEN
        launchActivity<LoginActivity>()

        // THEN
        onView(withId(R.id.header)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)))
    }

    @Test
    fun whenObserveUserFullNameReceivesValue_thenItIsSetToLoggedAsTest() {
        // GIVEN
        mockObserveUserFullNameUseCase.mockExecute { Observable.just("A B") }

        // WHEN
        launchActivity<LoginActivity>()

        // THEN
        onView(withId(R.id.loggedAs)).check(matches(withText(containsString("A B"))))
    }

    @Test
    fun whenNameAndSurnameAreFilledAndSubmitIsClicked_andUsecaseIsSuccessful_thenToastWithSuccessfulMessageIsShown() {
        // GIVEN
        val args = SyncLoginUseCase.LoginData("name", "surname")
        mockLoginCompletabler.mockExecute(args) { Completable.complete() }

        // WHEN
        launchActivity<LoginActivity>()
        onView(withId(R.id.name)).perform(typeText("name"))
        onView(withId(R.id.surname)).perform(typeText("surname"), closeSoftKeyboard())
        onView(withId(R.id.submit)).perform(click())

        // THEN
        verify { mockToastCreator.showToast(any(), "LoginActivity test toast: Successfully logged in!") }
    }

    @Test
    fun whenNameAndSurnameAreFilledAndSubmitIsClicked_andUsecaseIsNotSuccessful_thenToastErrorMessageIsShown() {
        // GIVEN
        val args = SyncLoginUseCase.LoginData("name", "surname")
        mockLoginCompletabler.mockExecute(args) { Completable.error(IllegalStateException("TEST")) }

        // WHEN
        launchActivity<LoginActivity>()
        onView(withId(R.id.name)).perform(typeText("name"))
        onView(withId(R.id.surname)).perform(typeText("surname"), closeSoftKeyboard())
        onView(withId(R.id.submit)).perform(click())

        // THEN
        verify { mockToastCreator.showToast(any(), "LoginActivity test toast: Login error!") }
    }
}
