package app.futured.arkitekt.examplehilt.ui.bottomsheet

import app.futured.arkitekt.core.event.Event

sealed class SomeEvent : Event<SomeViewState>()

object CloseEvent : SomeEvent()
