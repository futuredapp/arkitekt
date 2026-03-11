package app.futured.arkitekt.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.futured.arkitekt.core.event.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseCoreViewModel<VS : ViewState> : ViewModel() {
    abstract val viewState: VS

    private val eventChannel = Channel<Event<VS>>(Channel.BUFFERED)
    val events = eventChannel
        .receiveAsFlow()
        .flowOn(Dispatchers.Main)

    fun sendEvent(event: Event<VS>) = viewModelScope.launch {
        eventChannel.send(event)
    }
}