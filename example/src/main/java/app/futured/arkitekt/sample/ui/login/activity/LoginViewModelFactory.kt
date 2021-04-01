package app.futured.arkitekt.sample.ui.login.activity

import app.futured.arkitekt.dagger.factory.BaseViewModelFactory
import javax.inject.Inject
import javax.inject.Provider

class LoginViewModelFactory @Inject constructor(
    override val viewModelProvider: Provider<LoginViewModel>
) : BaseViewModelFactory<LoginViewModel>() {
    override val viewModelClass = LoginViewModel::class
}
