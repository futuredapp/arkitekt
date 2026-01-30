package app.futured.arkitekt.sample.ui.login.activity

import app.futured.arkitekt.crusecases.BaseCrViewModel
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    override val viewState: LoginViewState
) : BaseCrViewModel<LoginViewState>() {

    fun sendToastEvent(message: String) {
        sendEvent(ShowToastEvent("LoginActivity test toast: $message"))
    }
}
