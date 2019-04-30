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
        stateInteractor.init(true).execute({
            viewState.showHeader.value = View.VISIBLE
        }, {
            // do nothing
        })

        getUserFullNameObservabler.execute { fullName ->
            viewState.fullName.value = fullName
        }
    }

    fun logIn() = with(viewState) {
        loginCompletabler.init(name.value, surname.value).execute({
            sendEvent(NotifyActivityEvent("Successfully logged in!"))
        }, {
            sendEvent(NotifyActivityEvent("Login error!"))
        })
    }
}
