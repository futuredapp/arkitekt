package app.futured.arkitekt.sample.ui.form

import app.futured.arkitekt.core.event.Event

sealed class FormEvent : Event<FormViewState>()

data class ShowToastEvent(val message: String) : FormEvent()

object NavigateBackEvent : FormEvent()
