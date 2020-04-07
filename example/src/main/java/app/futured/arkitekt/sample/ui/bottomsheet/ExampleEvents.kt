package app.futured.arkitekt.sample.ui.bottomsheet

import app.futured.arkitekt.core.event.Event

sealed class ExampleEvent : Event<ExampleViewState>()

object CloseEvent : ExampleEvent()
