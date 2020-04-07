package com.thefuntasty.mvvmsample.ui.login.fragment

import app.futured.arkitekt.core.event.Event

sealed class LoginEvent : Event<LoginViewState>()

data class NotifyActivityEvent(val message: String) : LoginEvent()

object NavigateBackEvent : LoginEvent()
