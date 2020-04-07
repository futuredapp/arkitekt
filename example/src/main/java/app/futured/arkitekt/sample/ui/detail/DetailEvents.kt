package app.futured.arkitekt.sample.ui.detail

import app.futured.arkitekt.core.event.Event

sealed class DetailEvent : Event<DetailViewState>()

object NavigateBackEvent : DetailEvent()
