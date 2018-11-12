package com.thefuntasty.mvvmsample.ui.login.fragment

import com.thefuntasty.mvvm.BaseViewModel
import javax.inject.Inject

/**
 * TODO implement some logic
 */
class LoginViewModel @Inject constructor(): BaseViewModel<LoginViewState>() {
    override val viewState = LoginViewState
}
