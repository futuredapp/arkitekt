package com.thefuntasty.mvvmsample.ui.login.activity

import com.thefuntasty.mvvm.rxusecases.BaseRxViewModel
import javax.inject.Inject

class LoginViewModel @Inject constructor() : BaseRxViewModel<LoginViewState>() {
    override val viewState: LoginViewState = LoginViewState

    fun sendToastEvent(message: String) {
        sendEvent(ShowToastEvent("LoginActivity test toast: $message"))
    }
}
