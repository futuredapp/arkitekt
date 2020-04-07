package com.thefuntasty.mvvmsample.ui.coroutinesresult

import app.futured.arkitekt.core.event.Event

sealed class CoroutinesResultEvent : Event<CoroutinesResultViewState>()

object NavigateBackEvent : CoroutinesResultEvent()
