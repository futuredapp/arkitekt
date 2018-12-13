package com.thefuntasty.mvvmsample.ui.login.fragment

import android.view.View
import com.thefuntasty.interactors.BaseRxViewModel
import com.thefuntasty.mvvmsample.domain.LoginInteractor
import com.thefuntasty.mvvmsample.domain.StateInteractor
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginInteractor: LoginInteractor,
    private val stateInteractor: StateInteractor
) : BaseRxViewModel<LoginViewState>() {
    override val viewState = LoginViewState()

    override fun onStart() {
        stateInteractor.init(true).execute({
            viewState.showHeader.value = View.VISIBLE
        },{
            // do nothing
        })
    }

    fun logIn() = with(viewState) {
        loginInteractor.init(name.value, surname.value).execute {
            val fullNameString = "${it.first} ${it.second}"

            fullName.value = fullNameString
            sendEvent(NotifyActivityEvent(fullNameString))
        }
    }


}
