package com.thefuntasty.mvvmsample.ui.main

import app.futured.arkitekt.core.event.Event

sealed class MainEvent : Event<MainViewState>()

object ShowDetailEvent : MainEvent()

object ShowFormEvent : MainEvent()

object ShowLoginEvent : MainEvent()

object ShowBottomSheetEvent : MainEvent()

object ShowLoadEvent : MainEvent()
