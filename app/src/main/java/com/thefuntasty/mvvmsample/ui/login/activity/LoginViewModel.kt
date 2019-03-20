package com.thefuntasty.mvvmsample.ui.login.activity

import com.thefuntasty.interactors.BaseRxViewModel
import javax.inject.Inject

class LoginViewModel @Inject constructor() : BaseRxViewModel<LoginViewState>() {
    override val viewState: LoginViewState = LoginViewState
}
