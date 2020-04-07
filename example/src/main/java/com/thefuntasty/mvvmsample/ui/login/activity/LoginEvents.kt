package com.thefuntasty.mvvmsample.ui.login.activity

import app.futured.arkitekt.core.event.Event

sealed class FormEvent : Event<LoginViewState>()

data class ShowToastEvent(val message: String) : FormEvent()
