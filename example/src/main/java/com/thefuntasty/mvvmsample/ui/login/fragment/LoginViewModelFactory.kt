package com.thefuntasty.mvvmsample.ui.login.fragment

import com.thefuntasty.mvvm.factory.BaseViewModelFactory
import javax.inject.Inject
import javax.inject.Provider

class LoginViewModelFactory @Inject constructor(
    override val viewModelProvider: Provider<LoginViewModel>
) : BaseViewModelFactory<LoginViewModel>() {
    override val viewModelClass = LoginViewModel::class
}
