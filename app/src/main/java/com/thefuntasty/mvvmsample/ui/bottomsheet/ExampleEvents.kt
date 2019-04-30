package com.thefuntasty.mvvmsample.ui.bottomsheet

import com.thefuntasty.mvvm.event.Event

sealed class ExampleEvent : Event<ExampleViewState>()

object CloseEvent : ExampleEvent()
