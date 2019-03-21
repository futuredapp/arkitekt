package com.thefuntasty.mvvmsample.ui.login.fragment

import com.thefuntasty.mvvm.event.Event

sealed class FormEvent : Event<LoginViewState>()

data class NotifyActivityEvent(val message: String) : FormEvent()
