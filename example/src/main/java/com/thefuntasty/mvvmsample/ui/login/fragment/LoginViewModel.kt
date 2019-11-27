package com.thefuntasty.mvvmsample.ui.login.fragment

import android.view.View
import com.thefuntasty.interactors.BaseRxViewModel
import com.thefuntasty.mvvmsample.domain.GetUserFullNameObservabler
import com.thefuntasty.mvvmsample.domain.LoginCompletabler
import com.thefuntasty.mvvmsample.domain.StateInteractor
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginCompletabler: LoginCompletabler,
    private val getUserFullNameObservabler: GetUserFullNameObservabler,
    private val stateInteractor: StateInteractor
) : BaseRxViewModel<LoginViewState>() {
    override val viewState = LoginViewState()

    override fun onStart() {
        stateInteractor.execute(true) {
            onSuccess { viewState.showHeader.value = View.VISIBLE }
            onError { }
        }

        getUserFullNameObservabler.execute(Unit) {
            onNext { viewState.fullName.value = it }
        }
    }

    fun logIn() = with(viewState) {
        loginCompletabler.execute(LoginCompletabler.LoginData(name.value, surname.value)) {
            onComplete { sendEvent(NotifyActivityEvent("Successfully logged in!")) }
            onError { sendEvent(NotifyActivityEvent("Login error!")) }
        }
    }

    fun onBack() = sendEvent(NavigateBackEvent)
}
