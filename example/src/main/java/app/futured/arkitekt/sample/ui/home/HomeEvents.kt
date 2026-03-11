package app.futured.arkitekt.sample.ui.home

import app.futured.arkitekt.core.event.Event

sealed class HomeEvent : Event<HomeViewState>()

object ShowDetailEvent : HomeEvent()

object ShowFormEvent : HomeEvent()

object ShowLoginEvent : HomeEvent()

object ShowBottomSheetEvent : HomeEvent()

object ShowLoadEvent : HomeEvent()
