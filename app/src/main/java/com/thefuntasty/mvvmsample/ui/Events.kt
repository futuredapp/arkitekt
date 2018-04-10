package com.thefuntasty.mvvmsample.ui

import com.thefuntasty.mvvm.event.Event

object ShowDetailEvent : Event()

object ShowFormEvent : Event()

data class ShowToastEvent(val message: String): Event()
