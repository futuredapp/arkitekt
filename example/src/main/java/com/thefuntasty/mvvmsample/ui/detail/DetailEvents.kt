package com.thefuntasty.mvvmsample.ui.detail

import app.futured.arkitekt.core.event.Event

sealed class DetailEvent : Event<DetailViewState>()

object NavigateBackEvent : DetailEvent()
