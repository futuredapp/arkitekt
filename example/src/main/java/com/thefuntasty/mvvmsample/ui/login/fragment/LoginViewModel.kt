package com.thefuntasty.mvvmsample.ui.login.fragment

import android.view.View
import com.thefuntasty.mvvm.rxusecases.BaseRxViewModel
import com.thefuntasty.mvvmsample.domain.GetStateUseCase
import com.thefuntasty.mvvmsample.domain.ObserveUserFullNameUseCase
import com.thefuntasty.mvvmsample.domain.SyncLoginUseCase
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginCompletabler: SyncLoginUseCase,
    private val observeUserFullNameUseCase: ObserveUserFullNameUseCase,
    private val getStateUseCase: GetStateUseCase
) : BaseRxViewModel<LoginViewState>() {
    override val viewState = LoginViewState()

    override fun onStart() {
        getStateUseCase.execute(true) {
            onSuccess { viewState.showHeader.value = View.VISIBLE }
            onError { }
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
