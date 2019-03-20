package com.thefuntasty.mvvmsample.ui.login.activity

import com.thefuntasty.mvvm.factory.BaseViewModelFactory
import javax.inject.Inject
import javax.inject.Provider

class LoginViewModelFactory @Inject constructor(
    override val viewModelProvider: Provider<LoginViewModel>
) : BaseViewModelFactory<LoginViewModel>()
