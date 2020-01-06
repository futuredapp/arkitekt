package com.thefuntasty.mvvmsample.ui.login.activity

import com.thefuntasty.mvvm.rxusecases.BaseRxViewModel
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    override val viewState: LoginViewState
) : BaseRxViewModel<LoginViewState>() {

    fun sendToastEvent(message: String) {
        sendEvent(ShowToastEvent("LoginActivity test toast: $message"))
    }
}
