package app.futured.arkitekt.examplehilt.ui.first

import app.futured.arkitekt.core.event.Event

sealed class FirstEvent : Event<FirstViewState>()
data class NavigateToSecondFragmentEvent(val number: Int) : FirstEvent()
