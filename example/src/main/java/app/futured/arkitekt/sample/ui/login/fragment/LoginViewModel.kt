package app.futured.arkitekt.sample.ui.login.fragment

import android.view.View
import com.thefuntasty.mvvm.rxusecases.BaseRxViewModel
import app.futured.arkitekt.sample.domain.GetStateUseCase
import app.futured.arkitekt.sample.domain.ObserveUserFullNameUseCase
import app.futured.arkitekt.sample.domain.SyncLoginUseCase
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginCompletabler: SyncLoginUseCase,
    private val observeUserFullNameUseCase: ObserveUserFullNameUseCase,
    private val getStateUseCase: GetStateUseCase,
    override val viewState: LoginViewState
) : BaseRxViewModel<LoginViewState>() {

    override fun onStart() {
        getStateUseCase.execute(true) {
            onSuccess { viewState.showHeader.value = View.VISIBLE }
        }

        observeUserFullNameUseCase.execute(Unit) {
            onNext { viewState.fullName.value = it }
        }
    }

    fun logIn() = with(viewState) {
        loginCompletabler.execute(SyncLoginUseCase.LoginData(name.value, surname.value)) {
            onComplete { sendEvent(NotifyActivityEvent("Successfully logged in!")) }
            onError { sendEvent(NotifyActivityEvent("Login error!")) }
        }
    }

    fun onBack() = sendEvent(NavigateBackEvent)
}
