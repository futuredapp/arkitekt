package app.futured.arkitekt.sample.ui.login

import app.futured.arkitekt.core.event.Event

sealed class LoginEvent : Event<LoginViewState>()

data class ShowToastEvent(val message: String) : LoginEvent()

object NavigateBackEvent : LoginEvent()
