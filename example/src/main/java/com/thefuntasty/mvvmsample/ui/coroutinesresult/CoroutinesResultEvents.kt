package com.thefuntasty.mvvmsample.ui.coroutinesresult

import com.thefuntasty.mvvm.event.Event

sealed class CoroutinesResultEvent : Event<CoroutinesResultViewState>()

object NavigateBackEvent : CoroutinesResultEvent()
