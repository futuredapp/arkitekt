package com.thefuntasty.mvvmsample.ui.login.fragment

import com.thefuntasty.interactors.BaseRxViewModel
import com.thefuntasty.mvvmsample.domain.LoginInteractor
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginInteractor: LoginInteractor
) : BaseRxViewModel<LoginViewState>() {
    override val viewState = LoginViewState()

    fun logIn() = with(viewState) {
        loginInteractor.init(name.value, surname.value).execute {
            val fullNameString = "${it.first} ${it.second}"

            fullName.value = fullNameString
            sendEvent(NotifyActivityEvent(fullNameString))
        }
    }
}
