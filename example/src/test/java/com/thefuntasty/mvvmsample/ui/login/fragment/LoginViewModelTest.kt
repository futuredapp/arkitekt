package com.thefuntasty.mvvmsample.ui.login.fragment

import android.view.View
import com.thefuntasty.mvvm.rxusecases.test.everyExecute
import com.thefuntasty.mvvm.test.ViewModelTest
import com.thefuntasty.mvvmsample.domain.GetStateUseCase
import com.thefuntasty.mvvmsample.domain.ObserveUserFullNameUseCase
import com.thefuntasty.mvvmsample.domain.SyncLoginUseCase
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LoginViewModelTest : ViewModelTest() {

    val mockLoginCompletabler: SyncLoginUseCase = mockk()
    val mockObserveUserFullNameUseCase: ObserveUserFullNameUseCase = mockk()
    val mockGetStateUseCase: GetStateUseCase = mockk()

    lateinit var viewState: LoginViewState
    lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        viewState = LoginViewState()
        viewModel = spyk(
            LoginViewModel(mockLoginCompletabler, mockObserveUserFullNameUseCase, mockGetStateUseCase, viewState),
            recordPrivateCalls = true
        )
    }

    @Test
    fun `when onStart is called and get state is successful then header is visible`() {
        // GIVEN
        mockObserveUserFullNameUseCase.everyExecute { Observable.never() }
        mockGetStateUseCase.everyExecute(true) { Maybe.just(false) }

        // WHEN
        viewModel.onStart()

        // THEN
        assertEquals(View.VISIBLE, viewState.showHeader.value)
    }

    @Test
    fun `when onStart is called and get state is not successful then header is not visible`() {
        // GIVEN
        mockObserveUserFullNameUseCase.everyExecute { Observable.never() }
        mockGetStateUseCase.everyExecute(true) { Maybe.empty() }

        // WHEN
        viewModel.onStart()

        // THEN
        assertEquals(View.INVISIBLE, viewState.showHeader.value)
    }

    @Test
    fun `when onStart is called then full name is set to last observed value`() {
        // GIVEN
        mockGetStateUseCase.everyExecute(true) { Maybe.never() }
        mockObserveUserFullNameUseCase.everyExecute { Observable.just("first", "second") }

        // WHEN
        viewModel.onStart()

        // THEN
        assertEquals("second", viewState.fullName.value)
    }

    @Test
    fun `when login is called then name and surname is send to interactor`() {
        // GIVEN
        mockGetStateUseCase.everyExecute(true) { Maybe.never() }
        viewState.name.value = "name"
        viewState.surname.value = "surname"
        mockLoginCompletabler.everyExecute { Completable.never() }

        // WHEN
        viewModel.logIn()

        // THEN
        verify { mockLoginCompletabler.create(SyncLoginUseCase.LoginData("name", "surname")) }
    }

    @Test
    fun `when login is called and use case is successful then event is send`() {
        // GIVEN
        mockLoginCompletabler.everyExecute { Completable.complete() }

        // WHEN
        viewModel.logIn()

        // THEN
        verify { viewModel.sendEvent(NotifyActivityEvent("Successfully logged in!")) }
    }

    @Test
    fun `when login is called and use case is not successful then event is send`() {
        // GIVEN
        mockLoginCompletabler.everyExecute { Completable.error(IllegalStateException()) }

        // WHEN
        viewModel.logIn()

        // THEN
        verify { viewModel.sendEvent(NotifyActivityEvent("Login error!")) }
    }

    @Test
    fun `when onBack is called then NavigateBackEvent is send`() {
        // WHEN
        viewModel.onBack()

        // THEN
        verify { viewModel.sendEvent(NavigateBackEvent) }
    }
}
