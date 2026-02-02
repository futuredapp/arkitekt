package app.futured.arkitekt.sample.ui.login

import android.view.View
import app.futured.arkitekt.crusecases.BaseViewModel
import app.futured.arkitekt.sample.domain.GetStateUseCase
import app.futured.arkitekt.sample.domain.ObserveUserFullNameUseCase
import app.futured.arkitekt.sample.domain.SyncLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginCompletabler: SyncLoginUseCase,
    private val observeUserFullNameUseCase: ObserveUserFullNameUseCase,
    private val getStateUseCase: GetStateUseCase,
    override val viewState: LoginViewState
) : BaseViewModel<LoginViewState>() {

    init {
        getStateUseCase.execute(true) {
            onNext { viewState.showHeader.value = View.VISIBLE }
        }

        observeUserFullNameUseCase.execute(Unit) {
            onNext { viewState.fullName.value = it }
        }
    }

    fun logIn() = with(viewState) {
        loginCompletabler.execute(SyncLoginUseCase.LoginData(name.value, surname.value)) {
            onSuccess { sendEvent(ShowToastEvent("Successfully logged in!")) }
            onError { sendEvent(ShowToastEvent("Login error!")) }
        }
    }

    fun onBack() = sendEvent(NavigateBackEvent)
}
