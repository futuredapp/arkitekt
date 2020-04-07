package app.futured.arkitekt.core.event

import androidx.collection.ArrayMap
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import app.futured.arkitekt.core.ViewState
import kotlin.reflect.KClass

internal class LiveEventBus<T : ViewState> {

    private val eventMap: ArrayMap<KClass<out Event<T>>, LiveEvent<out Event<T>>> = ArrayMap()

    @Suppress("UNCHECKED_CAST")
    fun observe(lifecycleOwner: LifecycleOwner, eventClass: KClass<out Event<T>>, observer: (Event<T>) -> Unit) {
        val liveEvent: LiveEvent<Event<T>> = eventMap[eventClass] as LiveEvent<Event<T>>? ?: initLiveEvent(eventClass)
        liveEvent.observe(lifecycleOwner, Observer { observer(it) })
    }

    fun send(event: Event<T>) {
        val liveEvent: LiveEvent<*> = eventMap[event::class] ?: initLiveEvent(event::class)
        liveEvent.value = event
    }

    private fun initLiveEvent(eventClass: KClass<out Event<T>>): LiveEvent<Event<T>> {
        val liveEvent = LiveEvent<Event<T>>()
        eventMap[eventClass] = liveEvent
        return liveEvent
    }
}
