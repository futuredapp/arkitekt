package app.futured.arkitekt.core.testactivity

import app.futured.arkitekt.core.event.Event

sealed class TestEvent : Event<TestViewState>()

object EmptyEvent : TestEvent()
