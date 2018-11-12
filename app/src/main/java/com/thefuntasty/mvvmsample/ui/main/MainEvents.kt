package com.thefuntasty.mvvmsample.ui.main

import com.thefuntasty.mvvm.event.Event

sealed class MainEvent : Event<MainViewState>()

object ShowDetailEvent : MainEvent()

object ShowFormEvent : MainEvent()

object ShowLoginEvent : MainEvent()
