package com.thefuntasty.mvvmsample.ui.form

import com.thefuntasty.mvvm.event.Event

sealed class FormEvent : Event<FormViewState>()

data class ShowToastEvent(val message: String) : FormEvent()
