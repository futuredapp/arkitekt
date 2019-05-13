package com.thefuntasty.mvvmsample.ui.login.activity

import com.thefuntasty.mvvm.event.Event

sealed class FormEvent : Event<LoginViewState>()

data class ShowToastEvent(val message: String) : FormEvent()
