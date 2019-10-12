package com.thefuntasty.mvvm.viewmodel.testactivity

import com.thefuntasty.mvvm.event.Event

sealed class TestEvent : Event<TestViewState>()

object EmptyEvent : TestEvent()
