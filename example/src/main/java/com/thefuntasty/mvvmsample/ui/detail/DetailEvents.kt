package com.thefuntasty.mvvmsample.ui.detail

import com.thefuntasty.mvvm.event.Event

sealed class DetailEvent : Event<DetailViewState>()

object NavigateBackEvent : DetailEvent()
